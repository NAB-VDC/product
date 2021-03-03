package com.bank.product.config;

import java.util.Arrays;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class SecurityFeignRequestInterceptor implements RequestInterceptor {
	private static final String BEARER = "Bearer";
	private static final String AUTHORIZATION = "Authorization";

	private final OAuth2ClientContext oAuth2ClientContext;
	private final OAuth2ProtectedResourceDetails resource;
	private ClientCredentialsResourceDetails clientCredentialsResource;

	private AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(Arrays.<AccessTokenProvider>asList(
			new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
			new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider()));

	public SecurityFeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext,
			OAuth2ProtectedResourceDetails resource, ClientCredentialsResourceDetails clientCredentialsResource) {
		this.oAuth2ClientContext = oAuth2ClientContext;
		this.resource = resource;
		this.clientCredentialsResource = clientCredentialsResource;
	}

	/**
	 * Create a template with the header of provided name and extracted extract
	 *
	 * @see RequestInterceptor#apply(RequestTemplate)
	 */
	@Override
	public void apply(RequestTemplate template) {
		if (!template.url().startsWith("/public")) {
			OAuth2AccessToken accessToken = getToken();
			template.header(AUTHORIZATION, String.format("%s %s", BEARER, accessToken.getValue()));
		}
	}

	/**
	 * Extract the access token within the request or try to acquire a new one by
	 * delegating it to {@link #acquireAccessToken()}
	 *
	 * @return valid token
	 */
	private OAuth2AccessToken getToken() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth instanceof AnonymousAuthenticationToken) {
			return accessTokenProvider.obtainAccessToken(clientCredentialsResource, new DefaultAccessTokenRequest());
		}
		OAuth2AccessToken accessToken = oAuth2ClientContext.getAccessToken();
		if (accessToken == null || accessToken.isExpired()) {
			try {
				accessToken = acquireAccessToken();
			} catch (UserRedirectRequiredException e) {
				oAuth2ClientContext.setAccessToken(null);
				String stateKey = e.getStateKey();
				if (stateKey != null) {
					Object stateToPreserve = e.getStateToPreserve();
					if (stateToPreserve == null) {
						stateToPreserve = "NONE";
					}
					oAuth2ClientContext.setPreservedState(stateKey, stateToPreserve);
				}
				throw e;
			}
		}
		return accessToken;
	}

	/**
	 * Try to acquire the token using a access token provider
	 *
	 * @throws UserRedirectRequiredException in case the user needs to be redirected
	 *                                       to an approval page or login page
	 * @return valid access token
	 */
	private OAuth2AccessToken acquireAccessToken() throws UserRedirectRequiredException {
		AccessTokenRequest tokenRequest = oAuth2ClientContext.getAccessTokenRequest();
		if (tokenRequest == null) {
			throw new AccessTokenRequiredException(
					"Cannot find valid context on request for resource '" + resource.getId() + "'.", resource);
		}
		String stateKey = tokenRequest.getStateKey();
		if (stateKey != null) {
			tokenRequest.setPreservedState(oAuth2ClientContext.removePreservedState(stateKey));
		}
		OAuth2AccessToken existingToken = oAuth2ClientContext.getAccessToken();
		if (existingToken != null) {
			oAuth2ClientContext.setAccessToken(existingToken);
		}
		OAuth2AccessToken obtainableAccessToken;
		obtainableAccessToken = accessTokenProvider.obtainAccessToken(resource, tokenRequest);
		if (obtainableAccessToken == null || obtainableAccessToken.getValue() == null) {
			throw new IllegalStateException(
					" Access token provider returned a null token, which is illegal according to the contract.");
		}
		oAuth2ClientContext.setAccessToken(obtainableAccessToken);
		return obtainableAccessToken;
	}
}

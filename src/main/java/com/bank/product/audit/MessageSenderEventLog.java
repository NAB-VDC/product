package com.bank.product.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class MessageSenderEventLog {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public boolean send(MessageChannel channel, Object event) {
		logEvent(event);
		return channel.send(MessageBuilder.withPayload(event).build());
	}
	
	private void logEvent(Object event) {
        //TODO: write logging when message sending
		logger.debug("Message Payload: {}", event);
    }
}

package fr.ministone.game;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;

public class MSimpMessagingTemplate extends AbstractMessageSendingTemplate<String> {
    @Override
	public void convertAndSend(String destination, Object payload) throws MessagingException {
	}

    @Override
    protected void doSend(String destination, Message<?> message) {

    }
}
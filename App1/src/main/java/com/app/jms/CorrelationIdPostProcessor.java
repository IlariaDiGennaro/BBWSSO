package com.app.jms;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.core.MessagePostProcessor;

public class CorrelationIdPostProcessor implements MessagePostProcessor {
	
	private String correlationID;

	public CorrelationIdPostProcessor(String correlationID) {
		this.correlationID = correlationID;
	}

	@Override
	public Message postProcessMessage(Message message) throws JMSException {
		message.setJMSCorrelationID(correlationID);
		return message;
	}

}

package com.micro.mq.ibm;

import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class ReceiveMessage extends MessageListenerAdapter {
	@Autowired
	JmsOperations jmsOperations;

	@Override
	@JmsListener(destination = "Q1")
	public void onMessage(Message message) {
		String messageBody = new String(message.toString());
		System.out.println("成功监听Q1消息队列，传来的值为:" + messageBody);
	}
}

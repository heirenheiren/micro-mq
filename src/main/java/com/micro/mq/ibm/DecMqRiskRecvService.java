package com.micro.mq.ibm;

import java.io.UnsupportedEncodingException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Service;

import com.micro.mq.ibmconfig.MqProperties;

@Service
public class DecMqRiskRecvService implements MessageListener {

	@Autowired
	private JmsOperations jmsOperations;
	@Autowired
	private MqProperties mqProperties;

	@Override
	public void onMessage(Message message) {
		String str = null;
		// 1、读取报文
		try {
			if (message instanceof BytesMessage) {
				BytesMessage bm = (BytesMessage) message;
				byte[] bys = null;
				bys = new byte[(int) bm.getBodyLength()];
				bm.readBytes(bys);
				str = new String(bys, "UTF-8");
			} else {
				str = ((TextMessage) message).getText();
				str = new String(str.getBytes("ISO-8859-1"), "UTF-8");
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 2、处理报文

		// 3、组装回执发送
		String receipt = "";
		try {
			jmsOperations.convertAndSend("queue:///" + mqProperties.getBaseQueueNameSend() + "?targetClient=1",
					receipt.getBytes("UTF-8"));
		} catch (JmsException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}

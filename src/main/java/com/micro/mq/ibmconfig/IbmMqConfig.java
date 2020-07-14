package com.micro.mq.ibmconfig;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.transaction.PlatformTransactionManager;

import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.micro.mq.ibm.DecMqRiskRecvService;

@Configuration
public class IbmMqConfig {

	@Autowired
	private MqProperties mqProperties;

	/** =======================MQ 通道工厂============================ **/
	@Bean(name = "mqQueueConnectionFactory")
	public MQQueueConnectionFactory mqQueueConnectionFactory() {
		MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
		mqQueueConnectionFactory.setHostName(mqProperties.getHostName());
		try {
			mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
			mqQueueConnectionFactory.setCCSID(mqProperties.getCcsid());
			mqQueueConnectionFactory.setChannel(mqProperties.getChannel());
			mqQueueConnectionFactory.setPort(mqProperties.getPort());
			mqQueueConnectionFactory.setQueueManager(mqProperties.getQueueManager());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return mqQueueConnectionFactory;
	}

	@Bean(name = "userCredentialsConnectionFactoryAdapter")
	public UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter(
			MQQueueConnectionFactory mqQueueConnectionFactory) {
		UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
		userCredentialsConnectionFactoryAdapter.setUsername(mqProperties.getUserName());
		userCredentialsConnectionFactoryAdapter.setPassword(mqProperties.getPassword());
		userCredentialsConnectionFactoryAdapter.setTargetConnectionFactory(mqQueueConnectionFactory);
		return userCredentialsConnectionFactoryAdapter;
	}

	/** ============================MQ 消息监听接收============================= **/
	// 队列连接
	@Bean(name = "mqueue")
	public MQQueue mqueue() {
		MQQueue mqQueue = new MQQueue();
		try {
			mqQueue.setBaseQueueName(mqProperties.getBaseQueueNameRecv());
			mqQueue.setBaseQueueManagerName(mqProperties.getBaseQueueManagerName());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return mqQueue;
	}

	// 对队列进行监听
	@Bean(name = "simpleMessageListenerContainer")
	public SimpleMessageListenerContainer simpleMessageListenerContainer(
			UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter, MQQueue mqueue) {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(userCredentialsConnectionFactoryAdapter);
		simpleMessageListenerContainer.setDestination(mqueue);
		simpleMessageListenerContainer.setMessageListener(decMqRiskRecvService());
		return simpleMessageListenerContainer;
	}

	// 报文处理类
	@Bean(name = "decMqRiskRecvService")
	public DecMqRiskRecvService decMqRiskRecvService() {
		return new DecMqRiskRecvService();
	}

	/** ============================MQ 发送消息============================ **/
	@Bean(name = "cachingConnectionFactory")
	public CachingConnectionFactory cachingConnectionFactory(
			UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter) {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setTargetConnectionFactory(userCredentialsConnectionFactoryAdapter);
		cachingConnectionFactory.setSessionCacheSize(5);
		cachingConnectionFactory.setReconnectOnException(true);
		return cachingConnectionFactory;
	}

	@Bean(name = "jmsTransactionManager")
	public PlatformTransactionManager jmsTransactionManager(CachingConnectionFactory cachingConnectionFactory) {
		JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
		jmsTransactionManager.setConnectionFactory(cachingConnectionFactory);
		return jmsTransactionManager;
	}

	@Bean(name = "jmsOperations")
	public JmsOperations jmsOperations(CachingConnectionFactory cachingConnectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
		jmsTemplate.setReceiveTimeout(mqProperties.getReceiveTimeout());
		return jmsTemplate;
	}

}
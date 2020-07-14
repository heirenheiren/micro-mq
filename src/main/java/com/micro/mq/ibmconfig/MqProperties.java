package com.micro.mq.ibmconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = MqProperties.MQ_PREFIX)
public class MqProperties {
	public static final String MQ_PREFIX = "ibmmq";
	private String hostName;
	private int port;
	private String channel;
	private int ccsid;
	private String userName;
	private String password;
	private String queueManager;
	private String baseQueueManagerName;
	private String baseQueueNameRecv;
	private String baseQueueNameSend;
	private long receiveTimeout;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getCcsid() {
		return ccsid;
	}

	public void setCcsid(int ccsid) {
		this.ccsid = ccsid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQueueManager() {
		return queueManager;
	}

	public void setQueueManager(String queueManager) {
		this.queueManager = queueManager;
	}

	public String getBaseQueueManagerName() {
		return baseQueueManagerName;
	}

	public void setBaseQueueManagerName(String baseQueueManagerName) {
		this.baseQueueManagerName = baseQueueManagerName;
	}

	public String getBaseQueueNameRecv() {
		return baseQueueNameRecv;
	}

	public void setBaseQueueNameRecv(String baseQueueNameRecv) {
		this.baseQueueNameRecv = baseQueueNameRecv;
	}

	public String getBaseQueueNameSend() {
		return baseQueueNameSend;
	}

	public void setBaseQueueNameSend(String baseQueueNameSend) {
		this.baseQueueNameSend = baseQueueNameSend;
	}

	public long getReceiveTimeout() {
		return receiveTimeout;
	}

	public void setReceiveTimeout(long receiveTimeout) {
		this.receiveTimeout = receiveTimeout;
	}

}

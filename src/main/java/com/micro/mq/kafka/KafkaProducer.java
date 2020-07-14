package com.micro.mq.kafka;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.google.gson.GsonBuilder;

/**
 * Kafka消息生产类
 */
@Component
public class KafkaProducer {

	@Resource
	private KafkaTemplate<String, String> kafkaTemplate;

	@Value("${kafka.topic.user}")
	private String topicUser;// topic名称

	/**
	 * 发送用户消息
	 *
	 * @param user 用户信息
	 */
	public void sendUserMessage(User user) {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
		String message = builder.create().toJson(user);
		ListenableFuture<SendResult<String, String>> f = kafkaTemplate.send(topicUser, message);
		f.addCallback(new KafkaSendCallBack(message));
		// log.info("\n生产消息至Kafka\n" + message);
	}
}

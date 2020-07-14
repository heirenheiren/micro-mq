package com.micro.mq.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;

public class KafkaConsumerDemo {

	@Value("${kafka.topic.user}")
	private String topicUser;// topic名称

	public void consume() {
		Properties props = new Properties();

		// 必须设置的属性
		props.put("bootstrap.servers", "127.0.0.1:9092");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("group.id", "group-user");

		// 可选设置属性

		// 提交方式配置
		// 自动提交offset,每1s提交一次（提交后的消息不再消费，避免重复消费问题）
		props.put("enable.auto.commit", "true");// 自动提交offset:true【PS：只有当消息提交后，此消息才不会被再次接受到】
		props.put("auto.commit.interval.ms", "1000");// 自动提交的间隔

		// 消费方式配置
		/**
		 * earliest： 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费 latest：
		 * 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据 none：
		 * topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
		 */
		props.put("auto.offset.reset", "earliest ");// earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费

		// 拉取消息设置
		props.put("max.poll.records", "100 ");// 每次poll操作最多拉取多少条消息（一般不主动设置，取默认的就好）

		// 根据上面的配置，新增消费者对象
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		// 订阅topic-user topic
		consumer.subscribe(Collections.singletonList(topicUser));

		while (true) {
			// 从服务器开始拉取数据
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
			records.forEach(record -> {
				System.out.printf("成功消费消息：topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n",
						record.topic(), record.partition(), record.offset(), record.key(), record.value());
			});
		}
	}
}

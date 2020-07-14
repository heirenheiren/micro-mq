package com.micro.mq.scheduled;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.micro.mq.kafka.KafkaProducer;

@Component
public class ScheduledConfig {

	@Autowired
	private KafkaProducer kafkaProducer;

	@Scheduled(fixedRate = 2000)
	public void fixedRate() {
		System.out.println("fixedRate>>>" + new Date());
	}

	@Scheduled(fixedDelay = 2000)
	public void fixedDelay() {
		System.out.println("fixedDelay>>>" + new Date());
	}

	@Scheduled(initialDelay = 2000, fixedDelay = 2000)
	public void initialDelay() {
		System.out.println("initialDelay>>>" + new Date());
		kafkaProducer.sendUserMessage(null);
	}

	@Scheduled(cron = "0/5 * * * * *")
	public void cron() {
		System.out.println(new Date());
	}

}

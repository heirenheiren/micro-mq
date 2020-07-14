package com.micro.mq.kafka;

import org.springframework.util.concurrent.ListenableFutureCallback;

public class KafkaSendCallBack implements ListenableFutureCallback<Object> {

	private String message;

	public KafkaSendCallBack(String message) {
		// TODO Auto-generated constructor stub
		this.message = message;
	}

	@Override
	public void onSuccess(Object result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Throwable ex) {
		// TODO Auto-generated method stub

	}

}

package com.vinhlam.tourScheduling.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.vinhlam.tourScheduling.service.TokenUserService;



@Component
public class ScheduledTasks {
	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	@Autowired
	private TokenUserService tokenUserService;
	
	private List<String >listToken ;
	
	@Autowired
	public void ScheduledTasks() {
		listToken = tokenUserService.getAllListToken();
	}
	
//	Cứ sau 2 giây thì sẽ thực hiện gửi send message
	@Scheduled(fixedRate = 5000)
	public void scheduleTaskWithFixedRate() throws FirebaseMessagingException {
		
		sendNotificationToAlLUserByTopic(listToken);
		logger.info("Gửi tin nhắn cố định 2 giây 1 lần");
	}
	
////	2 giây khi hoàn thành 1 tác vụ thì sẽ gửi message
////	(Ở đây tác vụ đó mất 1 giây, thì tức là sau (1 + 2) là 3 giây thì sẽ gửi message)
//	@Scheduled(fixedDelay = 2000)
//	public void scheduleTaskWithFixedDelay() {
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		logger.info("2 giây sau khi hoàn thành thì gửi thông báo");
//	}
//	
//	//Tương tự như fixedRate, nhưng tin nhắn đầu tiên thì cho phép delay thời gian của initiaDelay
//	@Scheduled(fixedRate = 2000, initialDelay = 3000) 
//	public void scheduleTaskWithInitiaDelay() {
//		logger.info("Gửi tin nhắn initiaDelay");
//	}
	
	
	
//	Gửi message vào 1 thời gian cố định lặp lại, (giây thứ 30 của phút, ngày 15 háng tháng, ngày cuối cùng của tháng,..)
	@Scheduled(cron = "0 0 12 ? * *  ") // 12h mỗi ngày
	public void scheduleTaskWithCronExpression() throws FirebaseMessagingException {
		sendNotificationToAlLUserByTopic(listToken);
//		logger.info("Gửi tin nhắn vào 1 thời gian cố định được set sẵn");
	}
	
	
//	function sendNotificationToAlLUserByTopic
	public void sendNotificationToAlLUserByTopic(List<String> listToken) throws FirebaseMessagingException {
		List<String> registrationTokens = listToken;

		Notification.Builder builder = Notification.builder();
		builder.setTitle("Có sự thay đổi về giá Tour");
		builder.setBody("Check ngay ở Hahalolo.com bạn nhé!");
		
		WebpushConfig.Builder builderWeb = WebpushConfig.builder();
		builderWeb.putHeader("Header", "Có sự thay đổi về giá Tour!!");
		builderWeb.putData("Data", "Check ngay ở Hahalolo.com bạn nhé!!");
		builderWeb.setNotification(new WebpushNotification("Demo header", "Demo body"));
		
//			builderWeb.
//			Notification noti = new Notification(builder.build());
		MulticastMessage message = MulticastMessage.builder()
		    .addAllTokens(registrationTokens)
		    .putData("123", "132")
		    .setWebpushConfig(builderWeb.build())
		    .setNotification(builder.build())
		    .build();
		
		
		BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
		String messageId = response.getResponses().get(0).getMessageId();
		if (response.getFailureCount() > 0) {
		  List<SendResponse> responses = response.getResponses();
		  List<String> failedTokens = new ArrayList<>();
		  for (int i = 0; i < responses.size(); i++) {
		    if (!responses.get(i).isSuccessful()) {
		      // The order of responses corresponds to the order of the registration tokens.
		      failedTokens.add(registrationTokens.get(i));
		    }
		  }

		  System.out.println("List of tokens that caused failures: " + failedTokens);
		}
		System.out.println("Successfully sent message: " + response);
	}
}

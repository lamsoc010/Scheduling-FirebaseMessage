package com.vinhlam.tourScheduling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinhlam.tourScheduling.service.TokenUserService;

@RestController
@RequestMapping("/schedule")
public class ScueduledController {

	@Autowired
	private TokenUserService tokenUserService;
	
//	API: Get All list token divice all user
//	http://localhost:8080/schedule/getListToken
	@GetMapping("/getListToken")
	public ResponseEntity<?> getListToken() {
		List<String> listTokens = tokenUserService.getAllListToken();
		if(listTokens == null || listTokens.size() == 0) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("List Token is null");
		} else {
			return ResponseEntity.ok(listTokens);
		}
	}
}

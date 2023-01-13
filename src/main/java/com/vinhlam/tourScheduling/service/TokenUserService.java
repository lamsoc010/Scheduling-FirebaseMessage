package com.vinhlam.tourScheduling.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinhlam.tourScheduling.repository.TokenUserRepository;

@Service
public class TokenUserService {

	@Autowired
	private TokenUserRepository tokenUserRepository;
	
	public List<String> getAllListToken() {
		return tokenUserRepository.getAllListTokenUserDistinct();
	}
	
	
}

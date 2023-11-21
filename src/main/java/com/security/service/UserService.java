package com.security.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface UserService {

	ResponseEntity<String> singUp(Map<String, String> requestMap);

	ResponseEntity<String> login(Map<String, String> requestMap);

}

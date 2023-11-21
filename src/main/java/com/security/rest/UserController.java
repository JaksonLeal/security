package com.security.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.constant.FacturaConstant;
import com.security.service.UserService;
import com.security.util.FacturaUtils;

@RestController
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostMapping("/signup")
	public ResponseEntity<String> registrarUsuario(@RequestBody(required = true) Map<String, String> requestMap) {

		try {
			System.out.println(requestMap);
			requestMap.put("password", bCryptPasswordEncoder.encode(requestMap.get("password")));
			return userService.singUp(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FacturaUtils.getResponseEntity(FacturaConstant.SOMETHING_WENT_WRONT, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return userService.login(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FacturaUtils.getResponseEntity(FacturaConstant.SOMETHING_WENT_WRONT, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

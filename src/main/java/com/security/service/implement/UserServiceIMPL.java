package com.security.service.implement;

import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.security.auth.UserDetailServiceIMPL;
import com.security.auth.jwt.JwtUtil;
import com.security.constant.FacturaConstant;
import com.security.dao.UserDAO;
import com.security.pojo.User;
import com.security.service.UserService;
import com.security.util.FacturaUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceIMPL implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailServiceIMPL userDetailServiceIMPL;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public ResponseEntity<String> singUp(Map<String, String> requestMap) {
		log.info("Registro interno de un usuario{}", requestMap);

		try {
			if (validateSingudMap(requestMap)) {
				User user = userDAO.findByEmail(requestMap.get("email"));
				if (Objects.isNull(user)) {
					userDAO.save(getUserFromMap(requestMap));
					return FacturaUtils.getResponseEntity("Usuario registrado con exito", HttpStatus.CREATED);
				} else {
					return FacturaUtils.getResponseEntity("El usuario con ese email ya existe", HttpStatus.BAD_REQUEST);

				}
			} else {
				return FacturaUtils.getResponseEntity(FacturaConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return FacturaUtils.getResponseEntity(FacturaConstant.SOMETHING_WENT_WRONT, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateSingudMap(Map<String, String> requestMap) {

		if (requestMap.containsKey("nombre") && requestMap.containsKey("numeroDeContacto")
				&& requestMap.containsKey("email") && requestMap.containsKey("password")) {
			return true;
		} else {
			return false;
		}

	}

	private User getUserFromMap(Map<String, String> requestMap) {
		User user = new User();
		user.setNombre(requestMap.get("nombre"));
		user.setNumeroDeContacto(requestMap.get("numeroDeContacto"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false");
		user.setRole("user");
		return user;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		log.info("entro a login {}", requestMap);
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
			if (authentication.isAuthenticated()) {
				if (userDetailServiceIMPL.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<String>(
							"{\"token\":\"" + jwtUtil.generateToken(userDetailServiceIMPL.getUserDetail().getEmail(),
									userDetailServiceIMPL.getUserDetail().getRole()) + "\"}",
							HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("{\"mensaje\":\"" + "espere la aprobacion del admin" + "\"}",
							HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			log.error("{}", e);
		}
		return new ResponseEntity<String>("{\"mensaje\":\"" + "credenciales incorrectas" + "\"}",
				HttpStatus.BAD_REQUEST);
	}
}

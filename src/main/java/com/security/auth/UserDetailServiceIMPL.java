package com.security.auth;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.dao.UserDAO;
import com.security.pojo.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailServiceIMPL implements UserDetailsService {

	@Autowired
	private UserDAO userDAO;

	private User userDetail;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("entro a loadUserByUsername {}", username);

		userDetail = userDAO.findByEmail(username);

		if (!Objects.isNull(userDetail)) {
			return new org.springframework.security.core.userdetails.User(userDetail.getEmail(),
					userDetail.getPassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("usuario no encontrado");
		}

	}

	public User getUserDetail() {
		return userDetail;
	}

}

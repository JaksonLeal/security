package com.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.security.pojo.User;

public interface UserDAO extends JpaRepository<User, Integer> {

	User findByEmail(@Param(("email")) String email);

}

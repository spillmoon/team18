package com.loe.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loe.mapper.UserMapper;
import com.loe.model.UserInfoVO;
import com.loe.service.MainService;

@Service
public class MainServiceImpl implements MainService {

	@Autowired
	UserMapper mapper;
	
	@Override
	public int userJoin(UserInfoVO user) throws Exception {
		return this.mapper.userJoin(user);
	}

	@Override
	public UserInfoVO userLogin(HashMap<String, String> map) throws Exception {
		return this.mapper.userLogin(map);
	}
	
	
}

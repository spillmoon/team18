package com.loe.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.loe.model.UserInfoVO;

@Service
public interface MainService {

	public int userJoin(UserInfoVO user) throws Exception;
	public UserInfoVO userLogin(HashMap<String, String> map) throws Exception;
	
}

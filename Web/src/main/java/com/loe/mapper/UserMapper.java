package com.loe.mapper;

import java.util.HashMap;

import com.loe.model.UserInfoVO;

public interface UserMapper {
	public int userJoin(UserInfoVO user) throws Exception;
	public UserInfoVO userLogin(HashMap<String, String> map) throws Exception;
}

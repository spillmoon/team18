package com.loe.mapper;

import java.util.HashMap;
import java.util.List;

import com.loe.model.StoreInfoVO;
import com.loe.model.UserInfoVO;

public interface UserMapper {
	public int userJoin(UserInfoVO user) throws Exception;
	public UserInfoVO userLogin(HashMap<String, String> map) throws Exception;
	public List<StoreInfoVO> getStoreList(HashMap<String, String> map) throws Exception;
}

package com.loe.mapper;

import org.apache.ibatis.annotations.Param;

import com.loe.model.UserInfoVO;

public interface UserMapper {
	public void userInsert(UserInfoVO user)throws Exception;
}

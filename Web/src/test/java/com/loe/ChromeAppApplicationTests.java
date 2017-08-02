package com.loe;

import java.sql.Connection;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.loe.mapper.UserMapper;
import com.loe.model.UserInfoVO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ChromeAppApplication.class)
@WebAppConfiguration
public class ChromeAppApplicationTests {
	
	@Test
	public void contextLoads() {		
	}
	
}

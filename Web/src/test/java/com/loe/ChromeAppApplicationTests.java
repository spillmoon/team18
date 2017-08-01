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

//	@Autowired
//	private DataSource ds;
	
//	@Autowired
//	private SqlSessionFactory sqlSession;
	
	@Autowired
	private UserMapper mapper;
	
	@Test
	public void contextLoads() {		
	}
	
	@Test
	public void testConnection() throws Exception {
//		System.out.println("ds: " + ds);
//		Connection conn = ds.getConnection();
//		System.out.println("conn: " + conn);
//		conn.close();
		
//		System.out.println("sqlSession: " + sqlSession);
		
		UserInfoVO user = new UserInfoVO();
		user.setUser_id("spillmoon");
		user.setUser_pw("1234");
		user.setUser_name("msp");
		user.setUser_email("spillmoon@naver.com");
		System.out.println(user.toString());
		mapper.userInsert(user);
		
	}
}

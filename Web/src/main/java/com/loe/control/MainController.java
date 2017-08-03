package com.loe.control;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.loe.model.MessageInfoVO;
import com.loe.model.StoreInfoVO;
import com.loe.model.UserInfoVO;
import com.loe.service.MainService;

@Controller
public class MainController {
	@Value("${ldcc.startiot.url}")
	private String url;
	@Value("${ldcc.startiot.deviceid}")
	private String device_id;
	@Value("${ldcc.startiot.dkey}")
	private String dKey;
	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private MainService service;

	@RequestMapping(value = "/writeMessage", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public Map<String, String> writeMessage(@RequestParam Map<String, String> body) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			String user_id, message, store_index;
			store_index = body.get("store_index").toString();
			user_id = body.get("user_id").toString();
			message = body.get("message").toString();

			map.put("user_id", user_id);
			map.put("message", message);
			map.put("store_index", store_index);

			int result = service.writeMessage(map);
			if (result >= 1) {
				System.out.println("writeMessage true");
				map.put("result", "true");
			} else {
				System.out.println("writeMessage fail");
				map.put("result", "false");
			}
		} catch (Exception e) {
			map.put("result", "error");
			System.out.println(e.getMessage());
		}
		return map;
	}

	@RequestMapping(value = "/messageList", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public List<MessageInfoVO> messageList(@RequestParam Map<String, String> body) throws Exception {
		System.out.println("body: " + body);
		HashMap<String, String> map = new HashMap<String, String>();
		List<MessageInfoVO> messageList = null;
		try {
			String id = body.get("user_id").toString();
			map.put("user_id", id);

			messageList = service.messageList(map);
			for (int i = 0; i < messageList.size(); i++)
				System.out.println(messageList.get(i).getMessage());
		} catch (Exception e) {
			map.put("result", "false");
			System.out.println(e.getMessage());
		}
		return messageList;
	}

	@RequestMapping(value = "/getStoreList", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public List<StoreInfoVO> getStoreList(@RequestParam Map<String, String> body) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		List<StoreInfoVO> storeList = null;
		try {
			String centerX, centerY;
			centerX = body.get("centerX").toString();
			centerY = body.get("centerY").toString();

			map.put("centerX", centerX);
			map.put("centerY", centerY);

			storeList = service.getStoreList(map);
			for (int i = 0; i < storeList.size(); i++)
				System.out.println(storeList.get(i).getStore_name());

		} catch (Exception e) {
			map.put("result", "false");
			System.out.println(e.getMessage());
		}
		return storeList;
	}

	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public Map<String, String> userLogin(@RequestParam Map<String, String> body, HttpServletRequest req)
			throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			String id, pw;
			id = body.get("user_id").toString();
			pw = body.get("user_pw").toString();

			if (id == null || pw == null) {
				map.put("result", "false");
			} else {
				map.put("user_id", id);
				map.put("user_pw", pw);

				UserInfoVO user = service.userLogin(map);

				if (user.getUser_id().equals(id) && user.getUser_pw().equals(pw)) {
					System.out.println("userLogin true");
					map.put("result", "true");

					HttpSession session = req.getSession();
					session.setAttribute("id", id);
				} else {
					System.out.println("userLogin fail");
					map.put("result", "false");
				}
			}
		} catch (Exception e) {
			map.put("result", "error");
			System.out.println(e.getMessage());
		}
		return map;
	}

	@RequestMapping(value = "/userJoin", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public Map<String, String> userJoin(@RequestParam Map<String, String> body) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			String id, pw, name, email;
			id = body.get("user_id").toString();
			pw = body.get("user_pw").toString();
			name = body.get("user_name").toString();
			email = body.get("user_email").toString();

			if (id == null || pw == null || name == null || email == null) {
				map.put("result", "false");
			} else {
				UserInfoVO user = new UserInfoVO();
				user.setUser_id(id);
				user.setUser_pw(pw);
				user.setUser_name(name);
				user.setUser_email(email);

				int result = 0;
				result = service.userJoin(user);
				if (result > 0) {
					System.out.println("userJoin true");
					map.put("result", "true");
				} else {
					System.out.println("userJoin fail");
					map.put("result", "false");
				}
			}
		} catch (Exception e) {
			map.put("result", "error");
			System.out.println(e.getMessage());
		}
		return map;
	}

	private static String paser(String body) throws Exception {
		JSONParser jsonParser = new JSONParser();
		JSONObject result = (JSONObject) jsonParser.parse(body);
		JSONObject sgn = (JSONObject) result.get("m2m:sgn");
		JSONObject nev = (JSONObject) sgn.get("nev");
		JSONObject rep = (JSONObject) nev.get("rep");
		JSONObject om = (JSONObject) nev.get("om");
		if (om.get("op").toString().equals("1")) {
			JSONObject cin = (JSONObject) rep.get("m2m:cin");
			return (String) cin.get("con");
		} else {
			return "error";
		}
	}

	private static String containerPaser(String body) throws Exception {
		JSONParser jsonParser = new JSONParser();
		JSONObject result = (JSONObject) jsonParser.parse(body);
		JSONObject cin = (JSONObject) result.get("m2m:cin");
		return (String) cin.get("con");
	}

	
	// 페이지 최초 로딩시 겟 요청을 통한 데이터 리프레쉬..
	@RequestMapping(value = "/getTemperature", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String getTemperature() throws Exception {
		System.out.println("in getTemperature");
		String device_id = "S0004000100010004_12345671";
		String url = "http://server.norimsu.pe.kr:8080/~/charlot/base/S0004000100010004_12345671/Temperature/la";
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String temp = "no data";
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("X-M2M-RI", "RQI0001"); //
			httpGet.setHeader("X-M2M-Origin", "/S" + device_id); //
			httpGet.setHeader("Accept", "application/json");
			CloseableHttpResponse res = httpclient.execute(httpGet);

			try {
				if (res.getStatusLine().getStatusCode() == 200) {
					org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) res.getEntity();
					temp = EntityUtils.toString(entity);
					String con = containerPaser(temp);
					System.out.println("temperature: " + con);
					temp = con;
				} else {
					System.out.println("sendMgmt eerr");
				}
			} finally {
				res.close();
			}
		} finally {
			httpclient.close();
		}
		return temp;
	}

	@RequestMapping(value = "/dashboard/Temperature", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void dashboard(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {
		String content = paser(body);
		String sensor_nm = "Temperature";
		content = content+"@"+sensor_nm;
		System.out.println("Temperature in : " + content); // cr : /S0sdsasdasdasdasdasdas, // sensor_nm, con -> 실시간 데잍.
		if (content.equals("4")) {
			System.out.println("contentInstance is Deleted");
		} else {
			HttpEntity<String> entity = new HttpEntity<String>(content, headers);
			this.template.convertAndSend("/topic/subscribe", entity);
		}
	}

	// 페이지 최초 로딩시 겟 요청을 통한 데이터 리프레쉬..
	@RequestMapping(value = "/getSound", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String getSound() throws Exception {
		System.out.println("in getSound");
		String device_id = "0004000100010004_12345671";
		String url = "http://server.norimsu.pe.kr:8080/~/charlot/base/S0004000100010004_12345671/Sound/la";
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String temp = "no data";
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("X-M2M-RI", "RQI0001"); //
			httpGet.setHeader("X-M2M-Origin", "/S" + device_id); //
			httpGet.setHeader("Accept", "application/json");
			CloseableHttpResponse res = httpclient.execute(httpGet);

			try {
				if (res.getStatusLine().getStatusCode() == 200) {
					org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) res.getEntity();
					temp = EntityUtils.toString(entity);
					String con = containerPaser(temp);
					temp = con;
				} else {
					System.out.println("sendMgmt eerr");
				}
			} finally {
				res.close();
			}
		} finally {
			httpclient.close();
		}
		return temp;
	}

	@RequestMapping(value = "/dashboard/Sound", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void dashboardSound(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {
		String content = paser(body);
		String sensor_nm = "Sound";
		System.out.println("Sound in : " + content); // cr : /S0sdsasdasdasdasdasdas, sensor_nm, con -> 실시간 데잍.
		if (content.equals("4")) {
			System.out.println("contentInstance is Deleted");
		} else {
			HttpEntity<String> entity = new HttpEntity<String>(content, headers);
			this.template.convertAndSend("/topic/subscribe", entity);
		}
	}

	// 페이지 최초 로딩시 겟 요청을 통한 데이터 리프레쉬..
	@RequestMapping(value = "/getDust", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String getDust() throws Exception {
		System.out.println("in getDust");
		String device_id = "0004000100010004_12345671";
		String url = "http://server.norimsu.pe.kr:8080/~/charlot/base/S0004000100010004_12345671/Dust/la";
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String temp = "no data";
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("X-M2M-RI", "RQI0001"); //
			httpGet.setHeader("X-M2M-Origin", "/S" + device_id); //
			httpGet.setHeader("Accept", "application/json");
			CloseableHttpResponse res = httpclient.execute(httpGet);

			try {
				if (res.getStatusLine().getStatusCode() == 200) {
					org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) res.getEntity();
					temp = EntityUtils.toString(entity);
					String con = containerPaser(temp);
					temp = con;
				} else {
					System.out.println("sendMgmt eerr");
				}
			} finally {
				res.close();
			}
		} finally {
			httpclient.close();
		}
		return temp;
	}

	@RequestMapping(value = "/dashboard/Dust", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void dashboardDust(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {
		System.out.println("***************** DUST ***********************\n " + body);
		String content = paser(body);
		String sensor_nm = "Dust";
		System.out.println("Dust in : " + content); // cr : /S0sdsasdasdasdasdasdas, sensor_nm, con -> 실시간 데잍.
		if (content.equals("4")) {
			System.out.println("contentInstance is Deleted");
		} else {
			HttpEntity<String> entity = new HttpEntity<String>(content, headers);
			this.template.convertAndSend("/topic/subscribe", entity);
		}
	}

	@RequestMapping(value = "/getPressure1", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String getPressure1() throws Exception {
		System.out.println("in getPressure1");
		String device_id = "0004000100010004_12345671";
		String url = "http://server.norimsu.pe.kr:8080/~/charlot/base/S0004000100010004_12345671/Pressure1/la";
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String temp = "no data";
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("X-M2M-RI", "RQI0001"); //
			httpGet.setHeader("X-M2M-Origin", "/S" + device_id); //
			httpGet.setHeader("Accept", "application/json");
			CloseableHttpResponse res = httpclient.execute(httpGet);

			try {
				if (res.getStatusLine().getStatusCode() == 200) {
					org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) res.getEntity();
					temp = EntityUtils.toString(entity);
					String con = containerPaser(temp);
					temp = con;
				} else {
					System.out.println("sendMgmt eerr");
				}
			} finally {
				res.close();
			}
		} finally {
			httpclient.close();
		}
		return temp;
	}

	@RequestMapping(value = "/dashboard/Pressure1", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void dashboardPressure1(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {
		String content = paser(body);
		String sensor_nm = "Pressure1";
		System.out.println("Pressure1 in : " + content); // cr : /S0sdsasdasdasdasdasdas, sensor_nm, con -> 실시간 데잍.
		if (content.equals("4")) {
			System.out.println("contentInstance is Deleted");
		} else {
			HttpEntity<String> entity = new HttpEntity<String>(content, headers);
			this.template.convertAndSend("/topic/subscribe", entity);
		}
	}
	
	
	@RequestMapping(value = "/getPressure2", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public String getPressure2() throws Exception {
		System.out.println("in getPressure2");
		String device_id = "0004000100010004_12345671";
		String url = "http://server.norimsu.pe.kr:8080/~/charlot/base/S0004000100010004_12345671/Pressure2/la";
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String temp = "no data";
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader("X-M2M-RI", "RQI0001"); //
			httpGet.setHeader("X-M2M-Origin", "/S" + device_id); //
			httpGet.setHeader("Accept", "application/json");
			CloseableHttpResponse res = httpclient.execute(httpGet);

			try {
				if (res.getStatusLine().getStatusCode() == 200) {
					org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) res.getEntity();
					temp = EntityUtils.toString(entity);
					String con = containerPaser(temp);
					temp = con;
				} else {
					System.out.println("sendMgmt eerr");
				}
			} finally {
				res.close();
			}
		} finally {
			httpclient.close();
		}
		return temp;
	}

	@RequestMapping(value = "/dashboard/Pressure2", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void dashboardPressure2(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {	
		String content = paser(body);
		String sensor_nm = "Pressure2";
		System.out.println("Pressure2 in : " + content); // cr : /S0sdsasdasdasdasdasdas, sensor_nm, con -> 실시간 데잍.
		if (content.equals("4")) {
			System.out.println("contentInstance is Deleted");
		} else {
			HttpEntity<String> entity = new HttpEntity<String>(content, headers);
			this.template.convertAndSend("/topic/subscribe", entity);
		}
	}
	
//	
//	@RequestMapping(value = "/getPressure3", method = RequestMethod.GET)
//	@ResponseStatus(value = HttpStatus.OK)
//	@ResponseBody
//	public String getPressure3() throws Exception {
//		System.out.println("in getPressure3");
//		String device_id = "0004000100010003_12345671";
//		String url = "http://server.norimsu.pe.kr:8080/~/charlot/base/S0004000100010003_12345671/Pressure/la";
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//
//		String temp = "no data";
//		try {
//			HttpGet httpGet = new HttpGet(url);
//			httpGet.setHeader("X-M2M-RI", "RQI0001"); //
//			httpGet.setHeader("X-M2M-Origin", "/S" + device_id); //
//			httpGet.setHeader("Accept", "application/json");
//			CloseableHttpResponse res = httpclient.execute(httpGet);
//
//			try {
//				if (res.getStatusLine().getStatusCode() == 200) {
//					org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) res.getEntity();
//					temp = EntityUtils.toString(entity);
//					String con = containerPaser(temp);
//					temp = con;
//				} else {
//					System.out.println("sendMgmt eerr");
//				}
//			} finally {
//				res.close();
//			}
//		} finally {
//			httpclient.close();
//		}
//		return temp;
//	}
//
//	@RequestMapping(value = "/dashboard/Pressure3", method = RequestMethod.POST)
//	@ResponseStatus(value = HttpStatus.OK)
//	public void dashboardPressure3(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {	
//		String content = paser(body);
//		String sensor_nm = "Pressure"; // but Pressure3
//		System.out.println("Pressure3 in : " + content); // cr : /S0sdsasdasdasdasdasdas, sensor_nm, con -> 실시간 데잍.
//		if (content.equals("4")) {
//			System.out.println("contentInstance is Deleted");
//		} else {
//			HttpEntity<String> entity = new HttpEntity<String>(content, headers);
//			this.template.convertAndSend("/topic/subscribe", entity);
//		}
//	}
//	
//	
//	@RequestMapping(value = "/getPressure4", method = RequestMethod.GET)
//	@ResponseStatus(value = HttpStatus.OK)
//	@ResponseBody
//	public String getPressure4() throws Exception {
//		System.out.println("in getPressure4");
//		String device_id = "0004000100010003_12345671";
//		String url = "http://server.norimsu.pe.kr:8080/~/charlot/base/S0004000100010003_12345671/Sound/la";
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//
//		String temp = "no data";
//		try {
//			HttpGet httpGet = new HttpGet(url);
//			httpGet.setHeader("X-M2M-RI", "RQI0001"); //
//			httpGet.setHeader("X-M2M-Origin", "/S" + device_id); //
//			httpGet.setHeader("Accept", "application/json");
//			CloseableHttpResponse res = httpclient.execute(httpGet);
//
//			try {
//				if (res.getStatusLine().getStatusCode() == 200) {
//					org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) res.getEntity();
//					temp = EntityUtils.toString(entity);
//					String con = containerPaser(temp);
//					temp = con;
//				} else {
//					System.out.println("sendMgmt eerr");
//				}
//			} finally {
//				res.close();
//			}
//		} finally {
//			httpclient.close();
//		}
//		return temp;
//	}
//
//	@RequestMapping(value = "/dashboard/Pressure4", method = RequestMethod.POST)
//	@ResponseStatus(value = HttpStatus.OK)
//	public void dashboardPressure4(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {	
//		String content = paser(body);
//		String sensor_nm = "Sound"; //but Pressure4
//		System.out.println("Pressure4 in : " + content); // cr : /S0sdsasdasdasdasdasdas, sensor_nm, con -> 실시간 데잍.
//		if (content.equals("4")) {
//			System.out.println("contentInstance is Deleted");
//		} else {
//			HttpEntity<String> entity = new HttpEntity<String>(content, headers);
//			this.template.convertAndSend("/topic/subscribe", entity);
//		}
//	}
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/sendtoplug", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void sendToplug(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {
		System.out.println("in sendtoplug");
		System.out.println(body);
		if (body.equals("ON")) {
			sendMgmt(url, device_id, "switch", "1", "switch1", "null", dKey);
		} else {
			sendMgmt(url, device_id, "switch", "0", "switch1", "null", dKey);
		}

	}

	/*
	 * 留덇렇�꽕�떛�꽱�꽌�뿉�꽌 媛믪쓣 �꽆寃� 諛쏆쑝硫� �쟾援щ�� �궎�뒗 �븿�닔
	 * 
	 * @RequestMapping(value="/m2m", method=RequestMethod.POST) // �꽌踰꾩뿉�꽌 蹂대궡�삩
	 * �젙蹂대�� 援щ룆�븿.
	 * 
	 * @ResponseStatus(value=HttpStatus.OK) public void notify(@RequestBody
	 * String body, @RequestHeader HttpHeaders headers) throws Exception {
	 * String content = paser(paser(body,"representation"),"content");
	 * System.out.println("sss"); System.out.println("content1 : " + content);
	 * if(content.equals("4")){ System.out.println("contentInstance is Deleted"
	 * ); }else{ String value = new
	 * String(Base64Utils.decodeFromString(content)); System.out.println(
	 * "value : " +value); String light_url =""; String led_url ="";
	 * if(value.equals("1")){ //李쎈Ц OPEN System.out.println("window OPEN");
	 * sendMgmt(url, device_id, "switch1", "ON","switch2","ON", dKey);
	 * HttpEntity<String> entity = new HttpEntity<String>(content, headers);
	 * this.template.convertAndSend("/topic/subscribe2", entity); }else
	 * if(value.equals("0")){ //李쎈Ц Close System.out.println("window CLOSE");
	 * sendMgmt(url, device_id, "switch1", "OFF", "switch2","OFF",dKey);
	 * HttpEntity<String> entity = new HttpEntity<String>(content, headers);
	 * this.template.convertAndSend("/topic/subscribe2", entity); }
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * }
	 */

	/**
	 * 
	 * @param iotPlatformUrl
	 *            : iot플랫폼 주소
	 * @param device_id
	 *            : OID(디바이스아이디)
	 * @param cmdName
	 *            : 명령키 (ex..switch)
	 * @param cmd
	 *            : 명령값 (ex..0 or 1, ON or OFF, on or off ..etc)
	 * @param cmdName1
	 * @param cmd1
	 * @param dKey
	 *            : 디바이스인증키
	 * @throws ParseException
	 * @throws IOException
	 */
	public void sendMgmt(String iotPlatformUrl, String device_id, String cmdName, String cmd, String cmdName1,
			String cmd1, String dKey) throws ParseException, IOException {
		String resourceUrl = iotPlatformUrl + "/controller-" + device_id;
		System.out.println("iotPlatformResourceUrl : " + resourceUrl);
		System.out.println("OID : " + device_id);
		System.out.println("commandName : " + cmdName);
		System.out.println("command : " + cmd);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPut httpPut = new HttpPut(resourceUrl);
			httpPut.setHeader("X-M2M-RI", "RQI0001"); //
			httpPut.setHeader("X-M2M-Origin", "/S" + device_id); //
			httpPut.setHeader("Accept", "application/json");
			httpPut.setHeader("Authorization", "Bearer " + dKey);
			httpPut.setHeader("Content-Type", "application/vnd.onem2m-res+json");
			String body = "{ \"m2m:mgc\": {\"cmt\": 4,\"exra\": { \"any\":[{\"nm\" :\"" + cmdName + "\", \"val\" : \""
					+ cmd + "\"} ]},\"exm\" : 1,\"exe\":true,\"pexinc\":false}}";
			System.out.println(body);
			httpPut.setEntity(new StringEntity(body));

			CloseableHttpResponse res = httpclient.execute(httpPut);

			try {
				if (res.getStatusLine().getStatusCode() == 200) {
					org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) res.getEntity();
					System.out.println(EntityUtils.toString(entity));
				} else {
					System.out.println("sendMgmt eerr");
				}
			} finally {
				res.close();
			}
		} finally {
			httpclient.close();
		}

	}

	/**
	 * 
	 * @param meesagePlatformUrl
	 *            : 메시지플랫폼url
	 * @param send_phone
	 *            : 카카오 메시지를 받을 핸드폰 번호
	 * @param sender_key
	 *            : API 발송 key d6b73318d4927aa80df1022e07fecf06c55b44bf
	 * @param authKey
	 *            : 인증키
	 * @param message
	 *            : 보낼 메시지
	 * @return
	 * @throws Exception
	 */
	public int sendMesageAPI(String meesagePlatformUrl, String send_phone, String authKey, String sender_key,
			String message) throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(meesagePlatformUrl);
			// httpPost.setHeader("Authorization", "Basic
			// Y2xhc3M6bm90X29wZW5fYXBp");
			httpPost.setHeader("Authorization", "Basic " + authKey);
			httpPost.setHeader("Content-Type", "application/json; charset=EUC-KR");
			String body2 = "{ \"msg_id\" : \"iot\", \"dest_phone\" : \"" + send_phone + "\", \"send_phone\" : \""
					+ send_phone + "\", \"sender_key\" : \"" + sender_key + "\", \"msg_body\" : \"" + message
					+ "\", \"ad_flag\" : \"N\" }";

			ByteArrayEntity entity = new ByteArrayEntity(body2.getBytes("UTF-8"));

			System.out.println("TO Kakao BODY Message : " + body2);
			httpPost.setEntity(entity);

			CloseableHttpResponse res = httpclient.execute(httpPost);

			try {
				if (res.getStatusLine().getStatusCode() == 200) {
					org.apache.http.HttpEntity entity2 = (org.apache.http.HttpEntity) res.getEntity();
					System.out.println(EntityUtils.toString(entity2));
				} else {
					System.out.println("eerr");
				}
			} finally {
				res.close();
			}
		} finally {
			httpclient.close();
		}
		return 0;

	}
}

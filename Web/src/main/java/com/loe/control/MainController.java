package com.loe.control;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
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
		}else{
			return "error";
		}
	}
	

	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public Map<String, String> userLogin(@RequestParam Map<String, String> body) throws Exception {
		System.out.println("body: " + body);
		HashMap<String, String> map = new HashMap<String, String>();
		try{
			String id, pw;
			id = body.get("user_id").toString();
			pw = body.get("user_pw").toString();
			System.out.println("Receive Data: " + id + " " + pw);
			
			if(id == null || pw == null){
				map.put("result", "false");
			}
			else{
				map.put("user_id", id);
				map.put("user_pw", pw);
				
				UserInfoVO user = service.userLogin(map);
				
				if(user.getUser_id().equals(id) && user.getUser_pw().equals(pw)){
					System.out.println("userLogin true");
					map.put("result", "true");
				}else{
					System.out.println("userLogin fail");
					map.put("result", "false");
				}
			}
		}catch(Exception e){
			map.put("result", "error");
		}		
        return map;
	}
	
	@RequestMapping(value = "/userJoin", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public Map<String, String> userJoin(@RequestParam Map<String, String> body) throws Exception {
		System.out.println("body: " + body);
		HashMap<String, String> map = new HashMap<String, String>();
		try{
			String id, pw, name, email;
			id = body.get("user_id").toString();
			pw = body.get("user_pw").toString();
			name = body.get("user_name").toString();
			email = body.get("user_email").toString();
			System.out.println("Receive Data: " + id + " " + pw + " " + name + " " + email);
			
			if(id == null || pw == null || name == null || email == null){
				map.put("result", "false");
			}
			else{
				UserInfoVO user = new UserInfoVO();
				user.setUser_id(id);
				user.setUser_pw(pw);
				user.setUser_name(name);
				user.setUser_email(email);
				
				int result = 0;
				result= service.userJoin(user);
				if(result > 0 ){
					System.out.println("userJoin true");
					map.put("result", "true");
				}else{
					System.out.println("userJoin fail");
					map.put("result", "false");
				}
			}
		}catch(Exception e){
			map.put("result", "error");
			System.out.println(e.getMessage());
		}
		return map;
	}
	
	
	
	
	@RequestMapping(value = "/dashboard", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
	public void dashboard(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {
		String content = paser(body);
        System.out.println("Dashboard in : "+ content);
        if(content.equals("4")){
        	System.out.println("contentInstance is Deleted");
        }else{
            HttpEntity<String> entity = new HttpEntity<String>(content, headers);
    		this.template.convertAndSend("/topic/subscribe",entity);
        }
	}
	
	
	@RequestMapping(value="/sendtoplug", method=RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.OK)
	public void sendToplug(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {
		System.out.println("in sendtoplug");  
		System.out.println(body);
		if(body.equals("ON")){
			sendMgmt(url, device_id, "switch", "1", "switch1", "null", dKey);
		}else {
			sendMgmt(url, device_id, "switch", "0", "switch1", "null", dKey);
		}
		
	}	
	
 /* 留덇렇�꽕�떛�꽱�꽌�뿉�꽌 媛믪쓣 �꽆寃� 諛쏆쑝硫� �쟾援щ�� �궎�뒗 �븿�닔
	@RequestMapping(value="/m2m", method=RequestMethod.POST) // �꽌踰꾩뿉�꽌 蹂대궡�삩 �젙蹂대�� 援щ룆�븿.
	@ResponseStatus(value=HttpStatus.OK)
	public void notify(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {
		String content = paser(paser(body,"representation"),"content");
	    System.out.println("sss");
	    System.out.println("content1 : " + content);
	    if(content.equals("4")){
        	System.out.println("contentInstance is Deleted");
	    }else{
	    	 String value = new String(Base64Utils.decodeFromString(content));
		       System.out.println("value : " +value);
		       String light_url ="";
			    String led_url ="";
			    if(value.equals("1")){  //李쎈Ц OPEN
			    	System.out.println("window OPEN");
			    	sendMgmt(url, device_id, "switch1", "ON","switch2","ON", dKey);
					HttpEntity<String> entity = new HttpEntity<String>(content, headers);
					this.template.convertAndSend("/topic/subscribe2", entity);
			    }else if(value.equals("0")){ //李쎈Ц Close
			    	System.out.println("window CLOSE");
			    	sendMgmt(url, device_id, "switch1", "OFF", "switch2","OFF",dKey);
					HttpEntity<String> entity = new HttpEntity<String>(content, headers);
					this.template.convertAndSend("/topic/subscribe2", entity);
	    }
	   
	       
	   
	    	
	    }
	   
	    
	}	
*/

	/**
	 * 
	 * @param iotPlatformUrl : iot플랫폼 주소
	 * @param device_id : OID(디바이스아이디)
	 * @param cmdName : 명령키 (ex..switch)
	 * @param cmd : 명령값 (ex..0 or 1, ON or OFF, on or off ..etc)
	 * @param cmdName1
	 * @param cmd1
	 * @param dKey   : 디바이스인증키
	 * @throws ParseException
	 * @throws IOException
	 */
	public void sendMgmt(String iotPlatformUrl,String device_id, String cmdName, String cmd, String cmdName1, String cmd1, String dKey) throws ParseException, IOException{
	   String resourceUrl = iotPlatformUrl+"/controller-"+device_id;
	   System.out.println("iotPlatformResourceUrl : " + resourceUrl);
	   System.out.println("OID : " + device_id);
	   System.out.println("commandName : " + cmdName);
	   System.out.println("command : " + cmd);
	    CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPut httpPut = new HttpPut(resourceUrl);
			httpPut.setHeader("X-M2M-RI", "RQI0001"); // 
			httpPut.setHeader("X-M2M-Origin", "/S"+device_id); //
			httpPut.setHeader("Accept", "application/json");
			httpPut.setHeader("Authorization","Bearer "+dKey);
			httpPut.setHeader("Content-Type","application/vnd.onem2m-res+json");		
			String body="{ \"m2m:mgc\": {\"cmt\": 4,\"exra\": { \"any\":[{\"nm\" :\""+cmdName+"\", \"val\" : \""+cmd+"\"} ]},\"exm\" : 1,\"exe\":true,\"pexinc\":false}}";
			System.out.println(body);
			httpPut.setEntity(new StringEntity(body));
			
		
			CloseableHttpResponse res = httpclient.execute(httpPut);

			try {
				if (res.getStatusLine().getStatusCode() == 200)
				{
					org.apache.http.HttpEntity entity = (org.apache.http.HttpEntity) res.getEntity();
				    System.out.println(EntityUtils.toString(entity));
				}else{
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
      * @param meesagePlatformUrl : 메시지플랫폼url
      * @param send_phone : 카카오 메시지를 받을 핸드폰 번호
      * @param sender_key : API 발송 key   d6b73318d4927aa80df1022e07fecf06c55b44bf
      * @param authKey : 인증키
      * @param message : 보낼 메시지
      * @return
      * @throws Exception
      */
	public int sendMesageAPI(String meesagePlatformUrl, String send_phone, String authKey, String sender_key, String message)throws Exception{
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(meesagePlatformUrl);
			//httpPost.setHeader("Authorization", "Basic Y2xhc3M6bm90X29wZW5fYXBp");
			httpPost.setHeader("Authorization", "Basic "+authKey);
			httpPost.setHeader("Content-Type", "application/json; charset=EUC-KR");
			String body2 = "{ \"msg_id\" : \"iot\", \"dest_phone\" : \""+send_phone+"\", \"send_phone\" : \""+send_phone+"\", \"sender_key\" : \""+sender_key+"\", \"msg_body\" : \""+message+"\", \"ad_flag\" : \"N\" }";
			
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

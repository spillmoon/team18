package com.loe.control;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

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

	@RequestMapping(value = "/dashboard", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
	public void dashboard(@RequestBody String body, @RequestHeader HttpHeaders headers) throws Exception {
		String content = paser(body);
        System.out.println("Before base 64 : "+ content);
        if(content.equals("4")){
        	System.out.println("contentInstance is Deleted");
        }else{
            HttpEntity<String> entity = new HttpEntity<String>(content, headers);
    		this.template.convertAndSend("/topic/subscribe",entity);
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

	
	public void sendMgmt(String url,String deviceName, String cmdName, String cmd, String cmdName1, String cmd1, String dKey) throws ParseException, IOException{
		 //RP05 -> �쟾援�
	   String desUrl = url+"/controller-"+deviceName;
	   System.out.println("desurl : " + desUrl);
	    CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPut httpPut = new HttpPut(desUrl);
			httpPut.setHeader("X-M2M-RI", "RQI0001"); // 由ы�섏뒪�듃 ID
			httpPut.setHeader("X-M2M-Origin", "/S"+deviceName); // �젣�뼱�옄 �씠由�
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
					System.out.println("eerr");
				}
			} finally {
				res.close();
			}
		} finally {
			httpclient.close();
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


	
}

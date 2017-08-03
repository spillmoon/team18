var firstFloorUrl;
var secondFloorUrl;
var firstFloorSound;
var secondFloorSound = "middle";
var stompClient = null;

var isEmptySeat1 = 0;
var isEmptySeat2 = 0;
var isEmptySeat3 = 0;
var isEmptySeat4 = 0;

function connect() {
	var socket = new SockJS("/dashboard");
	
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/subscribe', function(message){
        	var jsonObject = JSON.parse(message.body);
        	var splitData = jsonObject.body.split("@");
        	
        	var sensor_nm = splitData[0];
        	var data = splitData[1];
        	
        	console.log(sensor_nm);
        	console.log(data);
        	
        	if(sensor_nm =='Temperature'){
        	    $('#temperature').emtpy();
        	    $('#temperature').append(data);
        	}
        	// graph value
           // temperature_value = JSON.parse(JSON.parse(message.body).body).temperature;         
            //humidity_value = JSON.parse(JSON.parse(message.body).body).humidity;
//        	temperature_value = JSON.parse(JSON.parse(message.body).body);
//            showMessage(new Date());
//        	//showMessage("수신 content-type : " + message.headers["content-type"]);
//        	var ct = JSON.parse(message.body).headers["content-type"];
//        	ct ? "" : ct = JSON.parse(message.body).headers["Content-Type"];
//        	showMessage("수신 본문의 content-type : " + ct);
//        	var ot = JSON.parse(message.body).headers["x-m2m-ot"];
//        	ot ? showMessage("수신 본문의 x-m2m-ot : " + ot) : "";
//        	showMessage("수신 본문 내용  : " + JSON.parse(message.body).body);
        });
    }, function(error) {
    	console.log(error);
    	disconnect();
    });	
}

$(document).ready(function(){
	// 지점 정보 가져오기
	$.ajax({
		type : "POST",
		url : "/getStoreList",
		data : {
			"centerY" : "126.876369",
			"centerX" : "37.483424"
		},
		success : function(msg) {
			var store_data = msg[0];
			storeMark(store_data);
		},
		error : function(xhr, status, error) {
			alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
		}
	});
	
	// 초기 온도 받아오기
	$.ajax({
		type : "GET",
		url : "/getTemperature",
		success : function(msg) {
			$("#temperature").text(msg);
		},
		error : function(xhr, status, error) {
		}
	});
	$.ajax({
		type : "GET",
		url : "/getSound",
		success : function(msg) {
			console.log("sound : " + msg);
			if(msg == 0){
				firstFloorSound = "middle";
			}
			else if(msg == 1){
				firstFloorSound = "low";
			}
			
			floorMark();
		},
		error : function(xhr, status, error) {
		}
	});
	// 초기 압력 값 가져오기
	$.ajax({
		type : "GET",
		url : "/getPressure1",
		success : function(msg) {
			console.log("pressure1 : " + msg);
			if(msg < 1000){
				isEmptySeat1 = 1;
				$("#seat3").css("background-image", 'url("/images/squared_chair_o.png")');
			}
			else if(msg >= 1000){
				isEmptySeat1 = 0;
				$("#seat3").css("background-image", 'url("/images/squared_chair_x.png")');
			}
			
			emptySeatMark();
		},
		error : function(xhr, status, error) {
		}
	});
	$.ajax({
		type : "GET",
		url : "/getPressure2",
		success : function(msg) {
			console.log("pressure2 : " + msg);
			if(msg < 1000){
				isEmptySeat2 = 1;
				$("#seat4").css("background-image", 'url("/images/squared_chair_o.png")');
			}
			else if(msg >= 1000){
				isEmptySeat2 = 0;
				$("#seat4").css("background-image", 'url("/images/squared_chair_x.png")');
			}
			
			emptySeatMark();
		},
		error : function(xhr, status, error) {
		}
	});
	$.ajax({
		type : "GET",
		url : "/getPressure3",
		success : function(msg) {
			console.log("pressure3 : " + msg);
			if(msg < 1000){
				isEmptySeat2 = 1;
				$("#seat5").css("background-image", 'url("/images/squared_chair_o.png")');
			}
			else if(msg >= 1000){
				isEmptySeat2 = 0;
				$("#seat5").css("background-image", 'url("/images/squared_chair_x.png")');
			}
		},
		error : function(xhr, status, error) {
		}
	});
	$.ajax({
		type : "GET",
		url : "/getPressure4",
		success : function(msg) {
			console.log("pressure4 : " + msg);
			if(msg < 1000){
				isEmptySeat2 = 1;
				$("#seat6").css("background-image", 'url("/images/squared_chair_o.png")');
			}
			else if(msg >= 1000){
				isEmptySeat2 = 0;
				$("#seat6").css("background-image", 'url("/images/squared_chair_x.png")');
			}
		},
		error : function(xhr, status, error) {
		}
	});

	
	$("#inquery").click(function(){
		window.location.href = "inquery2";
	});
	
	function storeMark(store_data){
		$(".store_name").text(store_data.store_name);
		
		if(store_data.store_congestion == "1"){
			$(".store_congestion").addClass("idle");
			$(".store_congestion").text("한적");
		}
		else if(store_data.store_congestion == "2"){
			$(".store_congestion").addClass("normal");
			$(".store_congestion").text("보통");
		}
		else{
			$(".store_congestion").addClass("busy");
			$(".store_congestion").text("혼잡");
		}
		
		if(firstFloorSound == "low"){
			firstFloorUrl = 'url("/images/background_low.png")';
		}
		else if(firstFloorSound == "middle"){
			firstFloorUrl = 'url("/images/background_middle.png")';
		}
		else if(firstFloorSound == "high"){
			firstFloorUrl = 'url("/images/background_high.png")';
		}
		
		if(secondFloorSound == "low"){
			secondFloorUrl = 'url("/images/background2_low.png")';
		}
		else if(secondFloorSound == "middle"){
			secondFloorUrl = 'url("/images/background2_middle.png")';
		}
		else if(secondFloorSound == "high"){
			secondFloorUrl = 'url("/images/background2_high.png")';
		}
	}
	function floorMark(){
		if(firstFloorSound == "low"){
			firstFloorUrl = 'url("/images/background_low.png")';
		}
		else if(firstFloorSound == "middle"){
			firstFloorUrl = 'url("/images/background_middle.png")';
		}
		else if(firstFloorSound == "high"){
			firstFloorUrl = 'url("/images/background_high.png")';
		}
		
		if(secondFloorSound == "low"){
			secondFloorUrl = 'url("/images/background2_low.png")';
		}
		else if(secondFloorSound == "middle"){
			secondFloorUrl = 'url("/images/background2_middle.png")';
		}
		else if(secondFloorSound == "high"){
			secondFloorUrl = 'url("/images/background2_high.png")';
		}
		
		$(".store_map").css("background-image", firstFloorUrl);
	}
	
	function emptySeatMark(){
		var totalEmptySeat = isEmptySeat1 + isEmptySeat2 + isEmptySeat3 + isEmptySeat4;
		$("#emptySeat").text(totalEmptySeat);
	}
	
	$("#firstFloor").click(function(){
		$("#firstFloor").addClass("disabled");
		$("#secondFloor").removeClass("disabled");
		$(".store_map").css("background-image", firstFloorUrl);
		
		$("#seat1").css("display", 'inherit');
		$("#seat2").css("display", 'inherit');
		$("#seat3").css("display", 'inherit');
		$("#seat4").css("display", 'inherit');
		$("#seat5").css("display", 'inherit');
		$("#seat6").css("display", 'inherit');
		
		$("#seat7").css("display", 'none');
		$("#seat8").css("display", 'none');
		$("#seat9").css("display", 'none');
		$("#seat10").css("display", 'none');
		$("#seat11").css("display", 'none');
		$("#seat12").css("display", 'none');
	});
	
	$("#secondFloor").click(function(){
		$("#secondFloor").addClass("disabled");
		$("#firstFloor").removeClass("disabled");
		$(".store_map").css("background-image", secondFloorUrl);
		
		$("#seat1").css("display", 'none');
		$("#seat2").css("display", 'none');
		$("#seat3").css("display", 'none');
		$("#seat4").css("display", 'none');
		$("#seat5").css("display", 'none');
		$("#seat6").css("display", 'none');
		
		$("#seat7").css("display", 'inherit');
		$("#seat8").css("display", 'inherit');
		$("#seat9").css("display", 'inherit');
		$("#seat10").css("display", 'inherit');
		$("#seat11").css("display", 'inherit');
		$("#seat12").css("display", 'inherit');
	});
	connect();
});


var idx;
var floor;

$(document).ready(function(){
//	var url = location.href.split("?");
//	var data = url[1].split("&");
//	
//	var idxPair = data[0].split("=");
//	var floorPair = data[0].split("=");
//	
//	idx = idxPair[1];
//	floor = floorPair[1];
//	
//	if(idx != 1){
//		alert("죄송합니다. 해당 지점은 서비스 준비 중 입니다.");
//		history.back();
//	}
	
	$("#inquery").click(function(){
		window.location.href = "inquery2";
//		$.ajax({
//			type : "POST",
//			url : "/inqueryRegister",
//			data : {
//				"store_index" : 1,
//				"floor" : 1
//			},
//			success : function(msg) {
//			},
//			error : function(xhr, status, error) {
//			}
//		});
	});
});
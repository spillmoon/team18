var idx;
var floor;

$(function() {
	var url = location.href.split("?");
	var data = url[1].split("&");
	
	var idxPair = data[0].split("=");
	var floorPair = data[0].split("=");
	
	idx = idxPair[1];
	floor = floorPair[1];
	
	if(idx != 1){
		alert("죄송합니다. 해당 지점은 서비스 준비 중 입니다.");
		history.back();
	}
});

var page = {
	init : function() {
		page.initInterface();
	},

	initInterface : function() {
		$("#inquery").click(function(){
			LEMP.Window.open({
				"_sPagePath" : "INQ/html/INQ0002.html",
				"_oMessage":{
					"store_index" : idx
				}
			});
		});
	}
}
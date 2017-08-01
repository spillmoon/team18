var page = {
	init : function() {
		page.initData();
		page.initInterface();
	},

	initData : function() {
		LEMP.Network.requestTr({
			"_sTrcode" : "T18004",
			"_oBody" : {
				"centerY" : "126.876369",
				"centerX" : "37.483424"
			},
			"_fCallback" : function(resT18004) {
				page.renderList(resT18004.body.store_list);
			}
		});
	},

	renderList : function(resBody) {
		var dataes = resBody;
		page.makeMap(dataes);
	},

	makeMap : function(dataes) {
		var myLatLng = {
			lat : 37.483424,
			lng : 126.876369
		};
		var map = new google.maps.Map(document.getElementById('map_canvas'), {
			zoom : 15,
			center : myLatLng
		});

		for (var i = 0, data; data = dataes[i]; i++) {
			page.addMarker(map, data);
		}
	},

	addMarker : function(map, data) {
		var icon = '../../MAP/images/icon_map_angel.png';
		var position = new google.maps.LatLng(data.store_latitude,
				data.store_longitude);
		
		var marker = new google.maps.Marker({
			position : position,
			icon : icon,
			map : map,
			idx : data.store_index
		});

		var contentString = '<div class="window">'
				+ '<div class="window_title">'
				+ data.store_name
				+ '</div>'
				+ '<div class="window_contents">'
				+ '<hr>'
				+ '<div>'
				+ data.store_address
				+ '</div>'
				+ '<div>'
				+ '<div class="col-xs-6">'
				+ '<button class="btn btn-default" type="button" onclick="myFun()" style="font-size:10px;color:rgb(150, 146, 150)">'
				+ data.store_tel
				+ '</button>'
				+ '</div>'
				+ '<div class="col-xs-6">'
				+ '<a href="../html/MAP0003.html?idx=' + data.store_index 
				+ '&floor=1">'
				+ '<button class="btn btn-default" type="button" style="font-size:10px;color:rgb(150, 146, 150)">'
				+ '상세보기' + '</button>' + '</a>' +
		'</div>' + '</div>' + '</div>' + '</div>';

		var infowindow = new google.maps.InfoWindow({
			content : contentString
		});

		marker.addListener('click', function() {
			infowindow.open(map, marker);
		});
	},
	
	initInterface : function(){
		$("#call").click(function(){
			alert("전화버튼");
		});
	}
}

function myFun(){
	alert(1);
}
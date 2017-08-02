function initMap() {
	// Create a map object and specify the DOM element for display.
	var myLatLng = {
		lat : 37.483424,
		lng : 126.876369
	};
	var map = new google.maps.Map(document.getElementById('map_canvas'), {
		zoom : 15,
		center : myLatLng
	});

	var dataes = new Array();

	$.ajax({
		type : "POST",
		url : "/getStoreList",
		data : {
			"centerY" : "126.876369",
			"centerX" : "37.483424"
		},
		success : function(msg) {
			for (var i = 0, data; data = msg[i]; i++) {
				addMarker(map, data);
			}
		},
		error : function(xhr, status, error) {
			alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
		}
	});

	function addMarker(map, data) {
		var icon1 = '/images/icon_map_angel1.png';
		var icon2 = '/images/icon_map_angel2.png';
		var icon3 = '/images/icon_map_angel3.png';

		if (data.store_congestion == "1") {
			var icon = icon1;
		} else if (data.store_congestion == "2") {
			var icon = icon2;
		} else if (data.store_congestion == "3") {
			var icon = icon3;
		}

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
				+ '<hr/>'
				+ '<div>'
				+ data.store_address
				+ '</div>'
				+ '<div>'
				+ '<div class="col-xs-6">'
				+ '<button class="btn btn-default" type="button" style="font-size:10px;color:rgb(150, 146, 150)">'
				+ data.store_tel
				+ '</button>'
				+ '</div>'
				+ '<div class="col-xs-6">'
				+ '<button class="btn btn-default" type="button" onclick="store'
				+ data.store_index
				+ '()" '
				+ 'style="font-size:10px;color:rgb(150, 146, 150)">'
				+ '상세보기'
				+ '</button>' + '</div>' + '</div>' + '</div>' + '</div>';

		var infowindow = new google.maps.InfoWindow({
			content : contentString
		});

		marker.addListener('click', function() {
			infowindow.open(map, marker);
		});
	}
}

function store1() {
	window.location.href = "map3";
}
function store2() {
	alert("죄송합니다. 현재 서비스 준비 중입니다.");
}
function store3() {
	alert("죄송합니다. 현재 서비스 준비 중입니다.");
}
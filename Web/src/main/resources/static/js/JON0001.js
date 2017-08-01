var serviceAgree = false;
var infoAgree = false;

$(document).ready(function(){
	$("#service_agree").click(function() {
		if (!serviceAgree) {
			serviceAgree = true;
			$("#service_agree").css("background-color", "rgb(255, 212, 87)");
			$("#service_agree").css("color", "white");
		} else {
			serviceAgree = false;
			$("#service_agree").css("background-color", "white");
			$("#service_agree").css("color", "black");
		}
	});

	$("#info_agree").click(function() {
		if (!infoAgree) {
			infoAgree = true;
			$("#info_agree").css("background-color", "rgb(255, 212, 87)");
			$("#info_agree").css("color", "white");
		} else {
			infoAgree = false;
			$("#info_agree").css("background-color", "white");
			$("#info_agree").css("color", "black");
		}
	});

	$("#join").click(function() {
		if (serviceAgree && infoAgree) {
			window.location.href = "join2";
		} else {
			alert("약관에 동의해주세요.");
		}
	});

	$("#not_join").click(function() {
		window.location.href = "login";
	});
});
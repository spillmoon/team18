$(document).ready(function() {
	var userId = "xkdl330";

	$.ajax({
		type : "POST",
		url : "/messageList",
		data : {
			"user_id" : userId
		},
		success : function(msg) {
			console.log(msg);
		},
		error : function(xhr, status, error) {
			alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
		}
	});
});
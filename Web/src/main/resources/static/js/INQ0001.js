$(document).ready(function() {
	var userId = "xkdl330";

	$.ajax({
		type : "POST",
		url : "/messageList",
		data : {
			"user_id" : userId
		},
		success : function(msg) {
			for (var i = 0, data; data = msg[i]; i++) {
				addInquery(data);
			}
		},
		error : function(xhr, status, error) {
			alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
		}
	});
	
	function addInquery(data){
		$("#inquery_contents").append(
				'<div id="inquery">'
				+ '<div id="inquery_title">'
				+ '<span id="store_name">'
				+ data.store_name
				+ '</span>'
				+ '<span id="reg_date">'
				+ data.reg_date
				+ '</span>'
				+ '</div>'
				+ '<div id="inquery_message">'
				+ '<p id="message">'
				+ data.message
				+ '</p>'
				+ '</div>'
				+ '</div>');
	}
});
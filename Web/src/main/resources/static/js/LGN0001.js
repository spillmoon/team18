$(document).ready(function(){
	$(".bt_login").click(function(){
		var userId = $("#user_id").val().trim();
		var userPwd = $("#user_pw").val().trim();

		$.ajax({
			type : "POST",
			url : "/userLogin",
			data : {
				"user_id" : userId,
				"user_pw" : userPwd
			},
			success : function(msg) {
				window.location.href = "main";
			},
			error : function(xhr, status, error) {
				alert("아이디 혹은 비밀번호가 잘못되었습니다.");
			}
		});
	});
});
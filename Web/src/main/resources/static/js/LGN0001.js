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
				if(msg.result == "true"){
					window.location.href = "main";
				}
				else if(msg.result == "false"){
					alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
				}
				else{
					alert("아이디 혹은 패스워드가 다릅니다.");
				}
			},
			error : function(xhr, status, error) {
				alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
			}
		});
	});
});
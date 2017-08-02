$(document).ready(function() {
	// 완료 버튼 클릭 시
	$("#join").click(function() {
		var userId = $("#id").val().trim();
		var userPw = $("#pw").val().trim();
		var userPwCheck = $("#pw_check").val().trim();
		var userEmail = $("#email").val().trim();
		var userName = $("#name").val().trim();

		if (userId == "") {
			alert("아이디를 입력해주세요.");
		} else if (userPw == "") {
			alert("비밀번호를 입력해주세요.");
		} else if (userPwCheck == "") {
			alert("비밀번호 확인을 입력해주세요.");
		} else if (userEmail == "") {
			alert("이메일을 입력해주세요.");
		} else if (userName == "") {
			alert("이름을 입력해주세요.");
		} else {
			if (userPw != userPwCheck) {
				alert("입력하신 비밀번호가 다릅니다. 비밀번호를 확인해주세요.");
			} else {
				$.ajax({
					type : "POST",
					url : "userJoin",
					data : {
						"user_name" : userName,
						"user_email" : userEmail,
						"user_pw" : userPw,
						"user_id" : userId
					},
					success : function(msg) {	
						if(msg.result == "true"){
							alert("회원가입에 성공하였습니다.");
							window.location.href = "login";
						}
						else if(msg.result == "false"){
							alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
						}
						else{
							alert("중복된 아이디가 존재합니다. 다른 아이디를 사용해주세요.");
						}
					},
					error : function(xhr, status, error) {
						alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
					}
				});
			}
		}
	});

	// 취소 버튼 클릭 시
	$("#not_join").click(function() {
		window.location.href = "login";
	});
});
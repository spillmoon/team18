var page = {
	init : function() {
		page.initInterface();
	},

	initInterface : function() {
		// 회원가입 버튼 클릭 시
		$("#join").click(function() {
			var userId = $("#id").val().trim();
			var userPw = $("#pw").val().trim();
			var userPwCheck = $("#pw_check").val().trim();
			var userEmail = $("#email").val().trim();
			var userName = $("#name").val().trim();
			
			if(userId == ""){
				alert("아이디를 입력해주세요.");
			}
			else if(userPw == ""){
				alert("비밀번호를 입력해주세요.");
			}
			else if(userPwCheck == ""){
				alert("비밀번호 확인을 입력해주세요.");
			}
			else if(userEmail == ""){
				alert("이메일을 입력해주세요.");
			}
			else if(userName == ""){
				alert("이름을 입력해주세요.");
			}
			else{
				if(userPw != userPwCheck){
					alert("입력하신 비밀번호가 다릅니다. 비밀번호를 확인해주세요.");
				}
				else{
					LEMP.Network.requestTr({
						"_sTrcode" : "T18001",
						"_oBody" : {
							"user_name" : userName, 	 
							"user_email" : userEmail, 	 
							"user_pw" : userPw,	 	 
							"user_id" : userId
						},
						"_fCallback" : function(resT18001) {
							if (resT18001.header.result == true) {
								// 다음페이지 이동 --> LEMP Window API(open)
								LEMP.Window.open({
									"_sPagePath" : "LGN/html/LGN0001.html"
								});
							} else {
								// alert --> LEMP Window API(alert)
								LEMP.Window.alert({
									"_vMessage" : "중복된 아이디가 존재합니다. 다른 아이디를 입력해주세요."
								});
							}
						}
					});
				}
			}
		});
		
		// 취소 버튼 클릭 시
		$("#not_join").click(function() {
			LEMP.Window.open({
				"_sPagePath" : "LGN/html/LGN0001.html"
			});
		});
	}
}
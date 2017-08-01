var page = {
	init : function() {
		page.initInterface();
	},

	initInterface : function() {
		// 로그인 버튼 클릭 시
		// 1) ID, PWD read
		// 2) DM00001 --> Network API
		$(".bt_login").click(function() {
			var userId = $("#user_id").val().trim();
			var userPwd = $("#user_pw").val().trim();

			LEMP.Network.requestLogin({
				"_sUserId" : userId,
				"_sPassword" : userPwd,
				"_sTrcode" : "T18002",
				"_oBody" : {
					"user_id" : userId,
					"user_pw" : userPwd
				},
				"_fCallback" : function(resT18002) {
					if (resT18002.header.result == true) {
						// 다음페이지 이동 --> LEMP Window API(open)
						LEMP.Window.open({
							"_sPagePath" : "MAN/html/MAN0001.html"
						});
					} else {
						// alert --> LEMP Window API(alert)
						LEMP.Window.alert({
							"_vMessage" : "아이디나 패스워드를 확인해주세요."
						});
					}
				}
			});
		});
	}
}
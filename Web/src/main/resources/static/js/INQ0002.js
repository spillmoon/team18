$(document).ready(function(){
	// 등록 버튼 클릭 시
	$("#register").click(function(){
		var storeIndex = 1;
		var userId = "xkdl330";
		var message = $("#message").val().trim();

		$.ajax({
			type : "POST",
			url : "/writeMessage",
			data : {
				"store_index" : storeIndex,
				"user_id" : userId,
				"message" : message
			},
			success : function(msg) {
				if(msg.result == "true"){
					alert("문의 내용이 관리자에게 전달하였습니다.");
					history.back();
				}
				else{
					alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
				}
			},
			error : function(xhr, status, error) {
				alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
			}
		});
	});
	
	// 취소 버튼 클릭 시
	$("#not_register").click(function(){
		history.back();
	});
});
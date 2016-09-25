
function showNewEmail()
{
	$('#subtitle').css("display","none");
	$('#username').css("display","none");
	$('#email').css("display","none");
	$('#change_email').css("display","none");
	$('#change_password').css("display","none");
	$('#message_wrapper').css("display","none");
	$('#newEmail_wrapper').css("display","block");
}

function hideNewEmail(){
	$('#subtitle').css("display","block");
	$('#username').css("display","block");
	$('#email').css("display","block");
	$('#change_email').css("display","block");
	$('#change_password').css("display","block");
	$('#message_wrapper').css("display","block");
	$('#newEmail_wrapper').css("display","none");
}

function showNewPassword()
{
	$('#subtitle').css("display","none");
	$('#username').css("display","none");
	$('#email').css("display","none");
	$('#change_email').css("display","none");
	$('#change_password').css("display","none");
	$('#message_wrapper').css("display","none");
	$('#newPassword_wrapper').css("display","block");
}

function hideNewPassword(){
	$('#subtitle').css("display","block");
	$('#username').css("display","block");
	$('#email').css("display","block");
	$('#change_email').css("display","block");
	$('#change_password').css("display","block");
	$('#message_wrapper').css("display","block");
	$('#newPassword_wrapper').css("display","none");
}
function changePassword() {
	if (confirm("Wollen Sie Ihr Passwort wirklich ändern ?") ) {
		var newPassword = $('#newPassword').val();
		$.ajax({
			type: "POST",
			contentType : 'text/plain; charset=utf-8',
			dataType : 'text',
			url: "/TwitterMonitor/changePassword",
			data: newPassword, 
			success :function (result) {}
		});
		$('#newPassword').val("");
		hideNewPassword();
	}
}

function changeEmail() {
	if (confirm("Wollen Sie Ihre Email-Adresse wirklich ändern") && ($('#newEmail').val() == $('#newEmail2').val())) {
		var newEmail = $('#newEmail').val();
		$.ajax({
			type: "POST",
			contentType : 'text/plain; charset=utf-8',
			dataType : 'text',
			url: "/TwitterMonitor/changeEmail",
			data: newEmail, 
			success :function (result) {
				window.location = "/TwitterMonitor/settings"
			}
		});
		$('#newEmail').val("");
		hideNewEmail();
	}
}

function enableNotifications() {
	request = "/TwitterMonitor/enableNotifications";
	window.location = request;
}

function deleteAccount() {
	if (confirm("Wollen Sie Ihren Account wirklich löschen")) {
		request = "/TwitterMonitor/deleteUser";
		window.location = request;
	}
}
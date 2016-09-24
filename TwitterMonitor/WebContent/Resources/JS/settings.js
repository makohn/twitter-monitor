function changePassword() {
	if (confirm("Wollen Sie Ihr Passwort wirklich ändern ?")) {
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
	}
}

function changeEmail() {
	if (confirm("Wollen Sie Ihre Email-Adresse wirklich ändern")) {
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
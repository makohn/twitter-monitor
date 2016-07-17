$(document).ready(function() {
	//set login_panel

});

function newUser() {

	$('#animation').css('display', 'none');
	$('#animation2').css('display', 'none');

	$('#createAccount').css('display', 'block');
	$('#createAccount').addClass('animated fadeIn');
}

function evalPasswd() {

	var val = document.getElementById('new_passwd').value;

	if (val.length > 8) {

		if (val.match(/\d{1,}/) && val.match(/[a-zA-ZäöüÄÖÜ]{1,}/)
				&& val.match(/\W/)) {
			$('#passwd_line').css('background-color', 'green');
			$('#passwd_line').css('width', '100%');
		}

		else if (val.match(/\d{1,}/) && val.match(/[a-zA-ZäöüÄÖÜ]{1,}/)
				|| val.match(/\W/) && val.match(/[a-zA-ZäöüÄÖÜ]{1,}/)) {
			$('#passwd_line').css('background-color', 'lightgreen');
			$('#passwd_line').css('width', '75%');
		} else {
			$('#passwd_line').css('background-color', 'yellow');
			$('#passwd_line').css('width', '50%');
		}
	} else {
		$('#passwd_line').css('background-color', 'red');
		$('#passwd_line').css('width', '25%');
	}
	
	if(val == "") {
		$('#passwd_line').css('background-color', 'white');
	}
};

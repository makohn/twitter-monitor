var count_slide_menu = 0;

function slide_menu() {

	if (count_slide_menu % 2 != 0) {

		$('#button_4').css('display', 'none');
		$('#button_5').css('display', 'none');
		$('#button_6').css('display', 'none');
		$('#change_sidebar_west_button').css('display', 'none');
		setTimeout(function() {
			$('#button_1').css('display', 'block');
			$('#button_1').addClass('animated slideInLeft');
		}, 400);
		setTimeout(function() {
			$('#button_2').css('display', 'block');
			$('#button_2').addClass('animated slideInLeft');
		}, 500);
		setTimeout(function() {
			$('#button_3').css('display', 'block');
			$('#button_3').addClass('animated slideInLeft');
		}, 600);
		setTimeout(function() {
			$('#change_sidebar_west_button').css('display', 'block');
			$('#change_sidebar_west_button').addClass('animated zoomIn');
			$('#change_sidebar_west_button').css(' background-image',
					'url(changeContextBirdLeft.png)');

		}, 700);
	} else {
		$('#change_sidebar_west_button').css('display', 'none');
		$('#button_1').css('display', 'none');
		$('#button_2').css('display', 'none');
		$('#button_3').css('display', 'none');

		setTimeout(function() {
			$('#button_4').css('display', 'block');
			$('#button_4').addClass('animated slideInLeft');
		}, 300);
		setTimeout(function() {
			$('#button_5').css('display', 'block');
			$('#button_5').addClass('animated slideInLeft');
		}, 400);
		setTimeout(function() {
			$('#button_6').css('display', 'block');
			$('#button_6').addClass('animated slideInLeft');
		}, 500);

		setTimeout(function() {
			$('#change_sidebar_west_button').css('display', 'block');
			$('#change_sidebar_west_button').addClass('animated zoomIn');
			$('#change_sidebar_west_button').css(' background-image',
					'url(changeContextBirdRight.png)');
		}, 700);

	}

	count_slide_menu++;
}

count_side_menu_north = 0;

function side_menu_north() {
	if (count_side_menu_north % 2 == 0) {
		$('#side_bar_north').css('display', 'block');

		$('#side_bar_north').addClass('animated fadeInDown');
		$('#searchfield').css('display', 'block');
		$('#sidebar').css('margin-top', '0px');
	} else {

		$('#side_bar_north').css('display', 'none');
		$('#searchfield').css('display', 'none');
		$('#sidebar').css('margin-top', '50px');
	}
	count_side_menu_north++;
}

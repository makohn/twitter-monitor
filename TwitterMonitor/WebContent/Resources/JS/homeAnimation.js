//Einleitung der Home-Animation

$(document).ready(function(){
    setTimeout(function(){
    $('#text_comic').addClass('animated fadeOut');
    
     },2300);
    
    setTimeout(function(){
    document.getElementById("animation").setAttribute('id','animation2');
     },3900);
    
     setTimeout(function(){
			$('#text_comic').css('margin-top','170px');
			$('#text_comic').css('margin-left','700px');
			$('#text_comic').removeClass('animated fadeOut');
			$('#text_comic').addClass('animated fadeIn');
    },4300);
    
    setTimeout(function(){
		    $('#runningMail_pic').addClass('animated zoomInLeft')
		    $('#runningMail_pic').css('display','block');
		    $('#bird_pic').addClass('animated fadeInLeft');
		    $('#bird_pic').css('display','block');
    },3900);
    
    
})

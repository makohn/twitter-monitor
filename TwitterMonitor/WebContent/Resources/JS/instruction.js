var clicked = []

function display(div)
{
	if(contains(clicked,div))
	{
		$('.slide:eq('+div+')').css('display','none');
		clicked.splice(getIndexOf(clicked,div), 1);
	}else
	{
	$('.slide:eq('+div+')').css('display','block');
	clicked.push(div);
	}
}

function contains(array,value) {
    var i = array.length;
    while (i--) {
       if (array[i] === value) {
           return true;
       }
    }
    return false;
}

function getIndexOf(array,value){
	var i = array.length;
    while (i--) {
        if (array[i] == value) {
        	
            return i;
        }else
        	{
        		return -1;
        	}
        
}
}
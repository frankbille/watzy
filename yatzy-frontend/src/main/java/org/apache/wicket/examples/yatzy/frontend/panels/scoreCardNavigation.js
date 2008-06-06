function prev(elm) {
	var e = $('#'+elm);
	var a = e.find('a');

	var s = a.filter('.selected');
	
	if (s.length == 1) {
		var i = a.index(s[0]);
		
		console.log(i);
		if (i > 0) {
			s.removeClass('selected');
			a.filter(':eq('+(i-1)+')').addClass('selected');
		}
	} else {
		e.find('.combination a:first').addClass('selected');
	}
}

function next(elm) {
	var e = $('#'+elm);
	var a = e.find('a');
		
	var s = a.filter('.selected');
	
	if (s.length == 1) {
		var i = a.index(s[0]);
		
		console.log(i);
		if (i < a.length-1) {
			s.removeClass('selected');
			a.filter(':eq('+(i+1)+')').addClass('selected');
		}
	} else {
		e.find('.combination a:first').addClass('selected');
	}
}

function select(elm) {
	$('#'+elm).find('a.selected').click();
}

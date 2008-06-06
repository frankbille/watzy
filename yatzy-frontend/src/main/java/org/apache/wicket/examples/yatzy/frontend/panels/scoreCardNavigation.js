function prev(elm) {
	var e = $('#'+elm);
	var a = e.find('a');

	var s = a.filter('.selected');
	
	if (s.length == 1) {
		var i = a.index(s[0]);
		
		if (i > 0) {
			s.removeClass('selected');
			var ne = a.filter(':eq('+(i-1)+')');
			ne.addClass('selected');
			
			// Ensure that it is visible
			st = $(window).scrollTop();
			et = ne.offset().top;
			
			if (et < st) {
				$.scrollTo('-='+(st - et));
			}
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
		
		if (i < a.length-1) {
			s.removeClass('selected');
			var ne = a.filter(':eq('+(i+1)+')');
			ne.addClass('selected');
			
			// Ensure that it is visible
			lowestvisibley = $(window).height() + $(window).scrollTop();
			bottomelm = ne.offset().top+ne.height();
			
			if (lowestvisibley < bottomelm) {
				$.scrollTo('+='+(bottomelm-lowestvisibley+3));
			}
		}
	} else {
		e.find('.combination a:first').addClass('selected');
	}
}

function select(elm) {
	$('#'+elm).find('a.selected').click();
}

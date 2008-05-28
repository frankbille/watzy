if (typeof(ExpandableContent) == "undefined")
	ExpandableContent = { };

ExpandableContent.initialize = function () {
	$('.expandableMenu').width('100%');
	$('.expandableMenu').bind('mouseleave', function() {
		$(this).find('.content').slideUp().removeClass('displayed');
	});
}

ExpandableContent.displayContent = function (content) {
	if ($('.expandableMenu .content').hasClass('displayed')) {
		$('.expandableMenu .content').hide();
		$('#' + content).show();
	} else {
		$('#' + content).slideDown().addClass('displayed');
	}
}

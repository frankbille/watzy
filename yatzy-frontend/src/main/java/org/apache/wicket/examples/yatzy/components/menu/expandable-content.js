if (typeof(ExpandableContent) == "undefined")
	ExpandableContent = { };

ExpandableContent.displayContent = function (content) {
	if ($('.expandableMenu .content').hasClass('displayed')) {
		$('.expandableMenu .content').hide();
		$('#' + content).show();
	} else {
		$('#' + content).slideDown().addClass('displayed');
	}
}

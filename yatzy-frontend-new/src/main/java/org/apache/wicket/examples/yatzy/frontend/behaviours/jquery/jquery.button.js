$(document).ready(function(){
	$('a.btn').each(function(){
		var b = $(this);
		var tt = b.html() || b.val();
		b.text('').css({cursor:'pointer'}).append(tt);
	});
	
	$('input:submit,button[@type=button],button[@type=submit]').each(function(){
		var b = $(this);
		var tt = b.text() || b.val();
		$this = this ;
		
		if ( this.onclick )
		{
			b = $('').insertAfter(this).addClass(this.className).attr('id',this.id) ;
			b.get(0).onclick = this.onclick ;
		} else {
			b = $('').insertAfter(this).addClass(this.className).attr('id',this.id).click(function(){
				$(this).parents('form').eq(0).submit();
			});
		}
		if ( $this.click ) b.click = $this.click ;
		$(this).remove();
		b.text('').css({cursor:'pointer'}).append(tt);
	});
	
	$('input:reset,button[@type=reset]').each(function(){
		var b = $(this);
		var tt = b.text() || b.val();
		$this = this ;
		b = $('').insertAfter(this).addClass(this.className).attr('id',this.id).click(function(){$(this).parents('form').eq(0).reset();});
		$(this).remove();
		b.text('').css({cursor:'pointer'}).append(tt);
	});
});

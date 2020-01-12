
(function ($) {
    "use strict";

	$('.message a').click(function(){
	   $('form').animate({height: "toggle", opacity: "toggle"}, "slow");
	});

})(jQuery);
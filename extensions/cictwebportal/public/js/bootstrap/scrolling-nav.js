
$(document).ready(function() {
$(window).scroll(function() {
    if(($(".navbar").offset().top < 50)&&($(window).width() > 870)){
        $("#navbar_main").removeClass('navbar-darker');
        $("#navbar_main").addClass("navbar-gray");
        $("#logo_main").width('310px');
        $("#logo_main").height('100px');
    }else if(($(".navbar").offset().top < 50)&&($(window).width() < 870)){
        $("#navbar_main").addClass("cc");
        $("#navbar_main").removeClass('navbar-darker');
        $("#navbar_main").addClass("navbar-gray");
        $("#logo_main").width('150px');
        $("#logo_main").height('50px');
    }else{
        $("#navbar_main").addClass('navbar-darker');
        $("#navbar_main").removeClass("navbar-gray");
        $("#icon").addClass('bg-black');
        $("#logo_main").width('150px');
        $("#logo_main").height('50px');
    }
});
$(window).resize(function() {
  resize_navbar();
});
});

function resize_navbar(){
  if($(window).width() < 870){
    $("#logo_main").width('150px');
    $("#logo_main").height('50px');
  }else{
    $("#logo_main").width('310px');
    $("#logo_main").height('100px');
  }
}

//jQuery for page scrolling feature - requires jQuery Easing plugin
$(function() {
   $(document).on('click', 'a.page-scroll', function(event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: $($anchor.attr('href')).offset().top - $('.navbar').height()
        }, 1500, 'easeInOutExpo');
        event.preventDefault();
    });

    $("#login-form").click(function() {
      $('html, body').animate({
          scrollTop: $("#login-div").offset().top - $('.navbar').height()
    }, 2000);
    });
});

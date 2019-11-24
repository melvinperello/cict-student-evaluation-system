<script type = "text/javascript">
$(document).ready(function() {
  load_nav();
});

function load_nav(){
  $("#container-nav").load("{{ asset('html/navbar/nav.php') }}",function(){
    $("#lnk_home").attr('href',"{{ route('home') }}");
    $("#lnk_search").attr('href',"{{ route('show-teacher-finder') }}");
    $("#lnk_app").attr('href',"{{ route('get-app') }}");
    $("#lnk_register").attr('href',"{{ route('register') }}");
    $("#lnk_login").attr('href',"{{ route('show-login') }}");
    $("#logo").attr('src', '{{ asset("img/logo/navnav.png") }}');
    announcements();
  });
}

function announcements(){
  $("#container-announcement").load("{{ asset('html/announcements/announcement.php') }}", function(){
    post_request("{{ route('get-more-anno') }}",onRequestAnnouncementSuccess);
  });
}

function onRequestAnnouncementSuccess(data){
  if(data == ""){
    show_desc("#card_announcements", "{{ asset( 'img/icons/megaphone.png' ) }}", "As of now, there are no posted announcements yet.<hr>");
  }else {
    $.each(data, function(key, value) {
      faculty_name = (value['faculty'] != null) ? ("<i class='fa fa-user-circle-o'></i> "+value['faculty']['last_name']+", "+value['faculty']['first_name'] +" "+value['faculty']['middle_name']) : ("");
      $("#card_announcements").append('<div class="card text-left wow fadeInUp animated anno-cards s-light"><div class="card-body anno-cards-body"><h4 class="card-title bold">'+value['all']['title']+'</h4><div class="collapse" id="'+key+'"><p class="card-text">'+value['all']['message']+'</p></div><p class="card-text"><small class="">'+faculty_name+'</small><a data-toggle="collapse" href="#'+key+'" class="btn btn-black-bordered float-right btn-sm">Read More</a></p></div><div class="card-footer div-black-top-bordered"><small class="">' +value['date_time']+'</small></div></div><br/>')
    });
    load_footer();
  }
}

function load_footer(){
  $( "#container-footer" ).load("{{ asset( 'html/home/footer.php' ) }}", function(){
  });
}

</script>

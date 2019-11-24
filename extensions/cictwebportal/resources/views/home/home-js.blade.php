<script type = "text/javascript">
$(document).ready(function() {
  load_nav();
});

function load_nav(){
  $("#container-nav").load("{{ asset('html/navbar/home_nav.php') }}",function(){
    resize_navbar();
    $("#lnk_register").attr('href',"{{ route('register') }}");
    $("#lnk_login").attr('href',"{{ route('show-login') }}");
    $("#logo_main").attr('src', '{{ asset("img/logo/navnav.png") }}');
    load_hello();
    load_eval_steps();
    announcements();
    faculty_sched();
    load_student_app();
    load_free_browsing();
    load_footer();
  });
}

function load_hello(){
  $( "#container-hello" ).load("{{ asset( 'html/home/hello.php' ) }}", function(){
    $(".first-slide").attr('src', "{{ asset('img/carousel/cict2.jpg') }}");
    $(".second-slide").attr('src', "{{ asset('img/carousel/cict3.jpg') }}");
    $(".third-slide").attr('src', "{{ asset('img/carousel/cict5.PNG') }}");
    $(".fourth-slide").attr('src', "{{ asset('img/carousel/1.jpg') }}");
  });
}

function announcements(){
  $("#container-announcement").load("{{ asset('html/home/announcement.php') }}", function(){
    post_request("{{ route('get-all-anno') }}", onRequestAnnouncementSuccess);
  });
}

function onRequestAnnouncementSuccess(data){
  if(data.length == 0){
    $("#card_announcements").html("");
    show_desc("#card_announcements", "{{ asset( 'img/icons/megaphone.png' ) }}", "As of now, there are no posted announcements yet.<hr>");
  }else {
    $.each(data, function(key, value) {
      faculty_name = (value['faculty'] != null) ? ("<i class='fa fa-user-circle-o'></i> "+value['faculty']['last_name']+", "+value['faculty']['first_name'] +" "+value['faculty']['middle_name']) : ("");
      $("#card_announcements").append('<div class="card text-left wow fadeInUp animated anno-cards s-light" style="border:1px solid #EEE"><div class="card-body anno-cards-body"><h4 class="card-title bold">'+ value['all']['title']+'</h4><div class="collapse" id="'+key+'"><p class="card-text">'+value['all']['message']+'</p></div><p class="card-text"><small class="">'+faculty_name+'</small><a data-toggle="collapse" href="#'+key+'" class="btn btn-black-bordered float-right btn-sm">Read More</a></p></div><div class="card-footer div-black-top-bordered"><small class="">' +value['date_time']+'</small></div></div><br>')
    });
    if(data.length == 5){
      $("#card_announcements").append("<a class='btn btn-black-bordered float-right fadeInUp animated' href='' id='btn_view_more_anno'>View More ></a>");
      $("#btn_view_more_anno").attr('href', "{{ route('show-more-anno') }}");
    }
  }
}

function faculty_sched(){
  $("#container-faculty-sched").load("{{ asset('html/home/faculty_sched.php') }}", function(){
    $("#btn_teacher_finder").attr('href', "{{ route('show-teacher-finder') }}");
  });
}

function load_eval_steps(){
  $( "#container-steps-in-eval" ).load("{{ asset( 'html/home/steps_in_eval.php' ) }}", function(){
    $("#wifi").attr('src', '{{ asset("img/steps/wifi.png") }}');
    $("#student").attr('src', '{{ asset("img/steps/student.png") }}');
    $("#linked").attr('src', '{{ asset("img/steps/linked_acc.png") }}');
    $("#bell").attr('src', '{{ asset("img/steps/bell.png") }}');
  });
}

function load_student_app(){
  $( "#container-student-app" ).load("{{ asset( 'html/home/student_app.php' ) }}", function(){
    $("#btn_dl_app").attr('href',"{{ route('get-app') }}");
    var android_pic = setInterval(function() {
      $("#android-pic").attr('src', "{{ asset('img/login.png') }}");
      android_pic = setTimeout(function() {
        $("#android-pic").attr('src', "{{ asset('img/loggedin.png') }}");
      },2000);
    },3000);
  });
}

function load_free_browsing(){
  $("#container-offline-webs").load("{{ asset('html/home/free_browsing.php') }}",function(){
    $("#w3s_link").attr('href',"http://10.10.10.10/offline/w3schools/W3School/www.w3schools.com/index.html");
    $("#tp_link").attr('href', "http://10.10.10.10/offline/tutorialspoint/Tutorialspoint/www.Tutorialspoint.com/index.html");
    $("#geeks_link").attr('href', "http://10.10.10.10/offline/geeksforgeeks/geeksforgeeks.org/www.geeksforgeeks.org/index.html");
    $("#programiz_link").attr('href', "http://10.10.10.10/offline/programiz2017/www.programiz.com/index.html");
    $("#w3s").attr('src', "{{ asset('/img/offline/wsh.png') }}");
    $("#tp").attr('src', "{{ asset('/img/offline/tp.png') }}");
    $("#geeks").attr('src', "{{ asset('/img/offline/geek.png') }}");
    $("#programiz").attr('src', "{{ asset('/img/offline/programiz.png') }}");
    // $("#java").attr('src', "{{ asset('/img/offline/java.png') }}");
  });
}

function load_footer(){
  $( "#container-footer" ).load("{{ asset( 'html/home/footer.php' ) }}", function(){
  });
}
</script>

<script type = "text/javascript">
//------------------------------------ON DOCUMENT READY---------------------------------------
//--------------------------------------------------------------------------------------------
$(document).ready(function() {
  load_nav();
});

function load_nav(){
  $("#container-nav").load("{{ asset('html/navbar/profile_nav.php') }}",function(){
    $(".loader").attr('style', 'display:none');
    load_container();
  });
}

function load_container(){
  $("#container").load("{{ asset('html/profile/student_profile_catch.php') }}",function(){

  });
}

</script>

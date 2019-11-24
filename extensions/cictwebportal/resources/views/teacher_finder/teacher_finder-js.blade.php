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
    teacher_finder();
  });
}

var searched_name = "";
function teacher_finder(){
  $("#container-teacher-finder").load("{{ asset('html/teacher_finder/teacher_finder.php') }}", function(){
    $("#bulsu_logo").attr('src', "{{ asset( 'img/logo/BULSU_LOGO.png' ) }}");
    $("#cict_logo").attr('src', "{{ asset( 'img/logo/CICT_LOGO.png' ) }}");
    show_desc("#search_desc", "{{ asset( 'img/icons/search.png' ) }}", "Looking for your instructor? Search for his/her name and click on suggested names. e.g. Samson");
    post_request("{{ route('get-faculty-name') }}", onRequestFacultySuccess);
  });
}

function onRequestFacultySuccess(data){
  if(data == "No data"){
    $("#txt_faculty_name").val("No faculty record found");
  }else{
    var faculty_name = [];
    $.each(data, function(key, value) {
      var each = { label : value['last_name']+", "+value['first_name']+" "+value['middle_name'], id : value['id'] };
      faculty_name.push(each);
    });

    $("#txt_faculty_name").autocomplete({
      source: faculty_name,
      select: function (event, ui) {
        $("#txt_faculty_name").val(ui.item.label); // display the selected name
        $("#txt_faculty_id").val(ui.item.id); // save selected id to hidden input
        return false;
      }
    });

    $("#btn_search_faculty").click(function(event) {
      var id = $("#txt_faculty_id").val();
      var searched_name = $("#txt_faculty_name").val();
      request_faculty_sched(id);
      $("#txt_faculty_id").val("");
    });
  }
}

var routee = "{{ route('get-faculty-sched','') }}/"
function request_faculty_sched(id){
  var validated_id = (id == "") ? ("no id") : (id);
  request = new Request();
  request.url = routee + validated_id;
  request.type = 'POST';
  request.replyType = 'json';
  // start
  request.begin = function(){

  }
  // success
  request.done = function(data, textStatus, xhr){
    onRequestFacultySchedSuccess(data/*$.parseJSON(data)*/);
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    //alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //alert("JQUERY TEXT STATUS: " + textStatus);
    //alert("ERROR DESCRIPTION: " + errorThrown);
    window.location = error_route + xhr.status;
  }
  // finished
  request.always = function(){
    // this will be called always whether fail or done at the end of this request
  }
  // send
  request.send(); // start the ajax request
}

function onRequestFacultySchedSuccess(data){
  $(".scheds").html(""); $("#tbl_sched").find('> tbody > tr').removeAttr('style');
  remove_prev("#faculty_sched_table","#search_desc");
  if(data[0]['result'] == "No faculty matched"){
    show_desc("#search_desc", "{{ asset( 'img/icons/fox.png' ) }}", "Aww! Sorry! No results matched your search. Faculty name does not exists.");
  }else if(data[0]['result'] == "No load_group"){
    show_desc("#search_desc", "{{ asset( 'img/icons/calendar.png' ) }}", "Faculty schedule not yet posted. How about try searching for another one?");
  }else{
    $.each(data, function(key,value) {
      subject = value['subject'];
      schedule = value['load_group_schedule'];
      load_group = value['load_group'];
      if(value['load_group_schedule'] !=null ){
        for (var i = 0; i < schedule.length; i++) {
          var faculty_name = "";
          $("#"+schedule[i]['class_day']).after("<tr class='text-center scheds'> <td>"+subject[0]['code']+"</td><td>"+convert(schedule[i]['class_start'])+"</td> <td>"+convert(schedule[i]['class_end'])+"</td> <td>"+schedule[i]['class_room']+"</td><tr/>");
          $("#"+schedule[i]['class_day']).attr('style', 'background-color:#EEEEEE');
        }
      }else {
      }
    });
    remove_prev("#search_desc","#faculty_sched_table");
  }
}

</script>

<script type = "text/javascript">
var get_photo = "{{ route('get-photo','') }}/";
//------------------------------------ON DOCUMENT READY---------------------------------------
//--------------------------------------------------------------------------------------------
$(document).ready(function() {
  load_nav();
  student_profile();
  request_profile_values(load_profile_values);
  // load_footer();
});

function load_nav(){
  $("#container-nav").load("{{ asset('html/navbar/profile_nav.php') }}",function(){
    $(".loader").attr('style', 'display:none');
    $("#logo").attr('src', "{{ asset('img/logo/huhu.png') }}");
    load_side_nav();
  });
}

$(function () {
  $('[data-toggle="tooltip"]').tooltip();
})

//--------------------------------REUSABLE GET PROFILE REQUEST--------------------------------
//--------------------------------------------------------------------------------------------
function request_profile_values(onDone){
  request = new Request();
  request.url = "{{ route('profile-get') }}";
  request.type = 'POST';
  request.replyType = 'json';
  // start
  request.begin = function(){
  }
  // success
  request.done = function(data, textStatus, xhr){
    onDone(data);
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    //  alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //  alert("JQUERY TEXT STATUS: " + textStatus);
    //  alert("ERROR DESCRIPTION: " + errorThrown);
    //  window.location = error_route + xhr.status;
  }
  // finished
  request.always = function(){
    // this will be called always whether fail or done at the end of this request
  }
  // send
  request.send(); // start the ajax request
}


//--------------------------------REUSABLE GET REQUESTS---------------------------------------
//--------------------------------------------------------------------------------------------
function post_request(route, goto_func, post_parameters, btn_id){
  request = new Request();
  request.url = route;
  request.type = 'POST';
  request.data = post_parameters;
  request.replyType = 'json';
  // start
  request.begin = function(){
    btn_clicked_start(btn_id);
  }
  // success
  request.done = function(data, textStatus, xhr){
    // alert(JSON.stringify(post_parameters));
    // alert(JSON.stringify(data));
    btn_clicked_end(btn_id);
    goto_func(data/*$.parseJSON(data)*/);
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    //alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //alert("JQUERY TEXT STATUS: " + textStatus);
    //alert("ERROR DESCRIPTION: " + errorThrown);
    window.location = error_route + xhr.status;
    btn_clicked_end(btn_id);
  }
  // finished
  request.always = function(){
    // this will be called always whether fail or done at the end of this request
    btn_clicked_end(btn_id);
  }
  // send
  request.send(); // start the ajax request
}


//--------------------------------REUSABLE GET REQUESTS---------------------------------------
//--------------------------------------------------------------------------------------------
function get_request(route, goto_func){
  // settings
  request = new Request();
  request.url = route;
  request.type = 'GET';
  request.replyType = 'json';
  // start
  request.begin = function(){
  }
  // success
  request.done = function(data, textStatus, xhr){
    //alert(data);
    goto_func(data);
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    //  alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //  alert("JQUERY TEXT STATUS: " + textStatus);
    //  alert("ERROR DESCRIPTION: " + errorThrown);
    //window.location = error_route + xhr.status;
  }
  // finished
  request.always = function(){
    // this will be called always whether fail or done at the end of this request
  }
  // send
  request.send(); // start the ajax request
}

//--------------------------------- CHECK IF STUDENT HAS_PROFILE = 1--------------------------
//--------------------------------------------------------------------------------------------
function click_tab(id,goto_func,loader){
  $(id).click(function(event) {
    clearInterval(clear_linked);
    //alert(goto_func);
    loader('#container');
    goto_func();
  });
}

function load_profile_values(data){
  info = data['info'];
  $("#btn-logout, #btn-logout_sub").click(function(event) {
    clearInterval(clear_linked);
    logout_request();
  });

  if(info['has_profile']=="1"){
    $("#btn_profile, #btn_profile_sub, #btn_history, #btn_history_sub, #btn_sched, #btn_sched_sub,#btn_grade, #btn_grade_sub").unbind('click');
    $("#btn_summary, #btn_summary_sub, #btn_linked, #btn_linked_sub, btn_settings, #btn_settings_sub, #btn_help, #btn_help_sub").unbind('click');
    click_tab("#btn_profile, #btn_profile_sub",student_profile,loader);
    click_tab("#btn_history, #btn_history_sub",student_history,loader);
    click_tab("#btn_sched, #btn_sched_sub",student_sched,loader);
    click_tab("#btn_grade, #btn_grade_sub",student_grade,loader);
    click_tab("#btn_summary, #btn_summary_sub",student_summary,loader);
    click_tab("#btn_linked, #btn_linked_sub",student_linked_account,loader);
    click_tab("#btn_settings, #btn_settings_sub",student_settings,loader);
    click_tab("#btn_help, #btn_help_sub",student_help,loader);
  }else{
    $("#btn_profile, #btn_profile_sub").click(function(event) {
      loader('#container');
      student_profile('#container');
      $("#divMessageBox").remove();
    });
    $("#btn_history, #btn_sched, #btn_grade, #btn_summary, #btn_linked, #btn_settings, #btn_help").click(function(event) {
      clear_notify();
    });
    $("#btn_history_sub, #btn_sched_sub, #btn_grade_sub, #btn_summary_sub, #btn_linked_sub, #btn_settings_sub, #btn_help_sub").click(function(event) {
      clear_notify();
    });
  }

  $("#nav_collapse_sm li").click(function(event) {
    $(".navbar-toggle").click();
  });
}

function clear_notify(){
  notify('Hi There! New here right?','Please complete your student profile to proceed.');
}


//---------------------------------------LOAD NAVBAR-----------------------------------------
//--------------------------------------------------------------------------------------------
function load_side_nav(){
  $("#sidebar").load("{{ asset('html/navbar/profile_side_nav.php') }}",function(){
    $("#linked_logo").attr('src',"{{ asset('img/icons/linked.png') }}");
    $("#sidebar").addClass('div-white-shadow');
    //$("#monosync_logo").attr('src',"{{ asset('img/logo/monosync_logo.jpg') }}");
    request_profile_values(side_nav_load_values);
    //side nav options
  });
}

//-----------------------------------------LOAD FOOTER----------------------------------------
//--------------------------------------------------------------------------------------------
function load_footer(){
  $("#footer-nav").load("{{ asset('html/footer/profile_footer.php') }}",function(){
    $("#footer-nav").hide().fadeIn("slow");
    // $("#monosync_logo_footer").attr('src', "{{ asset('img/logo/monosync_logo.jpg') }}");
  });
}

//-----------------------------------------LOAD HELP----------------------------------------
//--------------------------------------------------------------------------------------------
function student_help(){
  $("#container").load("{{ asset('html/profile/student_help.php') }}",function(){
    $(".loader").attr('style', 'display:none');
  });
}

//--------------------------------------LOAD NAVABAR VALUES------------------------------------------------
//---------------------------------------------------------------------------------------------------------
var newImg2 = document.createElement("img");
function side_nav_load_values(data){
  info = data['info'];
  $("#side-bar-name").html(info['first_name'] +" "+info['last_name']);
  $("#side-bar-id").html("STUDENT ID: " + info['id']);
  profile = data['profile'];
  load_display_picture("#profile-pic_img");
}

function load_display_picture(id){
  $(id).attr('src',"{{ asset('img/load-2.gif') }}");
  var _img = $(id);
  newImg2.id = "profile-pic_img";
  newImg2.src = get_photo+profile['profile_picture'];
  newImg2.className = 'img-fluid rounded-circle text-center';
  newImg2.style.border="1px solid #EEEEEE";
  newImg2.width="120";
  newImg2.height="120";
  newImg2.onload=onload_display_picture('profile-pic_img');
}

function onload_display_picture(id){
  // if(newImg2.src.includes("NONE")){
  //   newImg2.src = "{{ asset('img/icons/img.png') }}";
  // }else{
  //   var _img = $(id);
  //   _img.src = newImg2.src;
  // }
  // //  $("#"+id).replaceWith(newImg2);
  // setTimeout(function() {
  //   $("#"+id).replaceWith(newImg2);
  // },1000) ;
  var k = newImg2.src;
   if(k.indexOf("NONE") === -1){
    var _img = $(id);
   _img.src = newImg2.src;
   }else{
     newImg2.src = "{{ asset('img/icons/img.png') }}";
   }
  setTimeout(function() {
    $("#"+id).replaceWith(newImg2);
  },1000);
  $("#btn_change_pic").prop('disabled', false);
}

//---------------------------------------------------------------------------------------------------------
var newImg3 = document.createElement("img");
function load_values(data){
  if(data['student']== "No student"){
    notify("Database Request Done","No student record found.");
  }else if(data['student_profile']== "No student profile"){
    post_request("{{ route('profile-update') }}", onUp, "","");
  }
  else{
    info = data['info'];
    (data['curriculum'] != "no curriculum") ? ($("#curriculum").val(data['curriculum']['name'])) : ($("#curriculum").val("---"));
    $("#first_name").val(info['first_name']);
    $("#last_name").val(info['last_name']);
    $("#middle_name").val(info['middle_name']);
    $("#stud-id").val(info['id']);
    if((info['year_level']!=null) && (info['section']!=null) && (info['_group']!=null)){
      $("#year-sec-gr").val(info['year_level']+info['section']+"-G"+info['_group']);
    }else if(info['year_level']!=null){
      $("#year-sec-gr").val(info['year_level']);
    }else {
      $("#year-sec-gr").val("---");
    }
    if(info['gender'] == ""){
    }else if(info['gender']!= null){
      $("#"+info['gender']+"").attr("checked", "checked");
      $("."+info['gender']+"").addClass('active');
    }

    profile = data['profile'];
    update_profile_picture("#display-pic");
    $("#contact_no").val(data['profile']['mobile']);
    $("#email").val(profile['email']);
    $("#zipcode").val(profile['zipcode']);
    $("#student_address").val(profile['student_address']);
    $("#ice_name").val(profile['ice_name']);
    $("#ice_contact").val(profile['ice_contact']);
    $("#ice_address").val(profile['ice_address']);
    if(profile['floor_assignment'] == 'NULL'){
      $("#floor-assignment-lbl").val('---');
    }else{
      if(profile['floor_assignment'] == '3'){
        var floor = "3RD";
      }else{
        var floor = "4TH";
      }
      $("#floor-assignment-lbl").val( floor+ " FLOOR");
    }
  }
}

function onUp(){
  alert("fail");
}

function update_profile_picture(id){
  $(id).attr('src',"{{ asset('img/load-2.gif') }}");
  $("#btn_change_pic").prop('disabled', true);
  newImg3.id = "display-pic";
  newImg3.src = get_photo+profile['profile_picture'];
  newImg3.className = 'img-fluid img-thumbnail';
  newImg3.width="150";
  newImg3.height="150";
  newImg3.onload = onload_profile_photo('display-pic');
}

function onload_profile_photo(id){
  //$("newImg3.src:contains('NONE')").attr('src', "{{ asset('img/icons/img.png') }}");
  var k = newImg3.src;
   if(k.indexOf("NONE") === -1){
    var _img = $(id);
   _img.src = newImg3.src;
   }else{
     newImg3.src = "{{ asset('img/icons/img.png') }}";
   }
  setTimeout(function() {
    $("#"+id).replaceWith(newImg3);
  },1000);
  $("#btn_change_pic").prop('disabled', false);
}
//---------------------------------------------------------------------------------------------------------

//-------------------------------------------LOG OUT------------------------------------------------------
//---------------------------------------------------------------------------------------------------------
function logout_request(){
  $.post("{{ route('profile-logout') }}")
  .done(function(data){
    window.location.href = "{{ route('home') }}";
  })
  .fail(function(data){
  });
}
//---------------------------------------------------------------------------------------------------------


//-----------------------------------------STUDENT PROFILE-------------------------------------------------
//---------------------------------------------------------------------------------------------------------
function student_profile(){
  $( "#container" ).load("{{ asset( 'html/profile/student_profile.php' ) }}", function(){
    $( "#container" ).hide().fadeIn("slow");
    load_upload();
    request_profile_values(load_values);
    $("#preview").attr('src',"{{ asset('img/no_image.png') }}");
    $("#btn_view_pdf").attr('href',"{{ route('pdf-view') }}");

    /* frm_student_profile validation rules on saving a profile */
    $("#frm_student_profile").validate({
      rules: {
        gender:{
          required: true
        },
        zipcode: {
          digits: true,
          minlength: 2,
          maxlength: 10
        },
        student_address: {
          required: true,
          minlength: 2,
          maxlength: 300
        },
        email: {
          required: true,
          email: true,
          minlength: 5,
          maxlength: 40
        },
        contact_no: {
          required: true,
          digits: true,
          minlength: 11,
          maxlength: 11
        },
        ice_name: {
          required: true,
          letterswithbasicpunc: true,
          minlength: 2,
          maxlength: 60
        },
        ice_address: {
          required: true,
          minlength: 2,
          maxlength: 100
        },
        ice_contact: {
          required: true,
          digits: true,
          minlength: 11,
          maxlength: 11
        },
      },
      messages: {
        house_no: {
          pattern: 'Please enter numbers with hypens only'
        }
      },
      errorPlacement: function(error, element) {
        if (element.attr("name") == "gender") {
          error.insertAfter("#radio_gender");
        } else {
          error.insertAfter(element);
        }
      },
    }); //end of validate *///
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------

    $( "#btn_save" ).click(function(){
      var isValid = $("#frm_student_profile").valid(); // check if form is valid
      if(isValid == true){
        var form_values =  $("#frm_student_profile").serializeObject(); // serialize the form
        post_request("{{ route('profile-update') }}", onUpdateProfileSuccess, form_values,"#btn_save");
      }else{
        notify("Oops. You missed something.","Please complete all fields to save changes to your profile.");
      }
    }); //end of btn click
    onKeyPress('#ice_address','#btn_save');
  });// on_load end methods
}

function onUpdateProfileSuccess(data){
  show_buttons('#btn_save');
  if(data['result'] == "saved"){
    show_notif('#alert-me',"Saved Changes");
    request_profile_values(load_profile_values);
    request_profile_values(load_values);
  }else{
    show_notif('#alert-me',"Notification: Failed to save to database. Please check all text fields.");
  }
}
//---------------------------------------------------------------------------------------------------------


//---------------------------------------------------------------------------------------------------------
//-------------------------------------STUDENT SETTINGS----------------------------------------------------
function student_settings(){
  $( "#container" ).load("{{ asset( 'html/profile/student_settings.php' ) }}", function(){
    $( "#container" ).hide().fadeIn("slow");
    $("#frm_change_flr_ass").validate({
      rules: {
        floor_assignment: {
          required: true
        },
      },
    }); //end of validate

    $("#btn_save_flr").click(function(){
      var isValid = $("#frm_change_flr_ass").valid();
      if(isValid == true){
        var form_values = $("#frm_change_flr_ass").serializeObject();
        post_request("{{ route('settings-change-flr') }}", onUpdateFlrSuccess, form_values,"#btn_save_flr");
      }else{
      }
    }); //end of btn click
    onKeyPress('#floor_assignment','#btn_save_flr');

    show_hide_password("#span_old_pass","#icon_old_pass", "old_password");
    show_hide_password("#span_view_pass","#icon_pass", "new_password");
    show_hide_password("#span_confirm_pass","#icon_confirm_pass", "confirm_new_password");

    $("#frm_change_password").validate({
      rules: {
        old_password: {
          required: true,
          minlength: 6,
          maxlength: 50
        },
        new_password: {
          required: true,
          minlength: 6,
          maxlength: 50
        },
        confirm_new_password: {
          required: true,
          minlength: 6,
          maxlength: 50,
          equalTo: "#new_password"
        },
      },
      messages: {
        confirm_new_password: {
          equalTo: "Please enter your correct new password again.",
        }
      },
      errorPlacement: function(error, element) {
        if (element.attr("name") == "old_password") {
          error.insertAfter(".old_pass");
        } else if (element.attr("name") == "new_password") {
          error.insertAfter(".pass");
        } else if (element.attr("name") == "confirm_new_password") {
          error.insertAfter(".confirm_pass");
        } else {
          error.insertAfter(element);
        }
      },
    }); //end of validate

    $("#btn_save").click(function(){
      var isValid = $("#frm_change_password").valid();
      if(isValid == true){
        var form_values = $("#frm_change_password").serializeObject();
        post_request("{{ route('settings-pass-reset') }}", onUpdatePassSuccess, form_values,"#btn_save");
      }else{
      }
    }); //end of btn click
    onKeyPress('#confirm_new_password','#btn_save');
  });
}


function onUpdateFlrSuccess(data){
  if(data['result']=="saved"){
    show_notif('.alert-flr',"Cluster successfully changed");
  }else{
    notify("Database Connection Failed","Failed to save changes to database. Please refresh and try again.");
  }
}

function onUpdatePassSuccess(data){
  if(data['result']=="saved"){
    show_notif('.alert-pass',"Password successfully changed");
  }else if(data['result']=="wrong_pass"){
    show_notif('#old-password-error-1',"Old password incorrect");
    hide_result('#old_password','#old-password-error-1');
  }else{
    notify("Database Connection Failed","Failed to save new password to database. Please refresh and try again.");
  }
}
//---------------------------------------------------------------------------------------------------------

//---------------------------------------------------------------------------------------------------------
//-------------------------------------STUDENT SUMMARY-------------------------------------------------------
function student_summary(){
  get_request("{{ route('summary-get') }}", load_stud_summary);
}

function clicked(id){
  //  $("#"+id+"").append("<span class='glyphicon glyphicon-remove-circle pull-right'></span>");
  var div = id.replace('term','div');
  $("#"+div+"").toggle("slideDown");
}

function load_stud_summary(data){
  // alert(data);
  $("#container").html('<div class="row white-bg div-white-shadow fadeIn animated" id="div_summary" style="padding: 3%;"></div>');
    //  alert(data[0]);
  if(data['result'] == 'curriculum_not_set'){ //checks if curriculum is empty
    //if curriculum not set
    show_desc("#div_summary", "{{ asset('img/icons/curriculum.png') }}", "Curriculum not available. Please complete all procedures to view your curriculum with your grades.");
  }else if(data['result'] == 'no student record'){
    notify("Database Request Done","No student record found. Please refresh and try again.");
  }else{
  $("#div_summary").append('<div class="container col-lg-12"><h3 class="mont">Academic Evaluation</h3><hr></div>');
  $.each(data,function(key,value){
      var a=0;
      for(var i=0; i<=1; i++){  // 0,1 sem = 1st Sem | 2nd Sem
        if(value[i][0]['result'] == "curriculum subjects empty"){
          //alert("Database Request Done. Curriculum subjects empty. Please wait and try again.");
        }else{
          //--------------------------------------change to words ------------------------------------------------------------//
          var year_word = change_to_words(value[i][0]['cur']['year']); var sem_word = change_to_words(value[i][0]['cur']['semester']);
          //-------------------------------append title, div and table ---------------------------------------------------------//
          var year_sem = value[i][0]['cur']['year']+"_"+value[i][0]['cur']['semester']+"_"+key;
          $("#div_summary").append("<div class='container col-lg-12 summary_heading' onclick='clicked(this.id)' id=term_"+year_sem+">"+year_word+" Year "+sem_word+" Semester"+"<hr></div>");
          $("#div_summary").append("<div class='container col-lg-12'><div class='table-responsive' id=div_"+year_sem+" style='display:none'></div></div>"); //div creation
          $("#div_"+year_sem).append('<table id=table_'+year_sem+' class="table table-bordered"></tbody></table>');
          //--------------------------------------table headings -------------------------------------------------------------//
          $("#table_"+year_sem).append("<tbody><tr style='background-color: #DCDEE1;'> <th>Course Code</th><th>Descriptive Title</th><th>Units</th><th>Final</th><th>Credits</th><th>Remarks</th></tr>");
          var total_units = 0.0; var total_credits = 0.0;
          //-------------------------loop as how the number of subjects per sem ------------------------a----------------------//
          for (var j = 0; j < value[a].length; j++) {
            //added lec and lab units of each subject
            var units =  0.0; var credits = 0.0;
            units = value[i][j]['subject']['lec_units'] + value[i][j]['subject']['lab_units'];
            //if index error
            if(typeof value[i][j]['cur']['id'] === 'undefined'){ $("#div_grade").append("<tr> <td> index error </td>"); }
            //correct
            else{
              //grade record exists
              if(value[i][j]['grade'] != null){
                credits = value[i][j]['grade']['credit'];
                $("#table_"+value[i][0]['cur']['year']+"_"+value[i][0]['cur']['semester']).append("<tr> <td>"+value[i][j]['subject']['code']+"</td>"+ "<td>"+value[i][j]['subject']['descriptive_title']+"</td><td>"+units+"</td><td>"+value[i][j]['grade']['rating']+"</td>"+"<td>"+value[i][j]['grade']['credit']+"</td>"+"<td>"+value[i][j]['grade']['remarks']+"</td></tr>");
                total_credits += credits;
                if(value[i][j]['grade']['remarks'] == "FAILED"){
                  $("#table_"+value[i][0]['cur']['year']+"_"+value[i][0]['cur']['semester']+" tr:last").attr('style', 'color: red');
                  total_credits -= credits;
                }
                //no grade record //UNPOSTED GRADE
              }else{
                $("#table_"+year_sem).append("<tr> <td>"+value[i][j]['subject']['code']+"</td>"+ "<td>"+value[i][j]['subject']['descriptive_title']+"</td><td>"+units+"</td><td></td><td>0</td><td></td></tr>");
              }
            }//else
            total_units += units;
          }//end of for loop sem
          $("#table_"+year_sem).append("<tr style='background-color: #EEEEEE'> <td> "+value[a].length+" Subject(s)</td><td></td><td>"+total_units+"</td><td></td><td>"+total_credits+"</td><td></td></tr>");
        }
        a++;
      }//end of for loop 2 sem
    });// each
  }
}//end of function
//--------------------------------------------------------------------------------------------------------


//---------------------------------------------------------------------------------------------------------
//-------------------------------------STUDENT GRADE-------------------------------------------------------
function student_grade(){
  $( "#container" ).load("{{ asset( 'html/profile/student_grade.php' ) }}", function(){
    get_request("{{ route('grade-get') }}", load_table_grades);
  });
}

var grade_route = "{{ route('pdf-view-grade','') }}/"
function load_table_grades(data){
  $("#acad_term_list").html(""); $("#acad_term_cmb_menu").html(""); $("#div_grade").html("");//#clear containers
  $("#row_grade").removeClass('hide');
  $("#acad_term_list").append('<a class="list-group-item list-group-item-action list-group-item-secondary">Academic Year and Term</a>')
  if(data.length != 0){
    $.each(data, function(key, value) {
      $("#div_main_grade").removeAttr('style');
      var eval = value['eval']; var acad = value['acad']; var flag=true;
      var acad_term_yr = replace(acad['school_year']+"_"+acad['semester']," ","_"); //#impo:jquery can't select with an id containing spaces
      acad_term_yr = acad_term_yr+"_"+key;
      $("#acad_term_list").append('<a id='+acad_term_yr+' href="#" onclick="unhide(this.id)" class="list-group-item list-group-item-action point">'+acad['school_year']+" "+acad['semester']+'</a>');
      $("#acad_term_cmb_menu").append('<li><a id='+acad_term_yr+' href="#" class="point" onclick="unhide(this.id)">'+acad['school_year']+" "+acad['semester']+'</a></li>');

      if(key == 0){
        $("#div_grade").append('<div class="div_list '+acad_term_yr+'"><div class="table-responsive"><table id=tbl_'+acad_term_yr+' class="table table-striped table-bordered"></table></div></div>');
      }else{
        $("#div_grade").append('<div class="div_list '+acad_term_yr+'" hidden><div class="table-responsive"><table id=tbl_'+acad_term_yr+' class="table table-striped table-bordered"></table></div></div>');
      }
      $("#tbl_"+acad_term_yr).append("<tbody><tr> <th>Code</th><th>Descriptive Title</th><th>Grade</th><th>Units</th><th>Remarks</th> </tr>");

      var gwa = 0.0; var subj_count = 0; var units_passed = 0; var units = 0;
      if(value[0][0]['result'] == "No grade record of student"){
        $("#tbl_"+acad_term_yr).append("<tr><td colspan='5' class='text-center'>No grade record</td></tr>");
        flag=false;
      }else{
        for (var i = 0; i < value[0].length; i++) {
          $("#tbl_"+acad_term_yr).append("<tr><td>"+value[0][i]['subject']['code']+"</td><td>"+value[0][i]['subject']['descriptive_title']+"</td><td>"+value[0][i]['grade']['rating']+"</td><td>"+value[0][i]['grade']['credit']+"</td><td>"+value[0][i]['grade']['remarks']+"</td></tr>");
          if(value[0][i]['grade']['remarks'] == "FAILED"){
            $("#tbl_"+acad_term_yr+" tr:last").attr('style', 'color: red');
          }
          if(isNaN(value[0][i]['grade']['rating'])) {
            var gwa = 0.0; var subj_count = 0; var units_passed = 0; var units = 0;//#uncomment-if there are UNPOSTED/FAILED grades total computations won't be computed
            flag=false;
          }else if(value[0][i]['grade']['remarks'] == "FAILED"){}
          else{
            if(flag){
              subj_count += 1; units_passed += parseFloat(value[0][i]['grade']['credit']);
              gwa += parseFloat(value[0][i]['grade']['rating']);
            }
          } if(flag){units += parseFloat(value[0][i]['grade']['credit']);}
        }
      }//closing of else
      if(flag){gwa = gwa/subj_count; gwa = gwa.toFixed(4);}
      $("."+acad_term_yr).append("<div  style='border:1px solid #EEEEEE'><table class='table'><tr> <td></td><td>GWA : "+gwa+"<br/><sub>Gen. Weighted Average</sub></td><td>UNITS : "+units+"<br/><sub>Unit Enrolled</sub></th><td>EARNED: "+units_passed+"<br/><sub>Unit(s) Earned</sub></td></table><div class='text-right' style='padding:3%'><a href='"+grade_route + acad['school_year']+"_"+acad['semester']+"' target='_blank' id='btn_"+acad['school_year']+"_"+acad['semester']+"' class='btn btn btn-black-bordered'>Print PDF</a></div></div>");
      $("."+acad_term_yr).append("<div style='border:1px solid #EEEEEE; padding: 2%'><p><span class='glyphicon glyphicon-exclamation-sign'></span> WARNING!</p><p>Falsifiction, alteration or tampering of the report generated by the system is considered major and criminal offense, hence, they are punishable by expulsion or dismissal!</p></div>");

    });
  }else {
    show_desc("#grade_result", "{{ asset('img/icons/grades.png') }}", "Grades unavailable. <hr/> No Accepted evaluation record of the student is found. ");
  }
}

function replace(word, original, delimiter){
  var new_word = word.replace(original ,delimiter);
  return new_word;
}

function unhide(id){
  $("#div_grade .div_list").attr('hidden', '');
  $("."+id).removeAttr('hidden');
}

//---------------------------------------------------------------------------------------------------------
function load_upload(){
  var filename;
  $("#modal").dialog({
    autoOpen: false,
    modal: true,
    resizable: false,
    width: 300,
    dialogClass: 'modal_class fadeIn animated',
    buttons: [
      {
        text: 'Cancel',
        style: "background-color:#CC6E2D; color: white",
        click: function() {
          $(this).dialog("close");
        }
      },
    ],
  });

  $( "#btn_change_pic" ).click(function() {
    $('#modal').dialog('open');
  });

  $("#image-file").change(function(event) {
    $("#image-upload").prop('disabled', false);
    $("#upload_message").html("");
    var file = this.files[0];
    var imagefile = file.type;
    var match= ["image/jpeg","image/png","image/jpg"];
    if(!((imagefile==match[0]) || (imagefile==match[1]) || (imagefile==match[2])))
    {
      $("#image-upload").prop('disabled', true);
      $("#upload_message").html("<i class='fa fa-file-image-o'></span> <span class='fadeIn animated s-light'>Please select a valid image file type. Only jpeg, jpg and png image file type are allowed.</span><hr>");
      return false;
    }else{
      var reader = new FileReader();
      reader.onload = imageIsLoaded;
      reader.readAsDataURL(this.files[0]);
    }
  });

  function imageIsLoaded(e) {
    filename = e.target.result;
    $('#preview').attr('src', e.target.result);
    $('#preview').attr('width', '250px');
    $('#preview').attr('height', '250px');
  };

  $("#image-upload").click(function(event) {
    start_ajax();
    return false;
  });
}// end of function

function start_ajax(){
  request = new Request();
  request.processData = false;
  request.contentType = false;
  request.url = "{{ route('upload-photo') }}";
  request.type = 'POST';
  request.data = new FormData($("#form_upload")[0]),
  request.replyType = 'json';
  // start
  request.begin = function(){
    btn_clicked_start("#image-upload");
  }
  // success
  request.done = function(data, textStatus, xhr){
    //alert(JSON.stringify(data));
    if(data['result'] == "file_not_recieved"){
      $("#upload_message").html("<i class='fa fa-file-image-o'></i> <span class='fadeIn animated'>File not found! Please try again.</span><hr>");
    }else if(data['result'] == "image_rectangle"){
      $("#upload_message").html("<i class='fa fa-file-image-o'></i> <span class='fadeIn animated'>Image invalid. Please try not to upload a landscape photo.</span><hr>");
    }else if (data['result'] == "file_not_valid"){
      $("#upload_message").html("<i class='fa fa-file-image-o'></i> <span class='fadeIn animated'>File too large. The maximum file size allowed is 512 KB only.</span><hr>");
    }else{
      reload_display_photo();
      window.location.reload();
    }
    btn_clicked_end("#image-upload");
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    //alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //alert("JQUERY TEXT STATUS: " + textStatus);
    //alert("ERROR DESCRIPTION: " + errorThrown);
    //windows.location = status_error ++
  }
  // finished
  request.always = function(){
    // this will be called always whether fail or done at the end of this request
    btn_clicked_end("#image-upload");
    return false;
  }
  // send
  request.send(); // start the ajax request
}

function reload_display_photo(){
  $("#modal").dialog("close");
  request_profile_values(side_nav_load_values);
  request_profile_values(load_values);

  // load_display_picture_sub('profile-pic_sub');
  // load_display_picture('profile-pic_img');
  // update_profile_picture('display-pic')
}

//---------------------------------------------------------------------------------------------------------
//-------------------------------STUDENT LINKED ACCOUNT----------------------------------------------------

var clear_linked;

var qr_code;
var qr_id;
var qr_name;
var qr_cur;
var qr_gender;

function student_linked_account(){
  //  $("#container").load("{{ asset('html/profile/student_linked_account.php') }}", function(){
  //    $( "#container" ).hide().fadeIn("slow");
  request_profile_values(save_qr_data);
  clear_linked = setInterval(function() {
    get_request("{{ route('check-number', ['cict_id'=>session('SES_CICT_ID')]) }}", load_linked_acc_info);
  },1000);
  //  });
}

function save_qr_data(data){
  if(data['student'] == "No student"){
    notify("Database Request Done","No student record found in database");
  }else{
    if(data['student_profile'] == "No student profile"){
      notify("Database Request Done","No student profile record found in database");
    }else {
      info = data['info'];
      (data['curriculum'] != "no curriculum") ? (qr_cur = data['curriculum']['description']) : (qr_cur = "---");
      term = data['current_term'];
      qr_code = info['id']+'_'+info['cict_id']+'_'+term['id'];
      qr_id = info['id'];
      var middle_name = (info['middle_name'] != null) ? (info['middle_name']) : ("");
      qr_name = info['first_name']+" "+middle_name+" "+info['last_name'];
      (info['gender'] != null) ? (qr_gender = info['gender']) : (qr_gender = "---");
    }
  }
}

function load_qrcode(data){
  $("#div_linked").append('<span id="font-md">Reference Code</span><br/><img id="qrcode" style = "height: 200px; width: 200px; border: 1px solid #EEEEEE" src=""><br>');
  $("#qrcode").attr('src',"qrclient/qrclient.php?content=LINKED_NONE_"+qr_code);
  $("#div_linked").append("<p>STUDENT ID: "+qr_id+"</p>");
  $("#div_linked").append("<p>NAME: "+qr_name+"</p><hr/>");
  $("#div_linked").append("<div id='div_linked_sub' class='col-lg-8 col-lg-offset-2 text-left'></div><hr/>");
  $("#div_linked_sub").append("<p><i class='fa fa-list-alt'></i> CURRICULUM: "+ qr_cur+"</p>");
  $("#div_linked_sub").append("<p><i class='fa fa-venus-mars'></i> GENDER: "+ qr_gender+"</p>");
  $("#div_linked_sub").append("<p><i class='fa fa-phone-square'></i> &nbsp;IMEI: None</p>");
}

function load_linked_acc_info(data){
  $("#container").html('');
  $("#container").html('<div class="row text-center div-white-shadow" style="padding: 3%;"><div class="col-lg-6 col-md-8 mx-auto" id="div_linked" style="font-weight: bold; font-size: 9pt;"></div></div>');
  pila_info = data['pila_info'];
  linked_settings = data['settings'];
  if(data['pila_status'] == "yes"){
    $("#div_linked").append('<h5>Currently Serving:</h5>');
    $("#div_linked").append('<h2>'+data['called']+'<h2>');
    $("#div_linked").append("<div id='div_linked_sub' class='text-left col-lg-8 mx-auto'></div>");
    $("#div_linked_sub").append('<br/><hr/><h6><i class="fa fa-file-archive-o"></i> Reference Details</h6>');
    $("#div_linked_sub").append("<p>&nbsp;&nbsp;<i class='fa fa-address-book-o'></i> Reference No. #"+pila_info['floor_number']+"</p>");
    $("#div_linked_sub").append("<p>&nbsp;&nbsp;<i class='fa fa-building-o'></i> Room assignment "+pila_info['floor_assignment']+"</p>"); //linked_settings['floor_'+pila_info['floor_assignment']+'_name']
    $("#div_linked_sub").append("<p>&nbsp;&nbsp;<i class='fa fa-buysellads'></i> Accepted "+ new Date(pila_info['request_accepted']).toLocaleString() +"</p>");
    var var_request_called;
    if(pila_info['request_called'] == null){ var_request_called="UNCALLED"; }else{ var_request_called = pila_info['request_called']}
    $("#div_linked_sub").append("<p>&nbsp;&nbsp;<i class='fa fa-bell-o'></i> Called "+var_request_called+"</p>");
    if(pila_info['called_by'] == null){ var_called_by="--"; }else{ var_called_by = pila_info['called_by']}
    $("#div_linked_sub").append("<p>&nbsp;&nbsp;<i class='fa fa-user-circle'></i> Evaluator "+var_called_by+"</p>");
    if(pila_info['called_on_terminal'] == null){ var_called_on_terminal="--"; }else{ var_called_on_terminal = pila_info['called_on_terminal']}
    $("#div_linked_sub").append("<p>&nbsp;&nbsp;<i class='fa fa-television'></i> Terminal "+var_called_on_terminal+"</p>");
    var var_request_validity;
    if(pila_info['request_validity'] == null){ var_request_validity="VALID"; }else{ var_request_validity = new Date(pila_info['request_validity']).toLocaleString()}
    // $("#div_linked_sub").append("<p>&nbsp;&nbsp;<i class='fa fa-bell-slash-o'></i> Expiration "+var_request_validity+"</p>");
    $("#div_linked_sub").append("<br/><hr/><h6>&nbsp;&nbsp;<i class='fa fa-bullhorn'></i> Announcement</h6><p>"+data['announcement']+"</p>");
  }else {
    load_qrcode();
  }
}

//---------------------------------------------------------------------------------------------------------
//-------------------------------------STUDENT SCHED-------------------------------------------------------
function student_sched(){
  $( "#container" ).load("{{ asset( 'html/profile/student_sched.php' ) }}", function(){
    get_request("{{ route('sched-info-get') }}", load_sched_info);
    get_request("{{ route('sched-get') }}", load_table_sched);
    $("#bulsu_logo").attr('src', "{{ asset( 'img/logo/BULSU_LOGO.png' ) }}");
    $("#cict_logo").attr('src', "{{ asset( 'img/logo/CICT_LOGO.png' ) }}");
  });
}

function load_sched_info(data){
  $("#acad_term").html(""); $("#course_section").html("");
  $("#row_sched").removeClass('hide');
  (data['acad_term']=="no current term") ? ($("#acad_term").append('Current term not set')) : ($("#acad_term").append("A.Y "+data['acad_term']['school_year']));
  (data['section']=="no student record") ? (section = "no section") : (section = data['section']['year_level']+data['section']['section']+"-G"+data['section']['_group']);
  (data['course']=="no curriculum") ? (cur = "no curriculum") : (cur=data['course']['name']);
  $("#course_section").append( cur+ " " + section);
}

function load_table_sched(data){
  $(".scheds").html("");
  if(data[0]['result'] == "no load subjects"){
    show_desc("#sched_result", "{{ asset( 'img/icons/calendar_stud.png' ) }}", "Schedule unavailable. <hr/> Schedule not yet posted.");
  } else{
    $("#stud_sched_table").removeAttr('style');
    $.each(data, function(key,value) {
      subject = value['subject'];
      schedule = value['load_group_schedule'];
      load_group = value['load_group'];
      faculty = value['faculty'];
      if(value['load_group_schedule'] !=null ){
        for (var i = 0; i < schedule.length; i++) {
          var faculty_name = "";
          (load_group[0]['faculty'] !=null) ? (faculty_name = faculty[0]['last_name'] +", "+faculty[0]['first_name'] ) : ("");
          $("#"+schedule[i]['class_day']).after("<tr class='text-center scheds'> <td>"+subject[0]['code']+"</td><td>"+convert(schedule[i]['class_start'])+"</td> <td>"+convert(schedule[i]['class_end'])+"</td> <td>"+schedule[i]['class_room']+"</td><td>"+faculty_name+"</td><tr/>");
          $("#"+schedule[i]['class_day']).attr('style', 'background-color:#EEEEEE');
        }
      }else {
        //alert('Load group schedule empty');
      }
    });
  }
}

function student_history(){
  $( "#container" ).load("{{ asset( 'html/profile/student_eval.php' ) }}", function(){
    get_request("{{ route('eval-get') }}", load_table_eval);
    $("#bulsu_logo").attr('src', "{{ asset( 'img/logo/BULSU_LOGO.png' ) }}");
    $("#cict_logo").attr('src', "{{ asset( 'img/logo/CICT_LOGO.png' ) }}");
  });
}

function load_table_eval(data){
  $("#tbl_eval td").remove();
  $("#row_eval").removeClass('hide');
  if(data[0]['result'] == "Student not found"){
    notify("Database Connection Failed", "Student data not found.");
  }
  if(data[0]['result'] == "No Evaluation History"){
    show_desc("#div_student_eval", "{{ asset( 'img/icons/history.png' ) }}", "Enrollment history unavailable. <hr/> No evaluation history found.");
  }else {
    $("#span_student_eval").removeAttr('style');
    $.each(data, function(key,value) {
      eval = value['eval'];
      acad = value['acad'];
      evaluator = value['evaluator'];
      faculty = value['faculty'];

      var evaluator_name = "";
      evaluator_name = (value['evaluator'] != null) ? (evaluator_name = evaluator['last_name']+" , "+evaluator['first_name']+" "+evaluator['middle_name']) : ("");
      var faculty_name = "";
      faculty_name = (value['faculty'] != null) ? (faculty_name = faculty['last_name']+" , "+faculty['first_name']+" "+faculty['middle_name']) : ("");
      cancelled_date = (value['cancelled_date']!=null) ? (value['cancelled_date']) : (cancelled_date="");
      if((value['eval'] !=null ) && (value['acad'] !=null )){
        $("#tbl_eval").append("<tr class='text-center'><td>"+acad['school_year']+"</td> <td>"+acad['semester']+"</td> <td>"+eval['year_level']+"</td> <td>"+acad['type']+"</td> <td>"+evaluator_name+"</td> <td>"+eval['evaluation_date']+"</td><td>"+eval['remarks']+"</td><td>"+faculty_name+"</td><td>"+cancelled_date+"</td></tr>");
        if(eval['active'] == "1"){
          $("#tbl_eval tr:last").attr('style','background-color:#8DCF8C');
        }
      }else {
      }
    });
  }
}
</script>

<script type = "text/javascript">

$(document).ready(function() {
  load_nav();
});

//----------------------------------------------------------
//load navbar and add attr on links
function load_nav(){
  $( "#container-nav" ).load("{{ asset( 'html/navbar/register_nav.php' ) }}", function(){
    $(".loader").attr('style', 'display:none');
    $("#btn_home").attr('href',"{{ route('home') }}");
    forgot_pass_verify();
  });
}

// default view when ready
function forgot_pass_verify(){
  $( "#container" ).load("{{ asset( 'html/forgot_password/step_one_verify.php' ) }}", function(){
    $( "#div_verify" ).hide().fadeIn("slow");

    //----------------------------------------------------------
    $("#frm_verify").validate({
      rules: {
        stud_username: {
          required: true,
          alphanumeric: true,
          minlength: 4,
          maxlength: 20
        },
      },
      messages: {
        stud_username: {
          required: "Please fill in your Username.",
        }
      },
    }); //end of validate
    //----------------------------------------------------------

    // onclick
    $( "#btn_verify" ).click(function(){
      var isValid = $("#frm_verify").valid();
      if(isValid == true){
        var form_values = $("#frm_verify").serializeObject();
        verify_student(form_values);
      }else{}
    }); //end of btn click
    onKeyPress('#stud_username','#btn_verify');
  });
}

function verify_student(post_parameters){
  request = new Request();
  request.url = "{{ route('forgot-pass-verify') }}";
  request.type = 'POST';
  request.data = post_parameters;
  request.replyType = 'json';
  // start
  request.begin = function(){
    btn_clicked_start("#btn_verify");
  }
  // success
  request.done = function(data, textStatus, xhr){
    btn_clicked_end("#btn_verify");
    onRequestSuccess(data/*$.parseJSON(data)*/);
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    //alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //alert("JQUERY TEXT STATUS: " + textStatus);
    //alert("ERROR DESCRIPTION: " + errorThrown);
    btn_clicked_end("#btn_verify");
    window.location = error_route + xhr.status;
  }
  // finished
  request.always = function(){
    // this will be called always whether fail or done at the end of this request
    btn_clicked_end("#btn_verify");
  }
  // send
  request.send(); // start the ajax request
}

/* When success */
function onRequestSuccess(data){
  if(data['result'] == 'true'){
    security_question(data['id']);
  }else{
    show_result('#stud_username-error-1',"Please check, username not valid");
  }
}

function security_question(){
  $( "#container" ).load("{{ asset( 'html/forgot_password/step_two_question.php' ) }}", function(){
    //alert(JSON.stringify(account_data));
    $(".loader").attr('style', 'display:none');
    $( "#div_confirm" ).hide().fadeIn("slow");
    request_question();

    //----------------------------------------------------------
    $("#frm_confirm").validate({
      rules: {
        recovery_answer: {
          required: true,
          minlength: 2
        },
      },
      messages: {
        recovery_answer: {
          required: "Please confirm your recovery answer.",
        }
      }
    }); //end of validate
    //----------------------------------------------------------

    $("#btn_confirm").click(function(){
      var isValid = $("#frm_confirm").valid();
      if(isValid == true){
        var form_values = $("#frm_confirm").serializeObject();
        check_question(form_values);
      }else{}
    }); //end of btn click
    onKeyPress('#recovery_answer','#btn_confirm');
  });
}

function check_question(post_parameters){
  request = new Request();
  request.url = "{{ route('forgot-pass-check-answer') }}";
  request.type = 'POST';
  request.data = post_parameters;
  request.replyType = 'json';
  // start
  request.begin = function(){
    btn_clicked_start("#btn_confirm");
  }
  // success
  request.done = function(data, textStatus, xhr){
    //alert(data);
    btn_clicked_end("#btn_confirm");
    onCheckSuccess(data/*$.parseJSON(data)*/);
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    //alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //alert("JQUERY TEXT STATUS: " + textStatus);
    //alert("ERROR DESCRIPTION: " + errorThrown);
    btn_clicked_end("#btn_confirm");
    window.location = error_route + xhr.status;
  }
  // finished
  request.always = function(){
    // this will be called always whether fail or done at the end of this request
    btn_clicked_end("#btn_confirm");
  }
  // send
  request.send(); // start the ajax request
}

function onCheckSuccess(data){
  if(data['result'] == 'true'){
    reset_password();
  }else{
    show_result('#recovery_answer-error-1',"Please check your recovery answer.");
  }
}

function request_question(){
  request = new Request();
  request.url = "{{ route('forgot-pass-get') }}";
  request.type = 'POST';
  request.replyType = 'json';
  // start
  request.begin = function(){
  }
  // success
  request.done = function(data, textStatus, xhr){
    //alert(data);
    onGetSuccess(data/*$.parseJSON(data)*/);
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

function onGetSuccess(data){
  if(data['result'] == 'true'){
    $("#recovery_question").val(data['question']);
  }else{
    notify("Database Connection Failed","Failed to fetch recovery question from database. Please refresh and try again.");
  }
}

function reset_password(){
  $("#container").load("{{ asset( 'html/forgot_password/step_three_reset.php' ) }}", function(){
    $("#div_reset").hide().fadeIn("slow");

    //----------------------------------------------------------
    $("#frm_reset").validate({
      rules: {
        password: {
          required: true,
          minlength: 6
        },
        confirm_password: {
          required: true,
          minlength: 6,
          equalTo: "#password"
        },
      },
      messages: {
        confirm_password: {
          equalTo: "Please enter your correct password again.",
        }
      }
    }); //end of validate
    //----------------------------------------------------------

    $("#btn_reset").click(function(){
      var isValid = $("#frm_reset").valid();
      if(isValid == true){
        var form_values = $("#frm_reset").serializeObject();
        update_password(form_values);
      }else{}
    }); //end of btn click
    onKeyPress('#confirm_password','#btn_reset');
  });
}

//----------------------------------------------------------
//Update paswword with new password
function update_password(post_parameters){
  request = new Request();
  request.url = "{{ route('forgot-pass-reset') }}";
  request.type = 'POST';
  request.data = post_parameters;
  request.replyType = 'json';
  // start
  request.begin = function(){
    btn_clicked_start("#btn_reset");
  }
  // success
  request.done = function(data, textStatus, xhr){
    //alert(data);
    btn_clicked_end("#btn_reset");
    onUpdateSuccess(data/*$.parseJSON(data)*/);
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    //alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //alert("JQUERY TEXT STATUS: " + textStatus);
    //alert("ERROR DESCRIPTION: " + errorThrown);
    btn_clicked_end("#btn_reset");
    window.location = error_route + xhr.status;
  }
  // finished
  request.always = function(){
    // this will be called always whether fail or done at the end of this request
    btn_clicked_end("#btn_reset");
  }
  // send
  request.send(); // start the ajax request
}

//----------------------------------------------------------
//On password update success
function onUpdateSuccess(data){
  if(data['result']=="saved"){
    reset_success();
  }else{
    notify("Database Connection Failed","Failed to save new password on database. Please refresh and try again.");
  }
}

//----------------------------------------------------------
//load success view
function reset_success(){
  $("#container").load("{{ asset('html/forgot_password/reset_success.php') }}", function(){
    $( "#container" ).hide().fadeIn("slow");
    $( "#btn_login" ).attr('href',"{{ route('show-login') }}");
    $( "#btn_home" ).attr('href',"{{ route('home') }}");
  });
}
</script>

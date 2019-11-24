<script type = "text/javascript">
$(document).ready(function() {
  load_nav();
});

function load_nav(){
  $( "#container-nav" ).load("{{ asset( 'html/navbar/register_nav.php' ) }}", function(){
    $(".loader").attr('style', 'display:none');
    $("#btn_home").attr('href',"{{ route('home') }}");
    registration_verify();
  });
}

// default view when ready
function registration_verify(){
  $( "#container" ).load("{{ asset( 'html/registration/step_one_verify.php' ) }}", function(){
    $(".loader").attr('style', 'display:none');
    $("#btn_login").attr('href', "{{ route('show-login') }}");

    //----------------------------------------------------------
    $("#frm_verify").validate({
      rules: {
        stud_id: {
          required: true,
          alphanumeric: true,
          minlength: 4,
          maxlength: 20
        },
      },
      messages: {
        stud_id: {
          required: "Please fill in your student ID.",
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
      }else{
      }
    }); //end of btn click

    onKeyPress('#stud_id','#btn_verify');
  });
}

// verify student
function verify_student(post_parameters){
  // settings
  request = new Request();
  request.url = "{{ route('register-verify') }}";
  //request.url = "http://localhost/laravel/cictonlineportal/public/regiser";
  request.type = 'POST';
  request.data = post_parameters;
  request.replyType = 'json';
  // start
  request.begin = function(){
    btn_clicked_start("#btn_verify");
  }
  // success
  request.done = function(data, textStatus, xhr){
    //alert(data);
    btn_clicked_end("#btn_verify");
    onRequestSuccess(data/*$.parseJSON(data)*/);
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    btn_clicked_end("#btn_verify");
    //  alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //  alert("JQUERY TEXT STATUS: " + textStatus);
    //  alert("ERROR DESCRIPTION: " + errorThrown);
    // window.location = error_route + xhr.status;
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
    check_account(data['id'])
  }else{
    show_result("#stud_id-error-1","You are not registered as a CICT Student.")
  }
}

function check_account(cict_id){
  var post_parameters = {};
  post_parameters['cict_id'] = cict_id;
  request = new Request();
  request.url = "{{ route('register-check-account') }}";
  request.type = 'POST';
  request.data = post_parameters;
  request.replyType = 'json';
  // start
  request.begin = function(){
    btn_clicked_start("#btn_verify");
  }
  // success
  request.done = function(data, textStatus, xhr){
    //alert(JSON.stringify(data));
    btn_clicked_end("#btn_verify");
    onCheckSuccess(data/*$.parseJSON(data)*/);
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

/*  When success  */
function onCheckSuccess(data){
  if(data['result'] == 'true'){
    account_login();
  }else{
    registration_confirm(data['id']);
  }
}
function account_login(){
  $( "#container" ).load("{{ asset( 'html/registration/account_exist.php' ) }}", function(){
    $("#btn_login").attr('href',"{{ route('show-login') }}");
    $("#btn_recover_account").attr('href',"{{ route('forgot-pass') }}");
    $("#btn_register_another").attr('href', "{{ route('register') }}");
  });
}

// when result is existing
// pass the id
function registration_confirm(cict_id){
  $( "#container" ).load("{{ asset( 'html/registration/step_two_confirm.php' ) }}", function(){
    $("#btn_login").attr('href', "{{ route('show-login') }}");
    //----------------------------------------------------------
    $("#frm_confirm").validate({
      rules: {
        last_name: {
          required: true,
          letterswithbasicpunc: true,
          minlength: 2,
          maxlength: 25
        },
        first_name: {
          required: true,
          letterswithbasicpunc: true,
          minlength: 2,
          maxlength: 25
        },
        middle_name: {
          letterswithbasicpunc: true,
          minlength: 1,
          maxlength: 25
        },
      },
      messages: {
        last_name: { required: "Please fill in this field.", letterswithbasicpunc: "Please fill in with letters only.", },
        first_name: { required: "Please fill in this field.", letterswithbasicpunc: "Please fill in with letters only.", },
        middle_name: { required: "Please fill in this field.", letterswithbasicpunc: "Please fill in with letters only.", },
      },
    }); //end of validate #frm_confirm
    //----------------------------------------------------------

    // onclick
    $( "#btn_confirm" ).click(function(){
      var isValid = $("#frm_confirm").valid();
      if(isValid == true){
        var form_values = $("#frm_confirm").serializeObject();
        confirm_information(form_values, cict_id);
      }else{
      }
    }); //end of btn click
    onKeyPress('#middle_name','#btn_confirm');
  });
}

function confirm_information(post_param, cict_id){

  var post_parameters = {};
  post_parameters = post_param;
  post_parameters['cict_id'] = cict_id;

  request = new Request();
  request.url = "{{ route('register-confirm') }}";
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
    onConfirmSuccess(data/*$.parseJSON(data)*/,cict_id);
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

/*   When success    */
function onConfirmSuccess(data, cict_id){
  if(data['result'] == 'true'){
    registration_account(cict_id);
  }else{
    show_result(".alert-msg","You have entered the incorrect information.")
  }
}

function registration_account(cict_id){
  $( "#container" ).load("{{ asset( 'html/registration/step_three_account.php' ) }}", function(){
    show_hide_password("#span_view_pass","#icon_pass", "password");
    show_hide_password("#span_view_confirm_pass","#icon_confirm_pass", "confirm_password");
    //----------------------------------------------------------
    $("#frm_register").validate({
      rules: {
        username: {
          required: true,
          alphanumeric: true,
          minlength: 6
        },
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
      },
      errorPlacement: function(error, element) {
        if (element.attr("name") == "password") {
          error.insertAfter(".pass");
        } else if (element.attr("name") == "confirm_password") {
          error.insertAfter(".confirm_pass");
        } else {
          error.insertAfter(element);
        }
      },
    }); //end of validate
    //----------------------------------------------------------

    $( "#btn_account" ).click(function(){
      var isValid = $("#frm_register").valid();
      if(isValid == true){
        var form_values = $("#frm_register").serializeObject();
        account_details(form_values, cict_id);
      }else{}
    }); //end of btn click
    onKeyPress('#confirm_password','#btn_account')
  });
}

function account_details(post_param, cict_id){
  var account_data = {};
  account_data = post_param;
  account_data['cict_id'] = cict_id;

  request = new Request();
  request.url = "{{ route('register-check-username') }}";
  request.type = 'POST';
  request.data = account_data;
  request.replyType = 'json';
  // start
  request.begin = function(){
    btn_clicked_start("#btn_account");
  }
  // success
  request.done = function(data, textStatus, xhr){
    btn_clicked_end("#btn_account");
    onCheckUsernameSuccess(data,account_data);
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    //alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //alert("JQUERY TEXT STATUS: " + textStatus);
    //alert("ERROR DESCRIPTION: " + errorThrown);
    btn_clicked_end("#btn_account");
    window.location = error_route + xhr.status;
  }
  // finished
  request.always = function(){
    // this will be called always whether fail or done at the end of this request
    btn_clicked_end("#btn_account");
  }
  // send
  request.send(); // start the ajax request
}

/* When success    */
function onCheckUsernameSuccess(data, account_data){
  if(data['result'] == 'available'){
    registration_secure(account_data)
  }else{
    show_result("#username-error-1","You have entered a taken username.");
  }
}

function registration_secure(account_data){
  // account_data is json
  $( "#container" ).load("{{ asset( 'html/registration/step_four_secure.php' ) }}", function(){
    show_hide_password("#span_view_ans","#icon_ans", "recovery_answer");
    show_hide_password("#span_view_confirm_ans","#icon_confirm_ans", "confirm_recovery_answer");

    //----------------------------------------------------------
    $("#frm_setup_acc").validate({
      rules: {
        recovery_answer: {
          required: true,
          minlength: 2
        },
        confirm_recovery_answer: {
          required: true,
          minlength: 2,
          equalTo: "#recovery_answer"
        },
      },
      messages: {
        confirm_recovery_answer: {
          equalTo: "Please confirm your recovery answer.",
        }
      },
      errorPlacement: function(error, element) {
        if (element.attr("name") == "recovery_answer") {
          error.insertAfter(".ans");
        }else if (element.attr("name") == "confirm_recovery_answer") {
          error.insertAfter(".confirm_ans");
        } else {
          error.insertAfter(element);
        }
      },
    }); //end of validate
    //----------------------------------------------------------

    $("#btn_register").click(function(){
      var isValid = $("#frm_setup_acc").valid();
      if(isValid == true){
        var form_values = $("#frm_setup_acc").serializeObject();
        var final_account_data = $.extend({}, account_data, form_values);
        floor_assignment(final_account_data);
      }else{}
    }); //end of btn click
    onKeyPress('#confirm_recovery_answer','#btn_register');
  });
}

function floor_assignment(final_account_data){
  $( "#container" ).load("{{ asset( 'html/registration/step_five_floor_assignment.php' ) }}", function(){

    //----------------------------------------------------------
    $("#frm_floor_assignment").validate({
      rules: {
        floor_assignment: {
          required: true,
        },
      },
      messages: {
        floor_assignment: {
          equalTo: "Please select your evaluation room.",
        }
      }
    }); //end of validate
    //----------------------------------------------------------

    $("#btn_register").click(function(){
      var isValid = $("#frm_floor_assignment").valid();
      if(isValid == true){
        var form_values = $("#frm_floor_assignment").serializeObject();
        create_account_request(form_values, final_account_data);
      }else{
      }
    }); //end of btn click
    onKeyPress('#floor_assignment','#btn_register');
  });
}

function create_account_request(post_param, account_data_param){
  var account_data = $.extend({}, account_data_param, post_param);
  request = new Request();
  request.url = "{{ route('register-create') }}";
  request.type = 'POST';
  request.data = account_data;
  request.replyType = 'json';
  // start
  request.begin = function(){
    btn_clicked_start("#btn_register");
  }
  // success
  request.done = function(data, textStatus, xhr){
    //alert(JSON.stringify(data));
    btn_clicked_end("#btn_register");
    onCreateSuccess(data/*$.parseJSON(data)*/);
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    //alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //alert("JQUERY TEXT STATUS: " + textStatus);
    //alert("ERROR DESCRIPTION: " + errorThrown);
    btn_clicked_end("#btn_register");
    // window.location = error_route + xhr.status;
  }
  // finished
  request.always = function(){
    // this will be called always whether fail or done at the end of this request
    btn_clicked_end("#btn_register");
  }
  // send
  request.send(); // start the ajax request
}

function onCreateSuccess(data){
  if(data['result'] == "saved"){
    window.location.href = "{{ route('profile') }}";
  }else{
    notify("Database Connection Failed","Registration Failed. Please try again after a few minutes");
  }
}
</script>

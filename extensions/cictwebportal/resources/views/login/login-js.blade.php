<script type = "text/javascript">
$(document).ready(function() {
  login_form();
});

function login_form(){
  $( "#container-login" ).load("{{ asset( 'html/login/login.php' ) }}", function(){
    $("#btn_register").attr('href',"{{ route('register') }}");
    $("#lnk_forgot_pass").attr('href',"{{ route('forgot-pass') }}");
    $("#btn_dl_app_login").attr('href',"{{ route('get-app') }}");
    $("#btn_home").attr('href',"{{ route('home') }}");
    show_hide_password("#span_view_pass","#icon_pass", "password");

    $("form").validate({
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
      },
      messages: {
        username: {
          required: "Please enter your username.",
        },
        password: {
          required: "Please enter your password.",
        }
      },
      errorPlacement: function(error, element) {
        if (element.attr("name") == "password") {
          error.insertAfter(".pass");
        } else {
          error.insertAfter(element);
        }
      },
    }); //end of validate */

    $( "#btn_signin" ).click(function(){
      var isValid = $("#frm_login").valid();
      if(isValid == true){
        var form_values = $("#frm_login").serializeObject();
        login_details(form_values);
      }else{
      }
    }); //end of btn click
    onKeyPress('#password','#btn_signin');
  });
}


function login_details(post_parameters){
  request = new Request();
  request.url = "{{ route('login-verify') }}";
  request.type = 'POST';
  request.data = post_parameters;
  request.replyType = 'json';
  // start
  request.begin = function(){
    btn_clicked_start("#btn_signin");
  }
  // success
  request.done = function(data, textStatus, xhr){
    //alert(data);
    btn_clicked_end("#btn_signin");
    onCheckAccountSuccess(data/*$.parseJSON(data)*/);
  }
  // failed
  request.fail = function(xhr, textStatus, errorThrown){
    //alert("STATUS AND READY STATE: " + xhr.status + "-" +xhr.readyState);
    //alert("JQUERY TEXT STATUS: " + textStatus);
    //alert("ERROR DESCRIPTION: " + errorThrown);
    btn_clicked_end("#btn_signin");
    window.location = error_route + xhr.status;
  }
  // finished
  request.always = function(){
    // this will be called always whether fail or done at the end of this request
    btn_clicked_end("#btn_signin");
  }
  // send
  request.send(); // start the ajax request
}

function onCheckAccountSuccess(data){
  if(data['result'] == "existing"){
    $(document).ready(function() {
      window.location.href = "{{ route('profile') }}";
    });
  }else if(data['result'] == "wrong_pass"){
    show_result('#password-error-1','Please re-check your password.');
    hide_result('#password','#password-error-1');
  }else{
    show_result('#username-error-1','Please check, account does not exists');
    hide_result('#username','#username-error-1');
  }
}

</script>

<script>
if($(window).width() < 450){
  $("#logo").width('150px');
  $("#logo").height('50px');
  $("#nav-nav").append('<br><br><br><br>');
}else{
  $("#logo").width('270px');
  $("#logo").height('90px');
  $("#nav-nav").append('<br><br><br><br><br><br>');
}
</script>
<div class="container-fluid" id="nav-nav">
  <div class="row">
    <nav class="navbar navbar-expand-md fixed-top fadeIn animated navbar-light" id="navbar_sub">
      <a class="navbar-brand" href="#"><img src="" class="img-fluid" id="logo" width="250" height="150"></a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav mr-auto">
        </ul>
        <ul class="navbar-nav">
          <li class="nav-item">
            <a class="nav-link page-scroll" href="#search-section" id="lnk_home" >HOME</a>
          </li>
          <li class="nav-item">
            <a class="nav-link page-scroll" href="#search-section" id="lnk_search" >TEACHER FINDER</a>
          </li>
          <li class="nav-item">
            <a class="nav-link page-scroll" href="#app-section" id="lnk_app" >APP</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#" id="lnk_register" >SIGNUP</a>
          </li>
          <li class="nav-item">
            <a class="nav-link underline" href="#" id="lnk_login" >LOGIN </a>
          </li>
        </ul>
    </div>
    </nav>
  </div>
</div>

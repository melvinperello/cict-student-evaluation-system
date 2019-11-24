@include('global.html-start')
<head>
@include('global.html-header')
<link rel="stylesheet" type="text/css" href="{{ asset('css/profile.css') }}">
<link rel="stylesheet" type="text/css" href="{{ asset('css/pic_modal.css') }}">
</head>

<script type="text/javascript">
$(window).resize(function(event) {
  if($(window).width() > 691){
    $("#sidebar").addClass('show');
  }
});
</script>

<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top" class="bg-orange"> <!-- Navigation -->
    <div id="container-nav">
    </div>

  <div class="container-fluid">
    <div class="row d-flex d-md-block flex-nowrap wrapper" id="container-profile" >
        <div class="col-md-2 col-sm-2 col-2 col-1 float-left collapse width show" id="sidebar">
        </div>
        <main class="col-md-9 col-12 px-4 main float-left "  id="container" style="margin-top: 2%;">
        </main>
    </div>
  </div>

  <div class="loader" style="position: absolute; top: 50%; left: 50%;">
      <div class="loader-inner ball-scale-ripple-multiple">
          <div></div>
          <div></div>
          <div></div>
      </div>
  </div>

@include('global.reusable-js')
@include('profile.profile-js')
  <!-- this will contain the footer-->
  <footer class="footer hidden-lg hidden-md hidden-sm col-xs-12 text-center" style="background-color:white; border-top: 1px solid #DBDBDB; " hidden>
  <div class="container">
    <div class="text-center" style="padding-top: 2%">
      <!-- <a href="https://www.facebook.com/monosyncstudioph/" target="_blank" style="font-size:7pt;"><span><img src="{{ asset('img/monosync_logo.jpg') }}" id="monosync_logo_footer" height="15" width="15"/></span> Monosync Studio PH | 2017 </a> -->
      <br><span class="" style="font-size:7pt">"Innovating Possibilities."</span>
    </div>
  </div>
  </footer>
    @include('global.js-footer-scripts')
    @include('global.html-end')

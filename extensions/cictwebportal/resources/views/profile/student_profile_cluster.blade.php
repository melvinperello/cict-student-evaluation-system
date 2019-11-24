@include('global.html-start')
<head>
@include('global.html-header')
<link rel="stylesheet" type="text/css" href="{{ asset('css/bootstrap/navbar-side-fix.css') }}">
<link rel="stylesheet" type="text/css" href="{{ asset('css/bootstrap/btn-arrow.css') }}">
<link rel="stylesheet" type="text/css" href="{{ asset('css/profile.css') }}">
<link rel="stylesheet" type="text/css" href="{{ asset('css/pic_modal.css') }}">
<link rel="stylesheet" type="text/css" href="{{ asset('css/curriculum.css') }}">
<script src="{{ asset('js/bootstrap/scrolling-nav.js') }}"></script>
<style media="screen">
/* Sticky footer styles
-------------------------------------------------- */
html {
  position: relative;
  min-height: 100%;
}
body {
  /* Margin bottom by footer height */
  margin-bottom: 60px;
}
.footer {
  position: absolute;
  bottom: 0;
  width: 100%;
  /* Set the fixed height of the footer here */
  height: 60px;
  background-color: #f5f5f5;
}
</style>
</head>

<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top"> <!-- Navigation -->
  <div class="loader" style="position: absolute; top: 50%; left: 50%;">
      <div class="loader-inner ball-scale-ripple-multiple">
          <div></div>
          <div></div>
          <div></div>
      </div>
  </div>

  <div id="container-nav">
  </div>

  <div class= "container-fluid" >
    <div class="row">
    <div class="col-lg-1"><br></div>
    @include('global.reusable-js')
    @include('profile.profile-catch-js')
    <!-- this will contain the profile views -->
    <div class="col-lg-8 col-md-10 col-sm-9 col-xs-12" id="container">
    </div>
    <div class="col-lg-1"><br></div>
  </div>
</div><br/>
  <!-- this will contain the footer-->
  <footer class="footer hidden-lg hidden-md hidden-sm col-xs-12 text-center" style="background-color:white; border-top: 1px solid #DBDBDB; ">
  <div class="container">
    <div class="text-center" style="padding-top: 2%">
      <a href="https://www.facebook.com/monosyncstudioph/" target="_blank" style="font-size:7pt;"><span><img src="{{ asset('img/monosync_logo.jpg') }}" id="monosync_logo_footer" height="15" width="15"/></span> Monosync Studio PH | 2017 </a>
      <br><span class="" style="font-size:7pt">"Innovating Possibilities."</span>
    </div>
  </div>
  </footer>
    @include('global.js-footer-scripts')
    @include('global.html-end')

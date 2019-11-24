@include('global.html-start')
<head>
@include('global.html-header')
<link rel="stylesheet" type="text/css" href="{{ asset('css/page_not_found.css') }}">
</head>
<body>
<div class="container-fluid" style="font-family:Montserrat">
  <div class="row">
    <div class="col-lg-1 col-md-1"><br></div>
    <div class="col-lg-5 col-md-5 col-sm-6"> <span id="span_404">404</span></div>
    <div class="col-lg-5 col-md-5 col-sm-6" id="span_one">
      <span id="span_sub_one">We couldn't find this page.</span>
    </div>
    <div class="col-lg-1"><br></div>
  </div>

  <div class="row">
    <div class="col-lg-1" ><br></div>
    <div class="pull-left" id="span_sub_two" >
      Would you like to go back to the homepage?
    </div>
  </div>
  <div class="container">
  <hr>
      <div class="col-12 text-center">
        <a href="{{ route('home') }}" class="btn btn-orange"> Go back to homepage</a>
      </div>
    </div>
</div>
@include('global.js-footer-scripts')
@include('global.html-end')

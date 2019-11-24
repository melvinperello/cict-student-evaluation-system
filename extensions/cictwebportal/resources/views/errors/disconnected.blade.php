@include('global.html-start')
<head>
  @include('global.html-header')
  <link rel="stylesheet" type="text/css" href="{{ asset('css/page_not_found.css') }}">
</head>
<body>
  <div class="container-fluid" style="font-family:Montserrat">
    <div class="row">
      <div class="col-lg-1 col-md-1"><br></div>
      <div class="col-lg-5 col-md-5 col-sm-6 col-12 text-center"> <span id="span_404"><img src="{{ asset('img/icons/network.png') }}" class="img-fluid"></span></div>
      <div class="col-lg-5 col-md-5 col-sm-6 col-12 text-center" id="span_one">
        <span id="span_sub_one">Connection to the network is unavailable.</span>
      </div>
      <div class="col-lg-1"><br></div>
    </div>
    <div class="container">
      <div class="row">
        <div class="col-lg-1" ><br></div>
        <div class="pull-left" id="span_sub_two" >
        </div>
      </div><hr>
      <div class="row">
        <div class="col-12 text-center" id="span_sub_option">
          Please try reconnectiong again after a few minutes and click the refresh button below.<br><br>
        </div>
        <div class="col-12 text-center">
          <a href="{{ route('home') }}" class="btn btn-orange"> Refresh Page</a>
        </div>
      </div>
      <hr>
    </div>
  </div>
  @include('global.js-footer-scripts')
  @include('global.html-end')

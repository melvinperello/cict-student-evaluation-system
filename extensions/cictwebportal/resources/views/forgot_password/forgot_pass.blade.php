@include('global.html-start')
<head>
@include('global.html-header')
<link rel="stylesheet" type="text/css" href="{{ asset('css/forgot_pass.css') }}">
</head>

<body id="page-top" class="two-half-colour-background">
  <div class="loader" style="position: absolute; top: 50%; left: 46%;">
      <div class="loader-inner square-spin">
        <div style="background-color: #CE6F2D"></div>
      </div>
  </div>

  <div id="container-nav" class="">
  </div>

   @include('global.reusable-js')
   @include('forgot_password.forgot-pass-js')
    <!-- this will contain the reigster views -->
    <div id="container">
    </div>

    @include('global.js-footer-scripts')
    @include('global.html-end')

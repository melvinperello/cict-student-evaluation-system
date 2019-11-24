@include('global.html-start')
<head>
@include('global.html-header')

<link rel="stylesheet" type="text/css" href="{{ asset('css/home.css') }}">
</head>

<body id="page-top" data-spy="scroll" class="two-colour-background" data-target=".navbar-fixed-top"> <!-- Navigation -->
  <div id="container-nav"></div>

  @include('global.reusable-js')
  @include('login.login-js')

  <div id = "container-login"></div>

  @include('global.js-footer-scripts')
  @include('global.html-end')

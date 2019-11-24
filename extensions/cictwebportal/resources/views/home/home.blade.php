@include('global.html-start')
<head>
@include('global.html-header')

<link rel="stylesheet" type="text/css" href="{{ asset('css/home.css') }}">
<script src="{{ asset('js/bootstrap/scrolling-nav.js') }}"></script>
</head>

<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top"> <!-- Navigation -->
  <div id="container-nav"></div>

  <div id = "container-hello"></div>

  <div id = "container-steps-in-eval"></div>

  @include('global.reusable-js')
  @include('home.home-js')

  <!-- this will contain the login form -->
  <div id = "container"></div>

  <div id = "container-announcement"></div>

  <div id = "container-faculty-sched"></div>

  <div id = "container-student-app"></div>

  <div id = "container-offline-webs"></div>

  <div id = "container-footer"></div>

  @include('global.js-footer-scripts')
  @include('global.html-end')

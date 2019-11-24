{{-- STATIC IMPORTS MUST BE VISIBLE IN ALL VIEWS
    IF AN IMPORT IS SPECIFIC TO ONLY ONE VIEW
    ADD IT IN THE HEADER TAG INSTEAD
   --}}
   <meta charset="utf-8">
   <meta name="X-UA-Compatible" content="IE=edge">
   <meta name="viewport" content="width=device-width, initial-scale=1">
   <meta id="csrf-token" name="csrf-token" content="{{ csrf_token() }}">
   <title>CICT Web Portal | College Of Information and Communications Technology</title>
   <link rel="icon" href="{{ asset('img/logo/CICT.png') }}" type="image/png">
   <!-- css files -->
   <link rel="stylesheet" type="text/css" href="{{ asset('css/bootstrap/v4/bootstrap.min.css') }}">
   <link rel="stylesheet" type="text/css" href="{{ asset('css/bootstrap/v4/font-awesome.min.css') }}">
   <link rel="stylesheet" type="text/css" href="{{ asset('css/bootstrap/v4/animate.css') }}">
   <link rel="stylesheet" type="text/css" href="{{ asset('css/jquery/jquery-ui.min.css') }}">
   <link rel="stylesheet" type="text/css" href="{{ asset('css/main.css') }}">
   <link rel="stylesheet" type="text/css" href="{{ asset('css/loaders.css') }}">
   <!-- javascript files -->
   <script type = "text/javascript" src = "{{ asset( 'js/jquery/jquery-3.2.1.js' ) }}"></script>
   <script type = "text/javascript" src="{{ asset('js/jquery/jquery.validate.js') }}"></script>
   <script type = "text/javascript" src="{{ asset('js/jquery/additional-methods.js') }}"></script>
   <script type = "text/javascript" src="{{ asset('js/jquery/jquery.serialize-object.min.js') }}"></script>
   <script type = "text/javascript" src="{{ asset('js/mono-ajax.js') }}"></script>
   <script type = "text/javascript" src="{{ asset('js/bootstrap/wow.min.js') }}"></script>
      <script>
          new WOW().init();
      </script>

   <!-- AJAX SETTINGS -->
   <script src = "{{asset('js/jquery/jquery-ajax-settings.js')}}"></script>

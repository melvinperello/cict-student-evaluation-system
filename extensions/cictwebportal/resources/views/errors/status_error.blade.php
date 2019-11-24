@include('global.html-start')
<head>
@include('global.html-header')
<link rel="stylesheet" type="text/css" href="{{ asset('css/page_not_found.css') }}">
</head>
<body>
  <script type="text/javascript">
  $(document).ready(function() {
    @if($status === "0")
      show_report('&nbsp;&nbsp;1', 'Request not initialized.','There was no connection to the server, the request was not received by the server.');

    @elseif($status === "1")
      show_report('&nbsp;&nbsp;1', 'Server connection established.','Connection from the client to the server was established but no data exchange is taking place');

    @elseif($status === "2")
      show_report('&nbsp;&nbsp;2','Request received.','The request was sent to the server and the server has received the request.');

    @elseif($status === "3")
      show_report('&nbsp;&nbsp;3','Processing request.','The server is now processing the request. (E.g. running queries validation etc. from the server side.');

    @elseif($status === "300")
      show_report("300","Multiple Choices","A link list. The user can select a link and go to that location. Maximum five addresses.");

    @elseif($status === "301")
      show_report("301","Moved Permanently","The requested page has moved to a new URL.");

    @elseif($status === "302")
      show_report("302","Found","The requested page has moved temporarily to a new URL.");

    @elseif($status === "303")
      show_report("303","See Other","The requested page can be found under a different URL.");

    @elseif($status === "304")
      show_report("304","Not Modified","Indicates the requested page has not been modified since last requested.");

    @elseif($status === "306")
      show_report("306","Switch Proxy","No longer used.")

    @elseif($status === "307")
      show_report("307","Temporary Redirect","The requested page has moved temporarily to a new URL.");

    @elseif($status === "308")
      show_report("308","Resume Incomplete","Used in the resumable requests proposal to resume aborted PUT or POST requests.");

    @elseif($status === "400")
      show_report("400","Bad Request",'The request cannot be fulfilled due to bad syntax.');

    @elseif($status === "401")
      show_report('401","Unauthorized','The request was a legal request, but the server is refusing to respond to it. For use when authentication is possible but has failed or not yet been provided');

    @elseif($status === "402")
      show_report("402","Payment required","Reserved for future use.");

    @elseif($status === "403")
     show_report("403","Forbidden","The request was a legal request, but the server is refusing to respond to it.");

    @elseif($status === "404")
      show_report("404","Not Found","The requested page could not be found but may be available again in the future.");

    @elseif($status === "405")
      show_report("405","Method not allowed","A request was made of a page using a request method not supported by that page.");

    @elseif($status === "406")
      show_report("406","Not Acceptable","The server can only generate a response that is not accepted by the client.");

    @elseif($status === "407")
      show_report("407","Proxy Authentication","Required	The client must first authenticate itself with the proxy.");

    @elseif($status === "408")
      show_report("408","Request Timeout","The server timed out waiting for the request.");

    @elseif($status === "409")
      show_report("409","Conflict","The request could not be completed because of a conflict in the request.");

    @elseif($status === "410")
      show_report("410","Gone","The requested page is no longer available.");

    @elseif($status === "411")
      show_report("411","Length Required	The 'Content-Length' is not defined. The server will not accept the request without it.");

    @elseif($status === "412")
      show_report("412","Precondition Failed","The precondition given in the request evaluated to false by the server.");

    @elseif($status === "413")
      show_report("413","Requet Entity Too Large","The server will not accept the request, because the request entity is too large.");

    @elseif($status === "414")
      show_report("414","Request-URI Too Long","The server will not accept the request, because the URL is too long. Occurs when you convert a POST request to a GET request with a long query information.");

    @elseif($status === "415")
      show_report("415","Unsupported Media Type","The server will not accept the request, because the media type is not supported.");

    @elseif($status === "416")
      show_report("416","Requested Range Not Satisfiable","The client has asked for a portion of the file, but the server cannot supply that portion.");

    @elseif($status === "417")
      show_report("417","Expectation Failed","The server cannot meet the requirements of the Expect request-header field.");

    @elseif($status === "500")
      // show_report("500","Internal Server Error","Please refresh your browser and try again after a few seconds.")
      window.location.href = "{{ route('profile') }}";
    @elseif($status === "501")
      show_report("501","Not Implemented","The server either does not recognize the request method, or it lacks the ability to fulfill the request.")

    @elseif($status === "502")
      show_report("502","Bad Gateway","The server was acting as a gateway or proxy and received an invalid response from the upstream server.")

    @elseif($status === "503")
      show_report("503","Service Unavailable","The server is currently unavailable (overloaded or down).")

    @elseif($status === "504")
      show_report("504","Gateway Timeout","The server was acting as a gateway or proxy and did not receive a timely response from the upstream server.")

    @elseif($status === "505")
      show_report("505","HTTP Version Not Supported","The server does not support the HTTP protocol version used in the request.")

    @elseif($status === "511")
      show_report("511","Network Authentication Required","The client needs to authenticate to gain network access.")
    @elseif($status === "Failed")
      show_report("---","Something went wrong!","Sorry something went wrong. Access Denied.")
    @else
      show_report("&nbsp;&nbsp;---","Status code not found!","Status code error not listed.");
    @endif
  });

  function show_report(status_code,title, desc){
    $("#span_404").append(status_code);
    $("#span_sub_one").append(title);
    $("#span_sub_two").append(desc);
  }
  </script>
<div class="container-fluid" style="font-family:Montserrat">
  <div class="row">
    <div class="col-lg-1 col-md-1"><br></div>
    <div class="col-lg-5 col-md-5 col-sm-6 col-12 text-center"> <span id="span_404"></span></div>
    <div class="col-lg-5 col-md-5 col-sm-6 col-12 text-center" id="span_one">
      <span id="span_sub_one"></span>
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
      Please try again and click the refresh button below.<br>
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

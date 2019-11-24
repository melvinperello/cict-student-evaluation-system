<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title></title>
    <style type="text/css">
      .header { font-size:9pt; text-align: center; table-layout: fixed; }
      .profile{ clear: both; width: 100%; table-layout: fixed; display: inline-table; }
      .sub-header{ table-layout: fixed; text-align: right; vertical-align: bottom;display: inline-table; }
      td.info { border-bottom: 1px solid black; font-size: 8pt; }
      /* tr.highlight td{height: 40px;} Padding and margin not working. Used table instead*/
      .label  { font-weight: bold; font-size: 10pt; }
      #xs-font { font-size: 7pt; }
    </style>
  </head>
  <body>
<span>
  <table>
    <tr>
      <td width="15%"><img src="{{ asset('img/logo/BULSU_LOGO.png' )}}" width="70" height="70" alt=""></td>
      <td width="70%"><table class="header">
        <tr><td>Republic of the Philippines</td></tr>
        <tr><td>BULACAN STATE UNIVERSITY</td></tr>
        <tr><td style="font-weight: bold">COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY</td></tr>
        <tr><td>City of Malolos, Bulacan</td></tr>
        <tr><td>(044) 919-7800 local 1101-1102</td></tr>
        <tr><td id="xs-font">Level III Re-Accredited by the Accrediting Agency of Chartered Colleges and</td></tr>
        <tr><td id="xs-font">University of the Philippines (AACCUP)</td></tr>
      </table></td>
      <td width="15%"><img src="{{ asset('img/logo/CICT_LOGO.png' )}}" width="70" height="70" alt=""></td>
    </tr>
  </table>
</span>
<br>
      <table style="table-layout: fixed; display: inline-table">
        <tr>
          <td width="65%" height="120">
          <table class="sub-header">
            <tr><td></td></tr><tr><td></td></tr><tr><td></td></tr>
            <tr><td style="font-weight: bold">NEW STUDENT PROFILE</td></tr>
            <tr><td width="50%"></td><td width="20%" class="info" style="font-size: 9pt; text-align:center;">{{ $sem }}</td><td width="17%">Sem S.Y.</td><td width="20%" class="info" style="font-size: 9pt; text-align:center">{{ $sy }}</td></tr>
          </table></td>
          @if( $student_profile->profile_picture == "NONE")
          <td width="30%" align="right" ><img src="{{ asset('img/pic.png') }}" alt="" style="width:75px; height:75px"></td>
          @else
          <td width="30%" align="right" ><img src="{{ $display_pic }}" alt="" style="border:1px solid black; width:75px; height:75px"></td>
          @endif
        </tr>
      <table>
<br>
<!-- tablealways makes new line// -  -->
      <table class="profile">
        <tr><td width="10%" class="label">Name:</td>              <td width="89%" class="info"> {{ $student->last_name.", ".$student->first_name." ".$student->middle_name }} </td></tr><br>
        <tr><td width="10%" class="label">Address:</td>           <td width="90%" class="info"> {{ $student_profile->student_address }} </td></tr><br>
        <tr><td width="13%" class="label">Contact No.:</td>       <td width="33%" class="info"> {{ $student_profile->mobile }}</td>
            <td width="16%" class="label">E-mail Address:</td>    <td width="38%" class="info"> {{ $student_profile->email }} </td></tr><br>
        <tr><td width="11%" class="label">Guardian:</td>          <td width="43%" class="info"> {{ $student_profile->ice_name }} </td>
            <td width="14%" class="label">Contact No.:</td>       <td width="32%" class="info"> {{ $student_profile->ice_contact }} </td></tr><br>
        <tr><td width="24%" class="label">Address of the Guardian:</td><td width="76%" class="info"> {{ $student_profile->ice_address }} </td></tr><br>
      </table>
<br><br><br>
      <span style=" font-family: Helvetica; font-size: 9pt ">BulSU-OP-CICT-03F1</span><br>
      <span style=" font-family: Helvetica; font-size: 9pt ">Revision: 0</span>
  </body>
</html>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title></title>
  <style type="text/css">
  .header { font-size:9pt; text-align: center; table-layout: fixed; }
  .name_header { font-size:8pt; text-align: left; table-layout: fixed; border:1px solid #C0C0C0;
    font-family: Helvetica; padding: 1px; border-right-width: 3px; border-bottom-width: 3px; }
    .profile{ clear: both; width: 100%; table-layout: fixed; display: inline-table; }
    .sub-header{ table-layout: fixed; text-align: right; vertical-align: bottom;display: inline-table; }
    td.info { border-bottom: 1px solid black; font-size: 8pt; }
    /* tr.highlight td{height: 40px;} Padding and margin not working. Used table instead*/
    .label  { font-weight: bold; font-size: 10pt; }
    #xs-font { font-size: 7pt; }
    .border_bottom{ border-bottom:1px solid #C0C0C0; font-weight: bolder; border-bottom-width: 1px; }
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
            <tr><td>Guinhawa, City of Malolos, Bulacan</td></tr><tr><td></td></tr><tr><td></td></tr>
            <tr><td style="font-weight: bold; font-size:12pt">UNOFFICIAL REPORT OF GRADES</td></tr>
          </table></td>
          <td width="15%"><img src="{{ asset('img/logo/CICT_LOGO.png' )}}" width="70" height="70" alt=""></td>
        </tr>
      </table>
    </span>
    <br/>
    <span>
      <table>
        <tr>
          <td width="6%"></td>
          <td width="90%"><table class="name_header">
            <tr><td></td></tr>
            <tr><td width="2%"></td><td width="11%">Name:</td><td width="38%">{{ $student->last_name.", ".$student->first_name." ".$student->middle_name }}</td>          <td width="21%">Academic Term and Year:</td><td width="27%">{{ $year_sem }}</td></tr>
            <tr><td width="2%"></td><td width="11%">Student No.:</td><td width="38%">{{ $student->id }}</td>   <td width="21%">Year Level:</td><td width="15%">{{ $student->year_level }}</td></tr>
            <tr><td width="2%"></td><td width="11%">Course:</td><td width="38%">{{ $cur }}</td></tr>
            <tr><td></td></tr>
          </table></td>
        </tr>
      </table>
    </span>
    <br>
    <span>
      <table>
        <tr>
          <td width="5%"></td>
          <td width="90%"><table class="name_header" style="padding: 5px;">
            <tr><td width="2%"class="border_bottom"></td><th width="17%" class="border_bottom">SUBJECT CODE</th><th width="54%" class="border_bottom">TITLE</th><th width="8%" class="border_bottom">UNITS</th><th width="8%" class="border_bottom">FINAL</th><th width="12%" class="border_bottom">REMARKS</th></tr>
            @foreach ($collection as $each)
            <?php $grade = $each['grade']['remarks'] ?>
            @if( $grade == "FAILED" )
            <tr style="color: red"><td width="2%"></td><td>{{ $each['subject']['code'] }}</td><td>{{ $each['subject']['descriptive_title'] }}</td><td>{{ $each['grade']['credit'] }}</td><td>{{ $each['grade']['rating'] }}</td><td>{{ $each['grade']['remarks'] }}</td></tr>
            @else
            <tr><td width="2%"></td><td>{{ $each['subject']['code'] }}</td><td>{{ $each['subject']['descriptive_title'] }}</td><td>{{ $each['grade']['credit'] }}</td><td>{{ $each['grade']['rating'] }}</td><td>{{ $each['grade']['remarks'] }}</td></tr>
            @endif
            @endforeach
            <tr><td></td></tr>
          </table></td>
        </tr>
      </table>
    </span>
    <br/><br/><br/>
    <span style=" font-family: Helvetica; font-size: 9pt ">PRINT INFO: {{ $time }}</span>
  </body>
  </html>

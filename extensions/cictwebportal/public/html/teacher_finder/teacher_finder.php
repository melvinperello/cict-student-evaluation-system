<div class="container-fluid fadeIn animated div-border-orange">
    <br><br><h1 class="text-center mont banner">SEARCH SCHEDULE</h1> <br>
</div>

<div class="container-fluid wow fadeIn animated" style=" padding-top:6%;">
  <div class="row">
      <div class="container col-lg-5">
      <div class="input-group">
        <input id="txt_faculty_name" name="txt_faculty_name" value="" class="form-control pl-2" style="padding: 0;" placeholder="Search Faculty Name">
        <input id="txt_faculty_id" name="txt_faculty_id" value="" type="hidden">
        &nbsp;<button type="button" id="btn_search_faculty" name="btn_search_faculty" class="btn btn-orange">Search</button>
      </div>
      </div>
  </div><br><br>
</div>

<div class="container-fluid wow fadeIn animated" style="padding-top:6%;" id="search_desc">
</div>

<div class="wow fadeIn animated" id="faculty_sched_table" style="display:none"><br><br>
  <div class="row">
    <div class="container col-lg-10">
      <div id="div_sched" class="table-responsive">
        <table style="width:100%; border: 1px solid #EEEEEE">
          <tr>
            <td class="text-right" width="25%"><img id="bulsu_logo" src="" width="70" height="70" alt=""></td>
            <td class="text-center" width="50%"><table class="header" style="width:100%;">
              <tr><td>BULACAN STATE UNIVERSITY</td></tr>
              <tr><td style="font-weight: bold">COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY</td></tr>
              <tr><td id="acad_term"></td></tr>
              <tr><td id="course_section"></td></tr>
            </table></td>
            <td class="text-left" width="25%"><img id="cict_logo" src="" width="70" height="70" alt=""></td>
          </tr>
        </table>
      <br>
    </div>
    </div>
  </div>

  <div class="row">
    <div class="container col-lg-10 mont">
      <div id="div_sched" class="table-responsive">
      <table border="1" id="tbl_sched" class="table table-bordered text-left" style="padding: 5%; font-size:9pt" >
        <tr  id="SUNDAY">
          <td colspan="5">SUNDAY</td>
        </tr>
        <tr  id="MONDAY">
          <td colspan="5">MONDAY</td>
        </tr>
        <tr  id="TUESDAY">
          <td colspan="5">TUESDAY</td>
        </tr>
        <tr  id="WEDNESDAY">
          <td colspan="5">WEDNESDAY</td>
        </tr>
        <tr  id="THURSDAY">
          <td colspan="5">THURSDAY</td>
        </tr>
        <tr  id="FRIDAY">
          <td colspan="5">FRIDAY</td>
        </tr>
        <tr  id="SATURDAY">
          <td colspan="5">SATURDAY</td>
        </tr>
      </div>
    </div>
  </div>
</div>

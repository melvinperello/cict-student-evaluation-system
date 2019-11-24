<div id="modal" class="mont" title="Upload Display Photo">
  <form id="form_upload" enctype="multipart/form-data">
  <div id="image_preview"><img id="preview" class="img-fluid img-thumbnail"/></div>
  <hr>
  <div id="selectImage">

    <div id="upload_message"></div>
  <label>Select Your Image</label><br/>
  <input type="file" name="image-file" id="image-file"/>
  <button type="button" class="btn btn-black-bordered" name="image-upload" id="image-upload" style="padding:2%;">Upload Image</button>
  </div>
  </form>
</div>

<form id = "frm_student_profile">
  <div class="row white-bg div-white-shadow" style="margin-bottom: 2%;  padding: 3%;">
    <div class="col-lg-3 col-md-3 col-sm-3 col-6" style="border-left:2px solid #CE6F2D">
      <label for="" class="">Student ID / Number</label><br>
      <input type="text" autocomplete="off" class="span_orange" id="stud-id" value="" disabled>
    </div>
    <div class="col-lg-3 col-md-3 col-sm-3 col-6" style="border-left:2px solid #CE6F2D">
      <label for="" class="">Year/ Section/ Group</label><br>
      <input type="text" autocomplete="off" class="span_orange" id="year-sec-gr" value="" disabled>
    </div>
    <div class="col-lg-3 col-md-3 col-sm-3 col-6" style="border-left:2px solid #CE6F2D">
      <label for="" class="">Course Curriculum</label><br>
      <input type="text" autocomplete="off" class="span_orange" id="curriculum" value="" disabled>
    </div>
    <div class="col-lg-3 col-md-3 col-sm-3 col-6" style="border-left:2px solid #CE6F2D">
      <label for="" class="">Cluster Assignment</label><br>
      <input type="text" autocomplete="off" class="span_orange" id="floor-assignment-lbl" value="" disabled>
    </div>
  </div>
  <div class="row white-bg div-white-shadow" style="padding: 3%;">
    <div class="container col-12">
      <div class="container col-lg-10 col-md-9 col-sm-9 col-6 float-left" style="padding-top:2%; padding-left:0%;">
      <h6 class="bold">STUDENT PROFILE</h6>
      </div>
      <div class="container col-lg-2 col-md-3 col-sm-3 col-6 float-left" id="div_view_pdf">
          <a id="btn_view_pdf" class="btn btn-black-bordered" style="padding: 4%" target="_blank"><i class="fa fa-file-pdf-o"></i> VIEW PDF</a>
      </div>
    <br><br><hr>
    </div>

    <div class="col-lg-3 col-md-5 col-sm-12 col-12 text-center" id="profile-pic-1">
      <img id="display-pic" onload="" class="img-fluid img-thumbnail" width="150" height="150" alt=""><br>
      <button id="btn_change_pic" class="btn btn-black-bordered" style="padding: 2%" type="button">Change Picture</button>
    </div>

    <div class="col-lg-9 col-md-7 col-12">
      <div class="col-12 col-md-12">
        <label for="">First name</label><br>
        <input type="text" autocomplete="off" name="" class="form-control input-sm" id="first_name" value="" disabled>
      </div>
      <div class="col-12 col-md-12">
        <label for="">Middle name</label><br>
        <input type="text" autocomplete="off" name="" class="form-control input-sm" id="middle_name" value="" disabled>
      </div>
      <div class="col-12 col-md-12">
        <label for="">Last name</label><br>
        <input type="text" autocomplete="off" name="" class="form-control input-sm" id="last_name" value="" disabled>
      </div>
    </div>

    <div class="col-12">
      <div class="col-12 float-left" style="padding-top:2%; padding-left:0%">
      <h6 class="bold">STUDENT INFORMATION</h6>
      </div>
    <br><br><hr>
    </div>

    <div class="col-lg-6">
      <div class="col-12">
        <label for="">Contact no. *</label><br>
        <input type="text" autocomplete="off" name="contact_no" id="contact_no" class="form-control input-sm" placeholder="e.g. 09437083011" value="">
      </div>
      <div class="col-12">
        <label for="">E-mail address *</label><br>
        <input type="text" autocomplete="off" name="email" id="email" class="form-control input-sm" placeholder="e.g. juandelacruz@gamil.com" value="">
      </div>
    </div>

    <div class="col-lg-6">
      <div class="col-12" style="">
        <label for="">Gender *</label><br>
        <div class="btn-group" id="radio_gender" data-toggle="buttons">
          <label class="btn btn-blue MALE">
            <input type="radio" name="gender" id="MALE" value="MALE">Male
          </label>
          <label class="btn btn-blue FEMALE">
            <input type="radio" name="gender" id="FEMALE" value="FEMALE">Female
          </label>
        </div>
      </div>
      <div class="col-12">
        <label for="zipcode" class="">Zipcode (Optional)</label><br>
        <input type="text" autocomplete="off" id="zipcode" name="zipcode" class="form-control input-sm" placeholder="e.g. 3005" value="">
      </div>
    </div>

    <div class="col-lg-12">
      <div class="col-12">
        <label for="student_address" class="">Complete Address *</label><br>
        <input type="text" autocomplete="off" id="student_address" name="student_address" class="form-control input-sm" placeholder="e.g. 03 National Road Cut-Cot, Pulilan, Bulacan" value="">
      </div>
    </div>

    <div class="col-12">
      <div class="col-12 float-left" style="padding-top:2%; padding-left:0%">
      <h6 class="bold">GUARDIAN'S INFORMATION</h6>
      </div>
    <br><br><hr>
    </div>

    <div class="col-lg-6">
      <div class="col-12">
        <label for="">Full name *</label><br>
        <input type="text" autocomplete="off" name="ice_name" id="ice_name" class="form-control input-sm" placeholder="e.g. Mario Dela Cruz">
      </div>
    </div>

    <div class="col-lg-6">
      <div class="col-12">
        <label for="">Contact no. *</label><br>
        <input type="text" autocomplete="off" name="ice_contact" id="ice_contact" class="form-control input-sm" placeholder="e.g. 09228575766">
      </div>
    </div>

    <div class="col-lg-12">
      <div class="col-12">
        <label for="">Complete address *</label><br>
        <input type="text" autocomplete="off" name="ice_address" id="ice_address" class="form-control input-sm" placeholder="e.g. #07 National Road Cut-Cot, Pulilan, Bulacan">
      </div>
    </div>

    <div class="col-12 text-center">
      <br><div class="alert alert-secondary" id="alert-me" style="display:none">
      </div>
    </div>

    <div class="col-12 text-center">
      <div id="div_btn" class="mx-auto" style="margin-top:3%;" >
        <button type="button" name = "btn_save" id="btn_save" class="btn btn-black-bordered" style="padding: 2%"><i class="fa fa-save"></i> SAVE CHANGES</button>
      </div>
    </div>


  </form>
  </div>


$(document).ready(function(){
    $("#generated_form").hide();
});
// redirect to home
$('#btn_main').click(function () {
    window.location = 'index.html';
});
// validation rules
$("#frm_numbering").validate({
    rules: {
        txt_student_number: {
            required: true,
            alphanumeric: true,
			minlength: 4,
            maxlength: 20
        },
    },
}); //end of validate


//------------------------------------------------------
// preventing default action
$("#frm_numbering").submit(function (e) {
    return false;
});
//------------------------------------------------------

// QR CODE snippet
var qrcode = new QRCode(document.getElementById("div_qrcode"), {
    width: 300,
    height: 300,
    correctLevel: QRCode.CorrectLevel.Q,
});

// on btn generate click
$("#btn_generate").click(function () {
    validateAndGenerate();
}); //end of btn click

$("#txt_student_number").on('keydown', function (e) {
    if (e.keyCode == 13) {
        validateAndGenerate();
    }
});


function validateAndGenerate() {
    $("#generated_form").hide();
    var isValid = $("#frm_numbering").valid();
    if (isValid === true) {
        var form_values = $("#frm_numbering").serializeObject();
        var studentNumber = form_values['txt_student_number'];
        generate(studentNumber);
    }
}

function generate(student_number) {
    if (student_number === "") {
        return;
    }
    final_string = "MF#N#" + student_number;
    qrcode.makeCode(final_string);
    $("img").addClass("img-fluid");
    $("#generated_form").fadeOut().fadeIn();
	
	//------------------------------------------------------------------
	var img = $("#div_qrcode > img");
	img.click(function(){
	    validateAndGenerate();
		download(img.prop("src"), "NumberingCode.png", "image/png");
	});
	//------------------------------------------------------------------
	
};



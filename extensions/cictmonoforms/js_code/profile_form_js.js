$(document).ready(function () {
    $("#generated_form").hide();
});

//----------------------------------------------------------------
$('#chk_add').change(function () {
    if ($(this).is(':checked')) {
        var address = $("#txt_student_address").val();
        if(address == ""){
            alert("Your address is empty!");
            $(this).prop("checked", false);
            return;
        }
        $("#txt_ice_address").prop('readonly', true);
        $("#txt_ice_address").val(address);
    } else {
        $("#txt_ice_address").prop('readonly', false);
        $("#txt_ice_address").val("");
    }
});
//----------------------------------------------------------------

$('#btn_main').click(function () {
    window.location = 'index.html';
});

$("#frm_profiling").validate({
    rules: {
        txt_student_number: {
            required: true,
            alphanumeric: true,
			minlength: 4,
            maxlength: 20
        },
        txt_last_name: {
            required: true,
            letterswithbasicpunc: true,
            minlength: 2,
			maxlength: 25
        },
        txt_first_name: {
            required: true,
            letterswithbasicpunc: true,
            minlength: 2,
			maxlength: 25
        },
        txt_middle_name: {
            letterswithbasicpunc: true,
            minlength: 1,
			maxlength: 25
        },
        txt_floor: {
            required: true
        },
        gender: {
            required: true
        },
        txt_zipcode: {
            required: false,
			minlength: 4,
            maxlength: 10
        },
        txt_student_number: {
            required: true,
            minlength: 2,
            maxlength: 100
        },
        // txt_house_no: {
        //     required: true,
        //     minlength: 1,
        //     /*pattern: /^\d+(-\d+)*$/,*/
        //     maxlength: 20
        // },
        // txt_street: {
        //     required: true,
        //     minlength: 2,
        //     maxlength: 25
        // },
        // txt_city: {
        //     required: true,
			// minlength: 2,
        //     maxlength: 25
        // },
        // txt_brgy: {
        //     required: true,
			// minlength: 2,
        //     maxlength: 25
        // },
        // txt_province: {
        //     required: true,
			// minlength: 2,
        //     maxlength: 25
        // },
        txt_email: {
            required: true,
            email: true,
			minlength: 5,
            maxlength: 40
        },
        txt_contact_no: {
            required: true,
            digits: true,
            minlength: 11,
            maxlength: 11
        },
        txt_ice_name: {
            required: true,
            letterswithbasicpunc: true,
			minlength: 2,
            maxlength: 60
        },
        txt_ice_address: {
            required: true,
			minlength: 2,
            maxlength: 100
        },
        txt_ice_contact: {
            required: true,
            digits: true,
            minlength: 11,
            maxlength: 11
        },
    },
	messages: {
		txt_house_no: {
			pattern: 'Please enter numbers with hypens only. e.g. 12-45'
		}
	},
    errorPlacement: function (error, element) {
        if (element.attr("name") == "gender") {
            error.insertAfter("#radio_gender");
        } else if (element.attr("name") == "txt_floor") {
            error.insertAfter("#radio_flr");
        } else {
            error.insertAfter(element);
        }
    },
}); //end of validate

//------------------------------------------------------
// preventing default action
$("#frm_profiling").submit(function (e) {
    return false;
});
//------------------------------------------------------


String.prototype.mtrim = function () {
    return this.replace(/^\s+|\s+$/gm, '').toUpperCase();
}

// QR CODE snippet
var qrcode = new QRCode(document.getElementById("div_qrcode"), {
    width: 300,
    height: 300,
    correctLevel: QRCode.CorrectLevel.Q,
});


function create_qr(form_values) {
    $("#generated_form").hide();
    var student_number = form_values['txt_student_number'];
    var last_name = form_values['txt_last_name'];
    var first_name = form_values['txt_first_name'];
    var middle_name = form_values['txt_middle_name'];
    var contact_no = form_values['txt_contact_no'];
    var email = form_values['txt_email'];
    var gender = form_values['gender'];
    var floor = form_values['txt_floor'];
    //
    var zipcode = form_values['txt_zipcode'];
    var student_address = form_values['txt_student_address']
    // var house_no = form_values['txt_house_no'];
    // var street = form_values['txt_street'];
    // var brgy = form_values['txt_brgy'];
    // var city = form_values['txt_city'];
    // var province = form_values['txt_province'];
    //
    var txt_ice_name = form_values['txt_ice_name'];
    var txt_ice_address = form_values['txt_ice_address'];
    var txt_ice_contact = form_values['txt_ice_contact'];
    //
    var info = new Array();
    info[0] = "MF"; // Mono Forms Header
    info[1] = "P"; // Form Type P = Profiling
    info[2] = student_number.mtrim();
    info[3] = last_name.mtrim();
    info[4] = first_name.mtrim();
    info[5] = middle_name.mtrim();
    info[6] = gender.mtrim();
    info[7] = floor.mtrim();
    info[8] = contact_no.mtrim();
    //
    info[9] = zipcode.mtrim();

    // removed address info
    // info[10] = "-";// house_no.mtrim();
    // info[11] = "-";// street.mtrim();
    // info[12] = "-";//brgy.mtrim();
    // info[13] = "-";//city.mtrim();
    // info[14] = "-";//province.mtrim();
    info[10] = student_address.mtrim();

    info[11] = email.mtrim();


    //
    info[12] = txt_ice_name.mtrim();
    info[13] = txt_ice_address.mtrim();
    info[14] = txt_ice_contact.mtrim();
    //


    var delimiter = "#";

    var final_string = info.join(delimiter);
    // alert(final_string);

    qrcode.makeCode(final_string);
    $("img").addClass("img-fluid");
    $("#generated_form").fadeOut().fadeIn();

}

$("#btn_submit").click(function () {
    generateCode();
    //------------------------------------------------------------------
    var img = $("#div_qrcode > img");
    img.click(function(){
        generateCode();
        download(img.prop("src"), "ProfilingCode.png", "image/png");
    });
    //------------------------------------------------------------------
}); //end of btn click

function generateCode(){
    var isValid = $("#frm_profiling").valid();
    if (isValid == true) {
        var form_values = $("#frm_profiling").serializeObject();
        create_qr(form_values);
         $("#msg_box").prop("hidden", true);
    }else{
        //alert("Some fields might be empty or invalid.");
        $("#msg_box").prop("hidden", false);
    }
}
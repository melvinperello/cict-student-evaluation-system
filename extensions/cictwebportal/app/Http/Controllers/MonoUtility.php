<?php
namespace App\Http\Controllers;
use App\StudentProfile;

class MonoUtility
{
    public static function cloneProfile(StudentProfile $student_profile)
    {
        $cloned_profile = new StudentProfile;
        $cloned_profile->STUDENT_id = $student_profile->STUDENT_id;
        $cloned_profile->floor_assignment = $student_profile->floor_assignment;
        $cloned_profile->profile_picture = $student_profile->profile_picture;
        $cloned_profile->mobile = $student_profile->mobile;
        // $cloned_profile->house_no = $student_profile->house_no;
        // $cloned_profile->street = $student_profile->street;
        // $cloned_profile->brgy = $student_profile->brgy;
        // $cloned_profile->city = $student_profile->city;
        // $cloned_profile->province = $student_profile->province;
        $cloned_profile->student_address = $student_profile->student_address;
        $cloned_profile->zipcode = $student_profile->zipcode;
        $cloned_profile->email = $student_profile->email;
        $cloned_profile->ice_name = $student_profile->ice_name;
        $cloned_profile->ice_address = $student_profile->ice_address;
        $cloned_profile->ice_contact = $student_profile->ice_contact;

        return $cloned_profile;
    }
}

?>

<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

// chage to post
//Route::get('linked/login/{username}/{password}/{imei}/{is_skipped}', "Authenticator@login");
Route::post('linked/login', 'Authenticator@login');
Route::get('linked/is_used/{imei}', "Authenticator@is_used");
// chage to post
//Route::get('linked/logout/{account_id}/{type}', "Authenticator@logout");
Route::post('linked/logout', "Authenticator@logout");

Route::get('linked/search/{student_number}', "Marshall@search");
Route::post('linked/add_student', "Marshall@add_student");
Route::post('linked/create_student', "Marshall@create_new_student");

//
// cict_id param
Route::get('linked/check/{id}', "Marshall@check");
Route::post('linked/entrance', 'Marshall@entrance_pass');


// Student
// cict_id param
Route::get('linked/check_number/{cict_id}', 'Student@check_number');
Route::post('linked/cancel_reference', 'Student@cancel_reference');

// Mono Form
Route::post('linked/monoforms/profile', 'MonoForms@mono_profile');

//---------------------------------------------------------
// settings at config/filesystems.php
Route::get('linked/photo/{photo}', 'Media@get_photo');
//----------------------------------------------------------
#-----------------------------------------------------------
# New Routes since 11/13/2017
Route::post('linked/student/update', 'Marshall@update_student_info');
Route::get('linked/student/update/fetch/{cict_id}', 'Marshall@fetch_update_information');

<?php
namespace App\Http\Controllers;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Response;
use Illuminate\Http\File;
use App\StudentProfile;
use App\Student;
use Validator;
use Carbon\Carbon;
use Image;
use App\MonoImage;

class Media extends Controller
{
    /**
     * retrieves picture from the ftp server
     * @param $photo the file name from the server
     */
    public function get_photo($photo)
    {

        $profile_dir = "student_avatar";
        $photo_file = $profile_dir . "/" . $photo;

        // check if the file is existing.
        $exists = Storage::disk('ftp')->exists($photo_file);

        if ($exists) {
            $file = Storage::disk('ftp')->get($photo_file);

            $fileName = $photo_file;
            return Response::make($file, '200', array(
                'Content-Type' => 'application/octet-stream',
                'Content-Disposition' => 'attachment; filename="' . $fileName . '"'
            ));
        } else {
            $res['res'] = "not_found";
            print json_encode($res, JSON_FORCE_OBJECT);
        }
    }

    public function get_app()
    {
        $profile_dir = "application";
        $photo_file = $profile_dir . "/com.jhmvin.linked-10-10.apk";

        // check if the file is existing.
        $exists = Storage::disk('ftp')->exists($photo_file);

        if ($exists) {
            $file = Storage::disk('ftp')->get($photo_file);

            $fileName = $photo_file;
            return Response::make($file, '200', array(
                'Content-Type' => 'application/octet-stream',
                'Content-Disposition' => 'attachment; filename="' . $fileName . '"'
            ));
        } else {
            $res['res'] = "not_found";
            print json_encode($res, JSON_FORCE_OBJECT);
        }
    }

    public function upload_photo(Request $request)
    {
        #--------------------------------------------------------------------------
        // get cict_id of the corresponding student
        $cict_id = $request->session()->get('SES_CICT_ID');

        if (!$image = MonoImage::getFileFromRequest($request, 'image-file')) {
            return response()->json(['result' => 'file_not_recieved']);
        }
        #--------------------------------------------------------------------------

        // get supported image formats
        $validator = Validator::make($request->all(), [
            //'image-file' => 'image|mimes:jpeg,png,jpg,gif,svg|max:2000',2mb
            'image-file' => 'image|max:2000',
        ]);

        // check if the image have passed the required validation
        if (!$validator->passes()) {
            // failed to pass validator
            //return response()->json(['error' => $validator->errors()->all()]);
            return response()->json(['result' => 'file_not_valid']);
        }
        #--------------------------------------------------------------------------
        $fileExtension = $image->getClientOriginalExtension();
        $save_image_name = 'profile_' . $cict_id . '.' . $fileExtension;
        #--------------------------------------------------------------------------
        try {
            // resize image 
            $img = MonoImage::resizeImage($image, $save_image_name);
            if($img == "image_rectangle"){
              return response()->json(['result' => 'image_rectangle']);
            }else{
              //save in ftp then delete in temp folder
              MonoImage::saveToFTP('student_avatar/', $save_image_name, $img);
              // if it succeeded then great
              $file_uploaded = true;
              // but some things are meant to go wrong sometime
              // recheck if the file is now existing in the disk
              $exists = MonoImage::checkIfExist('student_avatar/', $save_image_name);
              // if not existing
              // sometimes servers are not reliable
              if (!$exists) {
                  $file_uploaded = false;
              }
            }
            #--------------------------------------------------------------------------
        } catch (Exception $e) {
            // if not then accept it it's not for you
            $file_uploaded = false;
        }
        // if the file was not uploaded
        if (!$file_uploaded) {
            // request var has no image file
            //dd('No image was found');
            return response()->json(['result' => 'file_not_uploaded']);
        }
        // in this segment of code we are certain the the file is already in the server
        // save the changes to the database
        // so what if the database failed to update ?
        // even if the database failed to updates it will not cause any harm
        // because the file will be just dormant and not usable in the server
        // what we are after is the accuracy of the database not the files

        // fetch the student profile

        // @fix
        // using the mass assignment syntax
        // still puzzled why the previous code updates all row event though it was set to update
        // active rows only
        #--------------------------------------------------------------------------
        $student_profile = StudentProfile::where('STUDENT_id', '=', $cict_id)
            ->where('active', '=', '1')
            ->orderBy('id', 'DESC')
            ->take(1)
            ->update(['profile_picture' => $save_image_name,]);
        #--------------------------------------------------------------------------
        if ($student_profile) {
            return response()->json(['result' => 'ok'], 200);
        } else {
            return response()->json(['result' => 'db_fail']);
        }
        #--------------------------------------------------------------------------
    }
}

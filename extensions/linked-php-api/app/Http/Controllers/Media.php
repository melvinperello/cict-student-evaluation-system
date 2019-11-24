<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Response;


class Media extends Controller
{
    //

    /**
     * retrieves picture from the ftp server
     * @param $photo the file name from the server
     */
    public function get_photo($photo){

        $profile_dir = "student_avatar";
        $photo_file = $profile_dir."/".$photo;

        // check if the file is existing.
        $exists = Storage::disk('ftp')->exists($photo_file);

        if($exists){
            $file = Storage::disk('ftp')->get($photo_file);

            $fileName = $photo_file;
            return Response::make($file, '200', array(
                'Content-Type' => 'application/octet-stream',
                'Content-Disposition' => 'attachment; filename="'.$fileName.'"'
            ));
        }else{

            $res['res'] = "not_found";
            print json_encode($res,JSON_FORCE_OBJECT);
        }



    }
}

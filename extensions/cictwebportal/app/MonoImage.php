<?php
/**
 * Created by PhpStorm.
 * User: Jhon Melvin
 * Date: 11/05/2017
 * Time: 00:54
 */

namespace App;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use Image;

class MonoImage
{
    /**
     * @param Request $request
     * @param $file_id
     * @return bool
     */
    public static function getFileFromRequest(Request $request, $file_id)
    {
      //echo $file_id;
        if ($request->hasFile($file_id)) {
            return $request->file($file_id);
        } else {
            return false;
        }
    }

    /**
     * Save the file to the FTP and delete the file in the temp.
     *
     * @param $directory
     * @param $image_name
     * @param $image_file
     */
    public static function saveToFTP($directory, $image_name, $image_file)
    {
        Storage::disk('ftp')->put($directory . $image_name, $image_file);
        // delete from app/temp
        unlink(storage_path('app/temp/' . $image_name));
    }

    /**
     * Checks if the file is existing in the FTP directory.
     *
     * @param $directory String
     * @param $file_name String
     */
    public static function checkIfExist($directory, $file_name)
    {
        return Storage::disk('ftp')->exists($directory . $file_name);
    }


    /**
     * Resize and crop an image.
     *
     * @param $image
     * @param $temp_folder
     * @param $image_name
     * @return mixed
     */
    public static function resizeImage($image, $image_name)
    {
        // if not existing create
        Storage::disk('local')->makeDirectory('temp');
        // get temp path
        $temp_folder = storage_path('app/temp');
        // resize
        $img = Image::make($image->getRealPath());
        $img->save($temp_folder . "/" . $image_name);
        //get image width and height
        $image_info = getimagesize($temp_folder . "/" . $image_name);
        $image_width = $image_info[0];
        $image_height = $image_info[1];

        if($image_width > $image_height){
          $img_error = "image_rectangle";
          return $img_error;
        }else{
          if (($image_width == 160) and ($image_height == 160)) {
              //check if image is 160x160 pixels or 2x2 ata sabi sa crinop mo
          } else if (($image_width == $image_height) and ($image_height == $image_width)) {
              //check if image is a square
              $img->resize(160, 160, function ($constraint) {
                  $constraint->aspectRatio();
              })->save($temp_folder . "/" . $image_name);
          } else {
              //if rectangle na pahaba sa camera ng phone
              $img->resize(210, 215, function ($constraint) {
                  $constraint->aspectRatio();
              })->crop(160, 160, 0, 0)->save($temp_folder . "/" . $image_name);
          }
          return $img;
        }
    }
}

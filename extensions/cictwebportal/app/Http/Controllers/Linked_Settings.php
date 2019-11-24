<?php
namespace App\Http\Controllers;
use Illuminate\Http\Request;
use App\LinkedSetting;

class Linked_Settings extends Controller
{
  public function get_floor_name(){

    #------------------------------------------------------
    // get current room name of each floor
    $floor = LinkedSetting::where('active','=','1')
    ->first();
    if($floor){
      $request['result'] = 'true';
      $request['floor_3'] = $floor->floor_3_name;
      $request['floor_4'] = $floor->floor_4_name;
    }else{
      $request['result'] = 'false';
    }

    #------------------------------------------------------
    // send response
    echo json_encode($request, JSON_FORCE_OBJECT);
  }
}

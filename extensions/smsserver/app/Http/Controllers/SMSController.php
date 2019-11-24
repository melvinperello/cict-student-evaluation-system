<?php

namespace App\Http\Controllers;

use App\SMSQuery;
use Illuminate\Http\Request;

class SMSController extends Controller
{
    //

    public function addSMStoQuery(Request $request)
    {
        $post = $request->all();
        $number = $post['number'];
        $message = $post['message'];
        $sender = $post['sender'];

        $sms = new SMSQuery;
        $sms->reciepients_number = $number;
        $sms->message_body = $message;
        $sms->sender_name = $sender;

        $result = $sms->save();

        if($result){
            $reply['status'] = "SENDING";
            print json_encode($reply,JSON_FORCE_OBJECT);
        }else{
            $reply['status'] = "FAILED";
            print json_encode($reply,JSON_FORCE_OBJECT);
        }
    }
}

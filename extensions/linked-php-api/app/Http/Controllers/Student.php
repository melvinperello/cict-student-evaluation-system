<?php

namespace App\Http\Controllers;

use App\Announcement;
use Illuminate\Http\Request;
use App\LinkedPila;
use App\LinkedFloorFour;
use App\LinkedFloorThree;

class Student extends Controller
{
    //--------------------------------------------------------------------------
    public function check_number($cict_id)
    {
        #------------------------------------------------------------------
        # Save student number this is CICT_ID PK not the Actual Student Number
        $id = $cict_id;
        #------------------------------------------------------------------
        # check for pila information
        # only non void should be fetch
        $student_pila = LinkedPila::where('active', '1')
            ->where('status', '!=', 'VOID')
            ->where('STUDENT_id', $id)
            ->orderBy('id', 'DESC')
            ->first();
        #------------------------------------------------------------------
        if (!$student_pila) {
            # if the student is not in line
            // request yung nagamit ko
            return response()->json(['pila_status' => 'no']);
        }
        #------------------------------------------------------------------
        # if the student is in line get floor assignment
        $floor_assignment = $student_pila->floor_assignment;
        # Get current number that is called in a particular cluster
        if ($floor_assignment == '3') {
            $current_called = $this->get_current('3');
        } else if ($floor_assignment == '4') {
            $current_called = $this->get_current('4');
        } else {
            $current_called = 'Wait...';
        }
        #------------------------------------------------------------------
        # Get Current Announcement
        $announcement = Announcement::where('active', '1')
            ->orderBy('id', 'desc')
            ->first();
        # Write Announcement
        if ($announcement) {
            try {
                $announce = strtoupper($announcement->title) . ' - ' . $announcement->message;
                $announceTitle = $announcement->title;
            } catch (\Exception $e) {
                $announce = "";
                $announceTitle = "No Announcement";
            }
        } else {
            $announce = "";
            $announceTitle = "No Announcement";
        }
        #------------------------------------------------------------------
        $reply['pila_status'] = "yes";
        $reply['pila_info'] = $student_pila;
        $reply['called'] = $current_called;
        $reply['announcement'] = $announce;
        $reply['title'] = $announceTitle;


        return response()->json($reply);
    }

    //--------------------------------------------------------------------------

    /**
     * Get the current called number in a particular cluster (floor).
     * @param $floor
     * @return mixed|string
     */
    private function get_current($floor)
    {
        #------------------------------------------------------------------
        # Query to get the latest called number in a particular cluster
        $pila = LinkedPila::where('active', '1')
            ->where('status', 'CALLED')
            ->where('floor_assignment', $floor)
            ->orderBy('id', 'DESC')
            ->first();
        # If No one is called.
        if (!$pila) {
            return "Wait...";
        }

//        #------------------------------------------------------------------
//        #Refractored fixed assignment to equality = -> == @date 09/01/2017
//        if ($floor == '3') {
//            $floor_info = LinkedFloorThree::where('pila_id', $pila->id)
//                ->first();
//        } else if ($floor == '4') {
//            $floor_info = LinkedFloorFour::where('pila_id', $pila->id)
//                ->first();
//        }
        return $pila->floor_number;
    }

    //------------------------------------------------------------------

    public function cancel_reference(Request $request)
    {
        $post = $request->all();
        $pila_id = $post['pila_id'];

        $pila_reference = LinkedPila::where('id', $pila_id)
            ->first();

        $pila_reference->status = "VOID";
        $res = $pila_reference->save();

        if ($res) {
            $reply['res'] = "ok";
            echo json_encode($reply, JSON_FORCE_OBJECT);
        } else {
            $reply['res'] = "failed";
            echo json_encode($reply, JSON_FORCE_OBJECT);
        }

    }

}

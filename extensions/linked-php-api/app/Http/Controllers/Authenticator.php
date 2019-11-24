<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\AccountStudent;
use App\Student;
use App\LinkedPila;
use App\LinkedMarshallSession;
use App\Curriculum;
use App\AcademicProgram;
use App\AcademicTerm;

use Carbon\Carbon;

# this class manages the authentication method for users
class Authenticator extends Controller
{
    //

    private $username;
    private $password;
    private $imei_used;
    private $account_model;
    private $student_model;
    //
    private $is_login_skipped;
    #-------------------------------------------------------------------------------
    /**
     * checks whether this phone is being used. for students it checks whether
     * the status is not = void.
     * @param $imei the imei of the phone
     */
    public function is_used($imei)
    {

        # checks in the marshall session if it's exist
        $marshall_session = LinkedMarshallSession::where('imei', $imei)
            ->where('active', 1)
            ->where('status', 'ONLINE')
            ->where('session_end', null)
            ->orderBy('ses_id', 'DESC')
            ->first();

        # if there is a marshal session in used
        if ($marshall_session) {
            $is_used['using'] = "YES";
            $is_used['type'] = "MARSHALL";
            $is_used['imei'] = $marshall_session->imei;
            $is_used['acc_id'] = $marshall_session->account_id;
            $is_used['session_id'] = $marshall_session->ses_id;
            echo json_encode($is_used);
            exit();
        }

        # if not check in the pila table if a student is using it.
        $linked_pila = LinkedPila::where('imei', $imei)
            ->where('active', 1)
            ->where('status', '!=', 'VOID')
            ->orderBy('id', 'DESC')
            ->first();

        # if there is a student that is using the phone
        if ($linked_pila) {
            $is_used['using'] = "YES";
            $is_used['type'] = "STUDENT";
            $is_used['imei'] = $linked_pila->imei;
            $is_used['acc_id'] = $linked_pila->ACCOUNT_id;
            $is_used['session_id'] = $linked_pila->id;
            echo json_encode($is_used);
            exit();
        }

        # if not the phone is not in use the user can login
        $is_used['using'] = "NO";
        echo json_encode($is_used);
    }

    /**
     * Marshal Logout
     * @param $account_id
     * @param $type
     */
    public function logout(Request $request /*$account_id, $type*/)
    {
        $post = $request->all();
        $account_id = $post['acc_id'];
        $type = $post['access'];

        if ($type == "ORGANIZATIONAL") {
            $time_now = Carbon::now();
            $time_now->timezone = 'Asia/Taipei';
            # log out query
            $res = LinkedMarshallSession::where('account_id', $account_id)
                ->where('active', 1)
                ->where('status', 'ONLINE')
                ->where('session_end', null)
                ->orderBy('ses_id', 'DESC')
                ->update(['status' => 'OFFLINE', 'session_end' => $time_now]);
            # check if logout is successfull
            if ($res) {
                $logout['status'] = "org_log_out";
            } else {
                $logout['status'] = "org_log_out_failed";
            }
            echo json_encode($logout, JSON_FORCE_OBJECT);
        }
    }

    #-------------------------------------------------------------------------------
    /*
    invoked function for route linked/login
    returns an associative array auth
    as of July 16, 2017
    it supports the following
    - blocked | if the user is blocked
    - is_active | is in used
    - wrong_password | not match
    - not_exist | not exist
    - success | to know if it's bypassed or normal check $this->is_logged_skip;

    Check first the state before anything else
    */
    # recieve the account details
    public function login(Request $request/*$username, $password, $imei, $is_skipped*/)
    {
        $post = $request->all();
        //$post = $request->all();
        $this->username = $post['username']; //$username;
        $this->password = $post['password']; //$password;
        $this->imei_used = $post['imei']; //$imei;
        $this->is_login_skipped = $post['skipped']; //$is_skipped;
        $this->verify_user();
    }
    #-------------------------------------------------------------------------------
    /*
    verifies the account
    */
    # verifies if the account is existing
    private function verify_user()
    {
        #
        # when logged in is skipped
        // search for the account
        if ($acc_id = $this->is_login_skipped) {
            # use account_id for login bypass
            $this->account_model = AccountStudent
                ::where('id', $acc_id)
                ->where('active', 1)
                ->first();

            ## skips other filters and exit script
            ## will also bypass if the account is already blocked
            ## until there is an online session
            ## but will not allow further login from other application
            $this->allow("SKIP");

        } else {
            # when not skipped
            # use username for normal login
            $this->account_model = AccountStudent
                ::where('username', $this->username)
                ->where('active', 1)
                ->first();
        }

        # if the account is existing
        if ($this->account_model) {
            if ($blocked_until = $this->is_blocked()) {
                $auth['state'] = "blocked";
                $auth['blocked_until'] = $blocked_until;
                echo json_encode($auth, JSON_FORCE_OBJECT);
                exit(); # exits the script
            }

            if ($this->password == $this->account_model->password) {
                # filter if already login
                # can only filter marshalls login and students with pila session
                # accounts that are not binded can login maultiple times
                $acc_id = $this->account_model->id;
                $access = $this->account_model->access_level;
                if ($this->is_logged_in($acc_id, $access)) {
                    $auth['state'] = "is_active";
                    echo json_encode($auth, JSON_FORCE_OBJECT);
                    exit();
                }

                // success login here
                $this->allow("NORMAL");
            } else {
                # wrong password
                $auth['state'] = "wrong_password";
                echo json_encode($auth, JSON_FORCE_OBJECT);
                $this->wrong_attempt();
            }
        } else {
            # account not existing
            $auth['state'] = "not_exist";
            echo json_encode($auth, JSON_FORCE_OBJECT);
        }

    } # verify ends

    #-------------------------------------------------------------------------------
    /*
    if the user tries to logged in to other devices while an active session is in place
    this will deny that request
    $account_id - must be passed
    */
    private function is_logged_in($account_id, $access_level)
    {
        ## checks for existence in pila table
        if ($access_level == "STUDENT") {
            $linked_pila = LinkedPila::where('ACCOUNT_id', $account_id)
                ->where('active', 1)
                ->where('status', '!=', 'VOID')
                ->orderBy('id', 'DESC')
                ->first();

            ## from pila
            if ($linked_pila) {
                return 1;
            } else {
                return 0;
            }


        } else if ($access_level == "ORGANIZATIONAL") {
            ## search marshall_session
            $marshall_session = LinkedMarshallSession::where('account_id', $account_id)
                ->where('active', 1)
                ->where('status', 'ONLINE')
                ->where('session_end', null)
                ->orderBy('ses_id', 'DESC')
                ->first();

            # from marhsall session
            if ($marshall_session) {
                return 1;
            } else {
                return 0;
            }

        }


    }
    #-------------------------------------------------------------------------------
    /*
    this is the success reply
    $skip = "SKIP" means that the logged in is done by bypass
    $skip = "NORMAL" means that the user used normal logged in method
    */
    private function allow($skip)
    {
        $auth['state'] = "success";
        $auth['account_id'] = $this->account_model->id;
        $auth['username'] = $this->account_model->username;
        $auth['access'] = $this->account_model->access_level;
        $auth['affiliates'] = $this->account_model->affiliates;
        # get student information
        $this->student_model = Student
            ::where('cict_id', $this->account_model->STUDENT_id)
            ->where('active', 1)
            ->first();

        $auth['cict_id'] = $this->student_model->cict_id;
        $auth['student_number'] = $this->student_model->id;
        $auth['last_name'] = $this->student_model->last_name;
        $auth['first_name'] = $this->student_model->first_name;
        //------------
        $auth['middle_name'] = $this->student_model->middle_name;
        $auth['gender'] = $this->student_model->gender;
        $auth['year'] = $this->student_model->year_level;
        $auth['section'] = $this->student_model->section;
        $auth['group'] = $this->student_model->_group;
        //-------------
        $auth['has_profile'] = $this->student_model->has_profile;
        $auth['enrollment_type'] = $this->student_model->enrollment_type;
        $auth['admission_year'] = $this->student_model->admission_year;

        /*
        Additional information. @date: 08092017
        */
        // get student curriculum
        $curriculum = Curriculum
            ::where('id', $this->student_model->CURRICULUM_id)
            ->first();

        # 09.06.2017

        $course_name = "NOT SET";
        $course_code = "NOT SET";
        if($curriculum){
            $academic_program = AcademicProgram
                ::where('id', $curriculum->ACADPROG_id)
                ->first();
            $course_name = $academic_program->name;
            $course_code = $academic_program->code;
        }



        //continue results;
        $auth['course_name'] = $course_name;
        $auth['course_code'] = $course_code;

        //
        // get current academic term.
        $academic_term = AcademicTerm
            ::where('active', '1')
            ->where('current', '1')
            ->first();

        $auth['current_term'] = $academic_term->id;
        // incase this fields are not used by the app.
        $auth['current_sy'] = $academic_term->school_year;
        $auth['current_sem'] = $academic_term->semester;



        /*
        After setting up the reply reset the account counters
        */
        $this->account_model->state = 0;
        $this->account_model->tries = 0;
        $this->account_model->blocked_until = null;
        $this->account_model->save();

        if ($skip == "SKIP") {
            // does not create new marshall session
        } else if ($skip == "NORMAL") {
            $this->create_marshall_session();
        }

        echo json_encode($auth, JSON_FORCE_OBJECT);
        exit(); # exit the script
    }

    #-------------------------------------------------------------------------------
    /*
    call only inside $this->allow();
    this is called if the user used the normal authentication method
    this will create a new session under marshall_session
    if $this->allow("SKIP") was called this function will not be invoked.
    */
    private function create_marshall_session()
    {
        # if the account is organization create session
        if ($this->account_model->access_level == "ORGANIZATIONAL") {
            # create marshall session
            $marshall_session = new LinkedMarshallSession();
            $marshall_session->cict_id = $this->student_model->cict_id;
            $marshall_session->account_id = $this->account_model->id;
            $marshall_session->name = $this->student_model->last_name
                . " "
                . $this->student_model->first_name
                . " "
                . $this->student_model->middle_name;
            $marshall_session->org = $this->account_model->affiliates;
            $marshall_session->imei = $this->imei_used;
            $session_start = Carbon::now();
            $session_start->timezone = 'Asia/Taipei';
            $marshall_session->session_start = $session_start;
            $marshall_session->save();
        }
    }

    #-------------------------------------------------------------------------------
    /*
    The user has 3 attempts to enter a wrong password
    on the third attempt the user will be blocked for 15mins
    */

    # manages wrong attemps
    private function wrong_attempt()
    {
        # get wrong attempts
        $wrong_attempts = $this->account_model->tries;
        # after 3 tries
        if ($wrong_attempts > 1) {
            $time_now = Carbon::now();
            $time_now->timezone = 'Asia/Taipei';
            //$mytime->toDateTimeString();
            # block user for 15 minutes
            $time_now->addMinutes(15);
            $this->account_model->blocked_until = $time_now;
            $this->account_model->tries = 0;
        } else {
            // increment
            $wrong_attempts++;
            $this->account_model->tries = $wrong_attempts;
        }
        $this->account_model->save();
    }

    #-------------------------------------------------------------------------------
    /*
    if the user has an assigned blocked_until due to incorrect attempts
    this class will compare if the time is already lapsed
    if the time is not yet lapsed the time until block expires
    */
    # checks block status of user
    private function is_blocked()
    {
        if ($this->account_model->blocked_until) {
            // current time
            $time_now = Carbon::now();
            $time_now->timezone = 'Asia/Taipei';

            // blocked until time
            $blocked_until = Carbon::parse($this->account_model->blocked_until, 'Asia/Taipei');

            if ($blocked_until->gt($time_now)) {
                # user is blocked
                return $blocked_until->format('h:i:s A');
            }
        }
        return 0; // not blocked
    } # end of isBlocked

} # class ends

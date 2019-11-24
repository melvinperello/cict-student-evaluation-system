<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class StudentProfile extends Model
{
    public $table = 'student_profile';
    public $primaryKey = 'STUDENT_id';
    public $timestamps = false;
}

<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Student extends Model
{
    //
    public $table = 'student';
    public $timestamps = false;
    public $primaryKey = 'cict_id'; //id always
}

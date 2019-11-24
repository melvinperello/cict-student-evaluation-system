<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Student extends Model
{
    //
    public $table = 'student';
    public $primaryKey = 'cict_id';
    public $timestamps = false;
}

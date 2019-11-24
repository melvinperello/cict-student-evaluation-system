<?php

namespace App\Http\Controllers;
use Illuminate\Http\Request;

class Error extends Controller
{
  public function show_status_error($status){
    return view('errors.status_error',['status' => $status]);
  }

  public function show_404(){
    return view('errors.404');
  }

  public function show_disconnected(){
    return view('errors.disconnected');
  }
}

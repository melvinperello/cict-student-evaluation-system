<?php

namespace App\Http\Middleware;

use Closure;

class Authenticator
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        // middleware action here
        if(session()->has('SES_AUTHENTICATED')){
          if(session()->get('SES_AUTHENTICATED') == 'YES'){
            // user is authenticated
            // may proceed to request

          }else{
            return redirect()->route('profile-get');
          }
        }else{
           return redirect()->route('home');
        }

        // proceed request
        return $next($request);


    }
}

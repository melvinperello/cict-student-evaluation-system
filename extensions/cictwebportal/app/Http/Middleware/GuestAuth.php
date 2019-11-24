<?php

namespace App\Http\Middleware;

use Closure;

class GuestAuth
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

      if(session()->has('SES_AUTHENTICATED')){
        if(session()->get('SES_AUTHENTICATED') == 'YES'){

          return redirect()->route('profile');

        }

        return redirect()->route('profile');

      }else{
        // if not authenticated will proceed to the requested route
      }
        // this is the requested route
        return $next($request);
    }
}

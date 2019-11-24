<?php
/*
Monosync Studio QR CODE Client
returns an image that can be used directly
*/
include('phpqrcode/qrlib.php');
$content = $_GET['content'];
$content = str_replace('_','#',$content);
QRcode::png($content,null,QR_ECLEVEL_Q);
?>

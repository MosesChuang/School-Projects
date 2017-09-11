<?php
require_once("model/Image.php");
Class ImageController {
    
    function show($params) {
        $id = $params[0];
        $view = "view";
        $image = new Image($id);
        #header("Location:$view/Image_Detail.php?id=$id");
        include_once("$view/Image_Detail.php");
    }
    
    function browse() {
        #$id = $params[0];
        $view = "view";
        #$image = new Image($id);
        #header("Location:$view/Image_Detail.php?id=$id");
        include_once("$view/Image_Browse.php");
    }
}
?>
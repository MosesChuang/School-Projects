<?php
include_once 'controller/ImageController.php';

$path = ltrim($_SERVER['REQUEST_URI'], '/');    // Trim leading slash(es)
$elements = explode('/', $path);                // Split path on slashes
array_shift($elements);
switch(array_shift($elements)) {
    case 'image':
        $controller = new ImageController();
        break;
    default:
        break;
}

$action = array_shift($elements);
$params = $elements;

$controller->$action($params);


?>

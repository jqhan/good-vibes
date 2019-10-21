<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 
// get database connection
include_once 'config/database.php';
 
include_once 'game.php';
 
$database = new Database();
$db = $database->getConnection();
 
$game = new Game($db);
 
// get posted data
$data = json_decode(file_get_contents("php://input"));

// make sure data is not empty
if(
    !is_null($data->sesh_id) &&
    !is_null($data->game_num) &&
    !is_null($data->win) &&
    !is_null($data->vibe) &&
    !is_null($data->playtillose) 
){
 
    // set product property values
    $game->sesh_id = $data->sesh_id;
    $game->game_num = $data->game_num;
    $game->win = $data->win;
    $game->vibe = $data->vibe;
    $game->playtillose = $data->playtillose;
 
    // create the product
    if($game->create()){
 
        // set response code - 201 created
        http_response_code(201);
 
        // tell the user
        echo json_encode(array("message" => "Game was created."));
    }
 
    // if unable to create the product, tell the user
    else{
 
        // set response code - 503 service unavailable
        http_response_code(503);
 
        // tell the user
        echo json_encode(array("message" => "Unable to create game."));
    }
}
 
// tell the user data is incomplete
else{
 
    // set response code - 400 bad request
    http_response_code(400);
 
    // tell the user
    echo json_encode(array("message" => "Unable to create game. Data is incomplete."));
}
?>

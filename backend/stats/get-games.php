<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 
include_once '../api/config/database.php';
 
 
$db = new Database();
$conn = $db->getConnection();

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}  

$query = "SELECT sesh_id, game_num, win, vibe, playtillose, date FROM rocke ORDER BY sesh_id";
$stmt = $conn->prepare($query);

$stmt->execute();

$result = $stmt;

$data = array();
foreach ($result as $row) {
	$data[] = $row;
}


echo json_encode($data);
?>

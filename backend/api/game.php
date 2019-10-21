<?php

class Game{

	private $conn;
	private $table_name = "";

	// object properties
	public $sesh_id;
	public $game_num;
	public $win;
	public $vibe;
	public $playtillose;

	public function __construct($db){
		$this->conn = $db;
	}

	function create(){
	 
	    $query = "INSERT INTO
			" . $this->table_name . "
		    SET
			sesh_id=:sesh_id, game_num=:game_num, win=:win, vibe=:vibe, playtillose=:playtillose";
	 
	    $stmt = $this->conn->prepare($query);
	 
	    // sanitize
	    $this->sesh_id=htmlspecialchars(strip_tags($this->sesh_id));
	    $this->game_num=htmlspecialchars(strip_tags($this->game_num));
	    $this->win=htmlspecialchars(strip_tags($this->win));
	    $this->vibe=htmlspecialchars(strip_tags($this->vibe));
	    $this->playtillose=htmlspecialchars(strip_tags($this->playtillose));
	 
	    // bind values
	    $stmt->bindParam(":sesh_id", $this->sesh_id);
	    $stmt->bindParam(":game_num", $this->game_num);
	    $stmt->bindParam(":win", $this->win);
	    $stmt->bindParam(":vibe", $this->vibe);
	    $stmt->bindParam(":playtillose", $this->playtillose);
	 
	    if($stmt->execute()){
		return true;
	    }
	 
	    return false;
	}
}
?>


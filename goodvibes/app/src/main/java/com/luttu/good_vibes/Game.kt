package com.luttu.good_vibes

import kotlinx.serialization.Serializable

@Serializable
class Game(val sesh_id: String, val game_num: Int, val win: Int, val vibe: Int, val playtillose: Int)
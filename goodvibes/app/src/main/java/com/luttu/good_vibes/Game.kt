package com.luttu.good_vibes

import kotlinx.serialization.Serializable

@Serializable
class Game(val seshID: String, val gameNum: Int, val win: Int, val vibe: Int, val playtillose: Int)
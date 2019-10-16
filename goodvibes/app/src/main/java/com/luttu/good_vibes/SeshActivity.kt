package com.luttu.good_vibes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_sesh.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list

class SeshActivity : AppCompatActivity() {

    private lateinit var mSesh: Sesh

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sesh)

        val sharedPreferencesSesh = getSharedPreferences("PREFERENCE_SESH", Context.MODE_PRIVATE)
        mSesh = Sesh(sharedPreferencesSesh)
        sliderVibe.progress = 5;
        textSesh.text = "Sesh " + mSesh.getSeshCount()

        sliderVibe.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                textVibeValue.text = "Vibe : $i"
            }
        })

        btnSaveGame.setOnClickListener {
            saveGameDataBtnPressed()
        }

        btnEndSesh.setOnClickListener {
            endSeshBtnPressed()
        }

    }

    private fun createGame(): Game {

        fun Boolean.toInt() = if (this) 1 else 0

        val win: Int = switchWin.isChecked.toInt()
        val vibe: Int = sliderVibe.progress
        val playtillose: Int = switchPlayTilLose.isChecked.toInt()
        return Game(mSesh.getSeshId(), mSesh.getGameCount(), win, vibe, playtillose)
    }

    private fun saveGameDataBtnPressed() {
        mSesh.saveGame(createGame())
        textGameNum.text = "Game " + mSesh.getGameCount()
        Toast.makeText(this, "Game data sent!", Toast.LENGTH_SHORT).show()
        Log.d("Sesh", mSesh.getSeshId())
    }

    private fun endSeshBtnPressed() {
        mSesh.endSesh()
        postGames()
        val intent = MainActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun postGames() {

        val json = Json(JsonConfiguration.Stable)

        val jsonData = json.stringify(Game.serializer(), mSesh.getGameList().get(0))

        val jsonList = json.stringify(Game.serializer().list, mSesh.getGameList())

    }

    companion object {

        fun newIntent(context: Context, serverURL: String): Intent {
            val intent = Intent(context, SeshActivity::class.java)
            intent.putExtra("url", serverURL)
            return intent
        }
    }
}


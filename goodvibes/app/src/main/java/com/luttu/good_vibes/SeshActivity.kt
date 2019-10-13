package com.luttu.good_vibes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_sesh.*

class SeshActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sesh)
        vibeSlider.progress = 5;

        vibeSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                vibeValue.text = "Vibe : $i"
            }
        })

    }

    companion object {

        fun newIntent(context: Context, serverURL: String): Intent {
            val intent = Intent(context, SeshActivity::class.java)
            intent.putExtra("url", serverURL)
            return intent
        }
    }
}


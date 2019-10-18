package com.luttu.good_vibes

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import android.view.Window
import android.widget.SeekBar
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sesh.*
import kotlinx.android.synthetic.main.dialog_transparent.*


class SeshActivity : AppCompatActivity() {

    private lateinit var mSesh: Sesh
    private lateinit var mEndpointBaseURL: String
    private lateinit var mDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sesh)
        this.title = Html.fromHtml("<font color=\"black\">good vibes :^)</font>", 0);

        mEndpointBaseURL = intent.getStringExtra("url")

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
        val factor = 10
        val win: Int = switchWin.isChecked.toInt() * factor
        val vibe: Int = sliderVibe.progress
        val playtillose: Int = switchPlayTilLose.isChecked.toInt() * factor
        return Game(mSesh.getSeshId(), mSesh.getGameCount(), win, vibe, playtillose)
    }

    private fun saveGameDataBtnPressed() {
        mSesh.saveGame(createGame())
        textGameNum.text = "Game " + mSesh.getGameCount()
        Toast.makeText(this, "Saved game", Toast.LENGTH_SHORT).show()
        Log.d("Sesh", mSesh.getSeshId())
        switchPlayTilLose.isChecked = false
    }

    private fun endSeshBtnPressed() {
        mSesh.endSesh()
        showLoading()
//        changeDialogLayoutToLoading()
        postGames()
        changeDialogLayoutToDone()
        hideLoading()
    }


    fun showLoading(){
        mDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_transparent)
        mDialog.btnCancelDone.setOnClickListener{
            cancelDoneBtnPressed()
        }
        mDialog.show()
    }


    fun hideLoading(){
        mDialog.hide()
    }

    private fun cancelDoneBtnPressed() {
        VibeUtils.hideLoading()
        finishActivity()
    }

    private fun postGames() {
        mSesh.getGameList().forEach { game ->
            postGameCall(game)
        }
    }

    private fun finishActivity() {
        val intent = MainActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    private fun changeDialogLayoutToLoading() {
        textLoadingStatus.text = "Sending sesh data.."
        btnCancelDone.text = "Cancel"
    }

    private fun changeDialogLayoutToDone() {
        textLoadingStatus.text = "Sesh data has successfully been sent"
        btnCancelDone.text = "OK"
    }

    @SuppressLint("CheckResult")
    private fun postGameCall(game: Game) {
        if (VibeUtils.isConnectedToInternet(this)) {
            val observable = ApiService(mEndpointBaseURL).postGameAPICall().doPostGame(game)
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { error ->
                    VibeUtils.showLongToast(this, error.message.toString())
                }
                )
        } else {
            VibeUtils.showLongToast(this, "No Internet Connection!")
        }
    }

    companion object {

        fun newIntent(context: Context, serverURL: String): Intent {
            val intent = Intent(context, SeshActivity::class.java)
            intent.putExtra("url", serverURL)
            return intent
        }
    }
}


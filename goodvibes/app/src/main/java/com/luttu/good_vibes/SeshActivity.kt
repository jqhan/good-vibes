package com.luttu.good_vibes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.View
import android.widget.SeekBar
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_sesh.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class SeshActivity : AppCompatActivity() {

    private lateinit var mSesh: Sesh
    private lateinit var mEndpointBaseURL: String
    private var mTimerStartTime: Long = System.currentTimeMillis()

    @SuppressLint("CheckResult")
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

        btnUndoGame.setOnClickListener {
            undoGameBtnPressed()
        }

        recyclerViewGameResults.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = GameResultAdapter(mSesh.getGameList())
        }

        Flowable.interval(1000L, TimeUnit.MILLISECONDS)
            .timeInterval()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                textStopWatchTime.text = getStopWatchString()
            }

    }

    fun showHideGameResultRVSymbols(visibility: Int) {
        textViewResultsGameNum.visibility = visibility
        textViewResultsWinLose.visibility = visibility
        imageViewMiky.visibility = visibility
    }


    fun getStopWatchString(): String {
        val nowTime = System.currentTimeMillis()
        val cast = nowTime - mTimerStartTime
        val date = Date(cast)
        val simpleDateFormat = SimpleDateFormat("mm:ss")
        return simpleDateFormat.format(date)
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
        switchPlayTilLose.isChecked = false
        mTimerStartTime = System.currentTimeMillis()
        updateUI()
    }

    private fun endSeshBtnPressed() {
        mSesh.endSesh()
        postGames()
        val handler = Handler()
        handler.postDelayed(Runnable {
            val intent = MainActivity.newIntent(this)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun undoGameBtnPressed() {
        mSesh.removeLastAddedGame()
        updateUI()
    }

    private fun updateUI() {
        textGameNum.text = "Game " + mSesh.getGameCount()
        recyclerViewGameResults.adapter?.notifyDataSetChanged()
        if (mSesh.getGameList().isEmpty()) {
            showHideGameResultRVSymbols(View.GONE)
            textAverageVibe.visibility = View.GONE
        } else {
            showHideGameResultRVSymbols(View.VISIBLE)
            textAverageVibe.visibility = View.VISIBLE
            val df = DecimalFormat("##.##")
            df.roundingMode = RoundingMode.CEILING
            textAverageVibe.text = "average vibe: " + df.format(mSesh.getAverageVibe())
        }
    }

    private fun postGames() {
        VibeUtils.showLoading(this)
        mSesh.getGameList().forEach { game ->
            postGameCall(game)
        }
        VibeUtils.hideLoading()
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

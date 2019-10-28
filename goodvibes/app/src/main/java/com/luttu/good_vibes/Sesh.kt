package com.luttu.good_vibes

import android.content.SharedPreferences
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Sesh(sharedPreferencesSesh: SharedPreferences) {

    private val mSeshID: String // "2019/10/14-1" etc
    private val mSharedPreferencesSesh: SharedPreferences = sharedPreferencesSesh
    private val mDate: String
    private var mSeshCount: Int // sesh number for the day
    private var mGameList = ArrayList<Game>()

    init {
        mDate = DATE_FORMAT.format(Date())
        mSeshCount = mSharedPreferencesSesh.getInt(mDate, 0)
        if (mSeshCount == 0) {
            clearOldSessions()
        }
        mSeshCount++

        mSeshID = mDate + "-" + mSeshCount
    }

    fun clearOldSessions() {
       mSharedPreferencesSesh.edit().clear().apply()
    }

    fun removeLastAddedGame() {
        mGameList.removeAt(mGameList.size-1)
    }

    fun endSesh() {
        var editor = mSharedPreferencesSesh.edit()
        editor.putInt(mDate, mSeshCount)
        editor.apply()
    }

    fun saveGame(game: Game) {
        mGameList.add(game)
    }

    fun getSeshCount(): Int {
        return mSeshCount
    }

    fun getGameCount(): Int {
        return mGameList.size + 1
    }

    fun getSeshId(): String {
        return mSeshID
    }

    fun getGameList(): List<Game> {
        return mGameList
    }

    companion object {
        val DATE_FORMAT = SimpleDateFormat("yyyy/MM/dd")
    }
}

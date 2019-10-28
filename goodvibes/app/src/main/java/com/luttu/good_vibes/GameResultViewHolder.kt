package com.luttu.good_vibes

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


class GameResultViewHolder(inflater: LayoutInflater, private val parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.game_result_item, parent, false)) {
    private var mTextViewGameNum: TextView? = null
    private var mTextViewVibe: TextView? = null
    private var mImageViewWinLose: ImageView? = null
    private var mTextViewPlayTilLose: TextView? = null

    init {
        mTextViewGameNum = itemView.findViewById(R.id.textItemGameNum)
        mTextViewVibe = itemView.findViewById(R.id.textItemGameVibe)
        mImageViewWinLose = itemView.findViewById(R.id.imageItemWinLose)
        mTextViewPlayTilLose = itemView.findViewById(R.id.textItemP2L)
    }

    fun bind(game: Game) {
        mTextViewGameNum?.text = game.game_num.toString()
        mTextViewVibe?.text = game.vibe.toString()
        mImageViewWinLose?.setImageDrawable(getSuitableDrawable(game))
        fixPaddingForTwoDigitNumbers(game)
        showPlayTilLoseIfNeeded(game)
    }

    private fun getSuitableDrawable(game: Game): Drawable? {
        return if (game.win == 10) {
            ContextCompat.getDrawable(parent.context, R.drawable.win)
        } else {
            ContextCompat.getDrawable(parent.context, R.drawable.lose)
        }
    }

    private fun fixPaddingForTwoDigitNumbers(game: Game) {
        val singleDigitMargin = dpToPx(6F)
        val doubleDigitMargin = dpToPx(1.5F)
        val paramTextViewGameNum =
            mTextViewGameNum?.layoutParams as LinearLayout.LayoutParams
        val paramTextViewVibe =
            mTextViewVibe?.layoutParams as LinearLayout.LayoutParams

        paramTextViewGameNum.setMargins(
            if (game.game_num < 10) singleDigitMargin else doubleDigitMargin,
            0, 0, 0
        )
        mTextViewGameNum!!.layoutParams = paramTextViewGameNum

        paramTextViewVibe.setMargins(
            if (game.vibe < 10) singleDigitMargin else doubleDigitMargin,
            0, 0, 0
        )
        mTextViewVibe!!.layoutParams = paramTextViewVibe
    }

    private fun showPlayTilLoseIfNeeded(game: Game) {
        if (game.playtillose == 10) {
            mTextViewPlayTilLose!!.visibility = VISIBLE
        } else {
            mTextViewPlayTilLose!!.visibility = GONE
        }
    }

    private fun dpToPx(dp: Float): Int {
        val r: Resources = parent.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            r.displayMetrics
        ).toInt()
    }

}


package com.luttu.good_vibes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.webkit.URLUtil
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.title = Html.fromHtml("<font color=\"black\">good vibes :^)</font>", 0);
        val sharedPreferencesAPI = getSharedPreferences("PREFERENCE_API", Context.MODE_PRIVATE)
        val savedURL = sharedPreferencesAPI.getString("api_base_url", "")

        if (savedURL != null && savedURL != "") {
            editTextEndpoint.setText(savedURL)
        }


        btnStartSesh.setOnClickListener {
            val url: String = editTextEndpoint.text.toString()

            if (URLIsFilledInAndValid(url)) {
                if (savedURL != url) {
                    saveURL(url, sharedPreferencesAPI)
                }
                val intent = SeshActivity.newIntent(this, url)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun URLIsFilledInAndValid(url: String): Boolean {
        return !url.isEmpty() && URLUtil.isValidUrl(url)
    }

    private fun saveURL(url: String, sharedPreference: SharedPreferences): Unit {
        val editor = sharedPreference.edit()
        editor.putString("api_base_url", url)
        editor.apply()
    }

    companion object {

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            return intent
        }
    }


}

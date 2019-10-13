package com.luttu.good_vibes

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreference =  getSharedPreferences("PREFERENCE_API", Context.MODE_PRIVATE)
        val savedURL= sharedPreference.getString("create", "Server URL")

        if (savedURL != null && savedURL != "Server URL") {
            serverURL.setText(savedURL)
        }


        btnStartSesh.setOnClickListener {
            val editTextServerURL = findViewById<EditText>(R.id.serverURL)
            val url: String = editTextServerURL.text.toString()

            if (serverURLIsFilledInAndValid(url)) {
                if ( savedURL != url) {
                    saveURLToDisk(url, sharedPreference)
                }
                val intent = SeshActivity.newIntent(this, url)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid server URL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun serverURLIsFilledInAndValid(url: String): Boolean {
        return !url.isEmpty() && URLUtil.isValidUrl(url)
    }

    private fun saveURLToDisk(url: String, sharedPreference: SharedPreferences): Unit {
        var editor = sharedPreference.edit()
        editor.putString("create", url)
        editor.commit()
    }


}

package com.jp.flutterintonative

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_data.*

class DataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        btnToSend.setOnClickListener {
            val inputData = etData.text.toString()
            etData.setText("")
            sendToFlutterModule(inputData)
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.extras?.getString("data")
                tvSetData.text = "Data From Flutter : $result"
            }
        }
    }

    private fun sendToFlutterModule(textOfAndroid: String) {
        MainActivity.startActivity(this, textOfAndroid)
    }
}

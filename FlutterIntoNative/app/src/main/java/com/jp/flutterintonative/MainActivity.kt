package com.jp.flutterintonative

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import io.flutter.facade.Flutter
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.FlutterView
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val flutterView: FlutterView = Flutter.createView(this, lifecycle, "gotoFlutter")
        addContentView(
            flutterView,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        MethodChannel(flutterView, CHANNEL).setMethodCallHandler { call, result ->
            // manage method calls here
            if (call.method == "FromClientToHost") {
                val resultStr = call.arguments.toString()
                val resultJson = JSONObject(resultStr)
                val res = resultJson.getString("result")

                val intent = Intent()
                intent.putExtra("data", res)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                result.notImplemented()
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }

        val data = intent?.extras?.getString("data")

        val json = JSONObject()
        json.put("data", data)

        Handler().postDelayed({
            MethodChannel(flutterView, CHANNEL).invokeMethod("fromHostToClient", json.toString())
        }, 500)


    }

    companion object {

        const val CHANNEL = "com.jp.flutterintonative/data"

        fun startActivity(context: DataActivity, data: String) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("data", data)
            context.startActivityForResult(intent, 100)
        }
    }
}

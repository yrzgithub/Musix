package com.android.musix

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

/*        var info = YTInfo("Mangatha theme")
        var handler = Handler(Looper.getMainLooper())

        info.apply {
           GlobalScope.launch {
               fetch()
               getStream()
               handler.post {
                   var play = Player(applicationContext,info)
                   play.play()
               }
           }
        }*/
    }
}
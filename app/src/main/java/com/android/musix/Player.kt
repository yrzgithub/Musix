package com.android.musix

import android.app.Application
import android.content.Context
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import java.util.logging.Handler
import java.util.logging.LogRecord

class Player(application : Application,context : Context) : AndroidViewModel(application) {

    var player : ExoPlayer

    init {
        println("Player Created")
        player = ExoPlayer.Builder(context).build()
    }

    fun play(info : YTInfo) : Unit
    {
        val mediaItem : MediaItem = MediaItem.fromUri(info.stream_url!!)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
        player.play()
    }

    override fun onCleared() {
        super.onCleared()
    }
}
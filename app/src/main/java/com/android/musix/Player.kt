package com.android.musix

import android.content.Context
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import java.util.logging.Handler
import java.util.logging.LogRecord

class Player(context : Context,info : YTInfo) {

    var player = ExoPlayer.Builder(context).build()
    val mediaItem : MediaItem = MediaItem.fromUri(info.stream_url!!)

    fun play() : Unit
    {
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
        player.play()
    }
}
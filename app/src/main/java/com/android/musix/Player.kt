package com.android.musix

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class Player(application : Application,context : Context) : AndroidViewModel(application) {

    var player : ExoPlayer

    init {
        println("Player Created")
        player = ExoPlayer.Builder(context).build()
    }

    fun setCurrentPosition(position : Int)
    {
        player.seekTo(position.toLong()*1000)
    }

    fun isPlaying() : Boolean {
        return player.isPlaying
    }

    fun play(info : YTInfo) : Unit
    {
        val mediaItem : MediaItem = MediaItem.fromUri(info.stream_url!!)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
        player.play()
    }

    fun play()
    {
        player.play()
    }

    fun forward()
    {
        player.seekTo(player.currentPosition + 3000)
    }

    fun backward()
    {
        player.seekTo(player.currentPosition - 3000)
    }

    fun pause()
    {
        player.pause()
    }

    fun getTotalDuration() : Int {
        return player.contentDuration.div(1000).toInt()
    }

    fun getBufferedDuration() : Int {
        return player.bufferedPosition.div(1000).toInt()
    }

    fun getCurrentPosition() : Int {
        return player.currentPosition.div(1000).toInt()
    }

    fun getDurationString(duration : Double) : String {
        val min = Math.round(duration/60)
        val sec = Math.round(duration%60)
        return String.format("%1d:%02d",min,sec)
    }

    fun getCurrentStringDuration() : String {
        return getDurationString(getCurrentPosition().toDouble())
    }

    fun getTotalStringDuration() : String {
        return getDurationString(getTotalDuration().toDouble())
    }

    override fun onCleared() {
        player.pause()
        player.stop()
        player.release()
        super.onCleared()
    }
}
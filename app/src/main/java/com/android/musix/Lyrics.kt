package com.android.musix

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider


class Lyrics : Fragment() {

    val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lyrics, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val lyricsList = view.findViewById<ListView>(R.id.lyrics)

        val lyrics : List<Map<String,String?>> = YTInfo.lyrics!!

        val lyricsAdapter = LyricsAdapter(requireContext(),lyrics)

        lyricsList.adapter = lyricsAdapter

        val player = ViewModelProvider(requireActivity())[Player::class.java]

       /* handler.postDelayed(object : Runnable {
            override fun run() {

                val lyric : Map<String, String?>? = lyrics.find {

                    val position = player.getCurrentPosition()

                    val dur = it["dur"]?.toInt()!!

                    val start = it["start"]?.toInt()!!
                    val end = start + dur

                    position in start..end
                }

                println(lyric)

                handler.postDelayed(this,lyric!!["dur"]!!.toLong())
            }
        },0)*/

        super.onViewCreated(view, savedInstanceState)
    }
}

class LyricsAdapter(val context : Context,val lyrics : List<Map<String,String?>>) : BaseAdapter() {

    override fun getCount(): Int {
        return lyrics.size
    }

    override fun getItem(p0: Int): Any {
        return lyrics[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view : TextView = if(p1==null) TextView(context) else p1 as TextView
        view.text = lyrics[p0]["line"]
        return view
    }

}
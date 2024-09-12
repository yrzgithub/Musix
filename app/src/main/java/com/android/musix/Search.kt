package com.android.musix

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.os.postDelayed
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerControlView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Search : Fragment(),Listener,OnSeekBarChangeListener {

    lateinit var listView : ListView
    lateinit var thumbnail : ImageView
    lateinit var title : TextView
    lateinit var favourite : ImageButton
    lateinit var play : Player
    lateinit var sheetTitle : TextView
    lateinit var sheetThumb : ImageView
    lateinit var sheetSeek : SeekBar
    lateinit var sheetEnd: TextView
    lateinit var sheetStart: TextView
    lateinit var playBtn : ImageButton
    lateinit var forwardBtn : ImageButton
    lateinit var backwardBtn : ImageButton

    var seekHandler : Handler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val layout = view.findViewById<LinearLayout>(R.id.layout)
        val search = view.findViewById<SearchView>(R.id.search)
        listView = view.findViewById<ListView>(R.id.searchList)

        val controls = activity?.findViewById<LinearLayout>(R.id.controls)
        thumbnail = activity?.findViewById<ImageView>(R.id.thumbnail)!!
        title = activity?.findViewById<TextView>(R.id.title)!!
        favourite = activity?.findViewById<ImageButton>(R.id.favourite)!!
        sheetEnd = activity?.findViewById<TextView>(R.id.end)!!
        sheetStart = activity?.findViewById<TextView>(R.id.start)!!
        playBtn = activity?.findViewById<ImageButton>(R.id.play)!!
        forwardBtn = activity?.findViewById<ImageButton>(R.id.forward)!!
        backwardBtn = activity?.findViewById<ImageButton>(R.id.backward)!!

        val bottom_view = requireActivity().findViewById<LinearLayout>(R.id.bottom_sheet)
        sheetThumb = bottom_view.findViewById(R.id.thumb)
        sheetTitle = bottom_view.findViewById(R.id.title)
        sheetSeek = bottom_view.findViewById(R.id.seek)

        title.isSelected = true
        sheetTitle.isSelected = true

        sheetSeek.setOnSeekBarChangeListener(this)

        play = Player(requireActivity().application,requireContext())
        play.player.addListener(this)

        playBtn.setOnClickListener {
            if(play.isPlaying()) {
                playBtn.setImageResource(R.drawable.play)
                play.pause()
            }
            else {
                playBtn.setImageResource(R.drawable.pause)
                play.play()
            }
        }

        forwardBtn.setOnClickListener {
            play.forward()
        }

        backwardBtn.setOnClickListener {
            play.backward()
        }

        val adapter = SearchAdapter(activity as Activity)
        listView.adapter = adapter

        listView.setOnItemClickListener { adapterView, view, i, l ->
            if (search.hasFocus())
            {
                search.setQuery(adapter.getItem(i).toString(),true)
            }
            else
            {
                play.pause()

                val info = (adapterView.adapter as SearchListAdapter).getItem(i) as YTInfo // infos.get(i)

                info.title.also {
                    title.text = it
                    sheetTitle.text = it
                }

                Glide.with(requireActivity()).asDrawable().addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        thumbnail.setImageDrawable(resource)
                        sheetThumb.setImageDrawable(resource)
                        return false
                    }

                }).load(info.thumbnail).submit()

                CoroutineScope(Dispatchers.IO).launch {
                    info.getStream(info)
                    requireActivity().runOnUiThread {
                        play.play(info)
                    }
                }
            }
        }

        val behavior = BottomSheetBehavior.from(bottom_view).apply {
            peekHeight = 0
        }

        controls?.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        search.setOnClickListener {
            search.requestFocus()
            search.onActionViewExpanded()
            listView.adapter = adapter
        }

        search.setOnQueryTextListener(object : OnQueryTextListener {

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.getSuggestions(p0!!)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

                search.clearFocus()
                search.onActionViewCollapsed()

                if(query!=null && query.isEmpty()) return false
                CoroutineScope(Dispatchers.IO).cancel()

                runBlocking {

                    CoroutineScope(Dispatchers.IO).launch {

                        val info = YTInfo(query!!)
                        val infos = info.fetch()

                        activity!!.runOnUiThread {
                            updateUI(infos!!)
                        }

                    }

                }

                return false
            }
        })

        return view
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when(playbackState)
        {
            ExoPlayer.STATE_READY -> {

                sheetSeek.apply {
                    max = play.getDuration()
                    sheetEnd.text = play.getStringDuration()
                }

                seekHandler.postDelayed(object : Runnable {
                    override fun run() {
                        sheetSeek.apply {
                            progress = play.getCurrentPosition()
                            secondaryProgress = play.getBufferedDuration()

                            sheetStart.text = play.getCurrentStringDuration()
                        }
                        seekHandler.postDelayed(this,1000)
                    }
                },0)

            }

            ExoPlayer.STATE_ENDED -> {
                seekHandler.removeCallbacksAndMessages(null)
            }
        }
        super.onPlaybackStateChanged(playbackState)
    }

    fun updateUI(infos : List<YTInfo>)
    {
        val adapter = SearchListAdapter(requireContext(), infos)
        listView.adapter = adapter
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        if(p2)
        {
            play.setCurrentPosition(p1)
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
       play.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        play.play()
    }
}
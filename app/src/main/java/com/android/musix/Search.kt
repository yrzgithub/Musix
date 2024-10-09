package com.android.musix

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
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


class Search : Fragment(),Listener,OnSeekBarChangeListener,OnClickListener,OnItemClickListener,OnQueryTextListener {

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
    lateinit var search : SearchView
    lateinit var controls : LinearLayout

    lateinit var adapter : SearchAdapter

    lateinit var behavior : BottomSheetBehavior<LinearLayout>

    var seekHandler : Handler = Handler(Looper.getMainLooper())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val layout = view.findViewById<LinearLayout>(R.id.layout)
        search = view.findViewById<SearchView>(R.id.search)
        listView = view.findViewById<ListView>(R.id.searchList).apply { onItemClickListener = this@Search }

        controls = activity?.findViewById<LinearLayout>(R.id.controls)!!
        thumbnail = activity?.findViewById<ImageView>(R.id.thumbnail)!!
        title = activity?.findViewById<TextView>(R.id.title)!!.apply { isSelected = true }
        favourite = activity?.findViewById<ImageButton>(R.id.favourite)!!
        sheetEnd = activity?.findViewById<TextView>(R.id.end)!!
        sheetStart = activity?.findViewById<TextView>(R.id.start)!!
        playBtn = activity?.findViewById<ImageButton>(R.id.play)!!
        forwardBtn = activity?.findViewById<ImageButton>(R.id.forward)!!
        backwardBtn = activity?.findViewById<ImageButton>(R.id.backward)!!

        val bottomView = requireActivity().findViewById<LinearLayout>(R.id.bottom_sheet)
        sheetThumb = bottomView.findViewById(R.id.thumb)
        sheetTitle = bottomView.findViewById<TextView?>(R.id.title).apply { isSelected = true }
        sheetSeek = bottomView.findViewById(R.id.seek)

        play = Player(requireActivity().application,requireContext())
        play.player.addListener(this)

        behavior = BottomSheetBehavior.from(bottomView).apply {
            peekHeight = 0
        }

        adapter = SearchAdapter(activity as Activity)
        listView.adapter = adapter

        sheetSeek.setOnSeekBarChangeListener(this)

        playBtn.setOnClickListener(this)
        forwardBtn.setOnClickListener(this)
        backwardBtn.setOnClickListener(this)
        controls.setOnClickListener(this)
        search.setOnClickListener(this)

        search.setOnQueryTextListener(this)

        return view
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when(playbackState)
        {
            ExoPlayer.STATE_READY -> {

                sheetSeek.max = play.getTotalDuration()
                sheetEnd.text = play.getTotalStringDuration()

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
                sheetEnd.setText(play.getTotalStringDuration())

            }

            ExoPlayer.STATE_BUFFERING -> {
                playBtn.setImageResource(R.drawable.loading)
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

    override fun onClick(p0: View?) {
        when(p0?.id)
        {
            playBtn.id -> {
                if(play.isPlaying()) {
                    play.pause()
                }
                else {
                    play.play()
                }
            }

            forwardBtn.id -> play.forward()

            backwardBtn.id -> play.backward()

            controls.id -> behavior.state = BottomSheetBehavior.STATE_EXPANDED

            search.id -> {
                search.requestFocus()
                search.onActionViewExpanded()
                listView.adapter = adapter
            }
        }
    }

    override fun onItemClick(adapterView: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
        if (adapterView?.adapter?.javaClass?.equals(SearchAdapter::class.java)!!)
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        search.clearFocus()
        search.onActionViewCollapsed()

        if(query!=null && query.isEmpty()) return false
        CoroutineScope(Dispatchers.IO).cancel()

        runBlocking {

            CoroutineScope(Dispatchers.IO).launch {

                val info = YTInfo(query!!)
                val infos = info.fetch()

                requireActivity().runOnUiThread {
                    updateUI(infos!!)
                }

            }

        }

        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.getSuggestions(newText!!)
        return false
    }

    override fun onPositionDiscontinuity(oldPosition: androidx.media3.common.Player.PositionInfo, newPosition: androidx.media3.common.Player.PositionInfo, reason: Int) {

        println("${oldPosition.positionMs} ${newPosition.positionMs}")

        super.onPositionDiscontinuity(oldPosition, newPosition, reason)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {

        playBtn.setImageResource(if(isPlaying) R.drawable.pause else R.drawable.play)

        super.onIsPlayingChanged(isPlaying)
    }
}
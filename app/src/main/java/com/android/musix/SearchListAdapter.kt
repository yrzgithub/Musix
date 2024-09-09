package com.android.musix

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

class SearchListAdapter(val context : Context, var info : List<YTInfo>) : BaseAdapter() {

    override fun getCount(): Int {
        return info.size
    }

    override fun getItem(p0: Int): Any {
        return info[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, view_: View?, p2: ViewGroup?): View {

        if(view_==null)
        {
            val view = LayoutInflater.from(context).inflate(R.layout.custom_yt_search,null,false)

            val thumbnail = view.findViewById<ImageView>(R.id.thumb_search)
            val title = view.findViewById<TextView>(R.id.title_search)
            val channel_icon = view.findViewById<ShapeableImageView>(R.id.uploader_icon)
            val channel = view.findViewById<TextView>(R.id.channel)
            val views = view.findViewById<TextView>(R.id.views)
            val time = view.findViewById<TextView>(R.id.time)

            val ytInfo = info[p0]

            Glide.with(context).load(ytInfo.thumbnail).into(thumbnail)
            Glide.with(context).load(ytInfo.channelThumbnail).into(channel_icon)

            title.setText(ytInfo.title)
            channel.setText(ytInfo.channelName)
            views.setText(ytInfo.views)
            time.setText(ytInfo.publishedTime)

            return view
        }

        return view_
    }
}
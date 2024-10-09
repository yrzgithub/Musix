package com.android.musix

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.imageview.ShapeableImageView

class SearchListAdapter(val context : Context, var info : List<YTInfo>) : BaseAdapter() {

    var thumbnails : MutableList<Drawable?> = MutableList(count) { null }
    var channels : MutableList<Drawable?> = MutableList(count) { null }

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

        val view =
            view_ ?: LayoutInflater.from(context).inflate(R.layout.custom_yt_search,null,false) // elvis operator

        val thumbnail = view.findViewById<ImageView>(R.id.thumb_search)
        val title = view.findViewById<TextView>(R.id.title_search)
        val channel_icon = view.findViewById<ShapeableImageView>(R.id.uploader_icon)
        val channel = view.findViewById<TextView>(R.id.channel)
        val views = view.findViewById<TextView>(R.id.views)
        val time = view.findViewById<TextView>(R.id.time)

        val ytInfo = info[p0]

        if(thumbnails[p0]!=null)
        {
            thumbnail.setImageDrawable(thumbnails[p0])
        }
        else
        {
            println("Thumbnail loading")

            Glide.with(context).asDrawable().addListener(object : RequestListener<Drawable>{

                val current = p0
                val current_thumb = thumbnail

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    thumbnails[current] = resource!!
                    current_thumb.setImageDrawable(resource)
                    return false
                }

            }).load(ytInfo.thumbnail).submit()
        }

        if(channels[p0]!=null)
        {
            channel_icon.setImageDrawable(channels[p0])
        }
        else
        {
            println("Channel Thumbnail loading")

            Glide.with(context).asDrawable().addListener(object : RequestListener<Drawable>{

                val current = p0
                val current_channel = channel_icon

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    channels[current] = resource!!
                    current_channel.setImageDrawable(resource)
                    return false
                }

            }).load(ytInfo.channelThumbnail).submit()
        }

        title.text = ytInfo.title
        channel.text = ytInfo.channelName
        views.text = ytInfo.views
        time.text = ytInfo.publishedTime

        return view
    }
}
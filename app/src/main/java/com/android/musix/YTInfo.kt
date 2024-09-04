package com.android.musix

import android.util.Log
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YTInfo(val query : String) {

    var instance = Python.getInstance()
    var module = instance.getModule("main")

    var title : String? = null
    var publishedTime : String? = null
    var thumbnail : String? = null
    var channelName : String? = null
    var channelThumbnail : String? = null
    var link : String? = null
    var stream_url : String? = null

    var infoList : MutableList<YTInfo>? = null

    constructor(query : String,title:String,publishedTime:String,thumbnail:String,channelName:String,channelThumbnail:String,link:String,stream_url:String) : this(query)
    {
        this.title = title
        this.publishedTime = publishedTime
        this.thumbnail = thumbnail
        this.channelName = channelName
        this.channelThumbnail = channelThumbnail
        this.link = link
        this.stream_url = stream_url
    }

    suspend fun fetch()
    {
        withContext(Dispatchers.IO) {
            infoList = mutableListOf<YTInfo>()

            module.callAttr("getUrlsInfo",query).asList().forEach {

                    val result = it.asMap().entries.associate {
                        (key,value) -> if(key!=null && value!=null) key.toString() to value.toString() else key.toString() to null
                    }

                    val info = YTInfo(query)

                    info.apply {
                        title = result["title"]
                        publishedTime = result["publishedTime"]
                        thumbnail = result["thumbnail"]
                        channelName = result["channelName"]
                        channelThumbnail = result["channelThumnail"]
                        link = result["link"]
                    }

                    infoList!!.add(info)
                }
            }
    }

    suspend fun getStream() : String? {
        stream_url = module.callAttr("getStream", infoList?.get(0)?.link).toString()
        Log.e("YTInfo", stream_url!!)
        return stream_url

    }
}

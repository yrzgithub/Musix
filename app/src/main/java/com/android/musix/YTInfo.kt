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
    var views : String? = null
    var link : String? = null
    var stream_url : String? = null

    var infoList : List<YTInfo>? = null

    fun fetch() : List<YTInfo>?
    {
         infoList = module.callAttr("getUrlsInfo",query).asList().map {

            val result = it.asMap().entries.associate {
                    (key,value) -> if(key!=null && value!=null) key.toString() to value.toString() else key.toString() to null
            }

            val info = YTInfo(query)

            info.apply {
                title = result["title"]
                publishedTime = result["publishedTime"]
                thumbnail = result["thumbnail"]
                channelName = result["channelName"]
                views = result["views"]
                channelThumbnail = result["channelThumnail"]
                link = result["link"]
            }
        }

        return infoList
    }

    fun getStream(info : YTInfo) : String? {
        println("link : "+ info.link)
        stream_url = module.callAttr("getStream", info.link).toString()
        return stream_url
    }
}

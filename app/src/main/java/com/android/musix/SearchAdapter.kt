package com.android.musix

import android.app.Activity
import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.chaquo.python.Python
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchAdapter(var activity : Activity) : BaseAdapter() {

    var results : List<String> = listOf()
    var main = Python.getInstance().getModule("main")

    override fun getCount(): Int {
        return results.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, view: View?, p2: ViewGroup?): View {

        val viewNew : View = if(view==null) activity.layoutInflater.inflate(R.layout.search_result,p2,false) else view
        val textResult : TextView = viewNew.findViewById(R.id.searchResult)
        textResult.text = results[p0]
        return viewNew

    }

    fun update(results: List<String>) {
        activity.runOnUiThread {
            this.results = results
            notifyDataSetChanged()
        }
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        super.registerDataSetObserver(observer)
    }

    fun getSuggestions(query : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val results : List<String> = main.callAttr("getSuggestions",query).asList().map { it.toString() }
            println(results)
            update(results)
        }
    }
}
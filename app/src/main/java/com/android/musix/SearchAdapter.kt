package com.android.musix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import com.chaquo.python.Python
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchAdapter() : BaseAdapter(),Filterable {

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

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val results = FilterResults()

                val query : String = p0.toString()
              //  println(query)
                getSuggestions(query)

                results.apply {
                    count = this@SearchAdapter.count
                    values = this@SearchAdapter.results.filter { check -> check.contains(p0.toString()) }
                }

                return results
            }

            override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
                if(filterResults!=null)
                {
                    this@SearchAdapter.results = filterResults.values as List<String>
                    notifyDataSetChanged()
                }
            }

        }

    }

    fun update(results: List<String>) {
        this.results = results
        notifyDataSetChanged()
    }

    fun getSuggestions(query : String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val results : List<String> = main.callAttr("getSuggestions",query).map { it.toString() }
            println(results)
            update(results)
        }
    }
}
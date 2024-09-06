package com.android.musix

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.widget.addTextChangedListener
import com.chaquo.python.Python
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Search : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val search = view.findViewById<SearchView>(R.id.search)
        val listView = view.findViewById<ListView>(R.id.searchList)

        val adapter = SearchAdapter(activity as Activity)
        listView.adapter = adapter

        search.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.getSuggestions(p0!!)
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                TODO("Not yet implemented")
            }
        })

        return view
    }
}
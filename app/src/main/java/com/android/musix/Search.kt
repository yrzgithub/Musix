package com.android.musix

import android.app.Activity
import android.app.Application
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AnimationUtils
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.GONE
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.SearchView.OnSuggestionListener
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.chaquo.python.Python
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class Search : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val layout = view.findViewById<LinearLayout>(R.id.layout)
        val search = view.findViewById<SearchView>(R.id.search)
        val listView = view.findViewById<ListView>(R.id.searchList)

        val controls = activity?.findViewById<LinearLayout>(R.id.controls)

        val thumbnail = activity?.findViewById<ImageView>(R.id.thumbnail)
        val title = activity?.findViewById<TextView>(R.id.title)
        val favourite =activity?.findViewById<ImageButton>(R.id.favourite)

       // val player = activity?.findViewById<LinearLayout>(R.id.player)

        title!!.isEnabled = true

        val adapter = SearchAdapter(activity as Activity)
        listView.adapter = adapter

        listView.setOnItemClickListener { adapterView, view, i, l ->
            search.setQuery(adapter.getItem(i).toString(),false)
        }

        val bottom_view = requireActivity().findViewById<LinearLayout>(R.id.bottom_sheet)

        val behavior = BottomSheetBehavior.from(bottom_view).apply {
            peekHeight = 0
        }

        controls?.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        search.setOnClickListener {
            listView.adapter = adapter
        }

        search.setOnQueryTextListener(object : OnQueryTextListener {

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.getSuggestions(p0!!)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

                if(query!=null && query.isEmpty()) return false
                CoroutineScope(Dispatchers.IO).cancel()

                runBlocking {

                    CoroutineScope(Dispatchers.IO).launch {

                        val info = YTInfo(query!!)

                        val infos = info.fetch()

                        val adapter = SearchListAdapter(context!!, infos!!)

                        activity!!.runOnUiThread {

                            listView.adapter = adapter
                            listView.setOnItemClickListener { adapterView, view, i, l ->

                                val info = infos.get(i)

                                Glide.with(requireContext()).load(info.thumbnail).into(thumbnail!!)
                                title!!.setText(info.title)

                                val play = Player(requireActivity().application,requireContext())

                                CoroutineScope(Dispatchers.IO).launch {

                                    info.getStream(info)
                                    requireActivity().runOnUiThread {
                                        play.play(info)
                                    }

                                }

                            }
                        }

                    }

                }

                return false
            }
        })

        return view
    }
}
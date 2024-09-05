package com.android.musix

import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(var manager : FragmentManager,cycle : Lifecycle) : FragmentStateAdapter(manager,cycle) {

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return Search()
    }

}
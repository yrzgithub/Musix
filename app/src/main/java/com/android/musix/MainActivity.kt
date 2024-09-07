package com.android.musix

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val main = findViewById<LinearLayout>(R.id.main)
        val tab : TabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val pager = findViewById<ViewPager2>(R.id.viewPager)
        val controls = findViewById<LinearLayout>(R.id.controls)

        tab.addTab(tab.newTab().setIcon(R.drawable.search))
        tab.addTab(tab.newTab().setIcon(R.drawable.favourite))
        tab.addTab(tab.newTab().setIcon(R.drawable.library))

        pager.adapter = FragmentAdapter(supportFragmentManager,lifecycle)

        pager.registerOnPageChangeCallback(object : OnPageChangeCallback()
        {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }

            override fun onPageSelected(position: Int) {
                println("Layout $position")
                tab.selectTab(tab.getTabAt(position))
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })

        controls.setOnClickListener {

            println("Clicked")

            val layout = layoutInflater.inflate(R.layout.player_pop,null)

            val dropDown = layout.findViewById<ImageButton>(R.id.drop_down)

            val pop = PopupWindow(this@MainActivity)

            pop.setOnDismissListener {
//                pop.animationStyle = R.style.PopDownAnimation
            }

            dropDown.setOnClickListener {
//                pop.dismiss()
//                pop.animationStyle=R.style.PopupAnimation
            }

//            pop.apply {
//                width = MATCH_PARENT
//                height = WRAP_CONTENT
//                contentView = layout
//                animationStyle=R.style.PopupAnimation
//                showAtLocation(layout,Gravity.BOTTOM,0,0)
//            }
        }

    }
}
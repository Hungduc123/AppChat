package com.phd.chat14android


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

import com.phd.chat14android.adapter.ViewPager2Adapter
import com.phd.chat14android.databinding.ActivityMainBinding
import com.phd.chat14android.ui.fragments.ChatFragment

class MainActivity : AppCompatActivity() {

    private var fragment: Fragment? = null
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Hide Toolbar manually
        //getSupportActionBar().hide();
        //setSupportActionBar(binding.toolbar)

        val adapter = ViewPager2Adapter(this)
        binding.content.adapter = adapter

        binding.content.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                when(position){
                    0-> binding.bottomNav.menu.findItem(R.id.navigation_chat).isChecked = true
                    1-> binding.bottomNav.menu.findItem(R.id.navigation_contacts).isChecked = true
                    2-> binding.bottomNav.menu.findItem(R.id.navigation_profile).isChecked = true
                    else-> { 0 }
                }
            }
        })
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_chat -> {binding.content.currentItem = 0 }

                R.id.navigation_contacts -> {binding.content.currentItem = 1}
                R.id.navigation_profile -> {binding.content.currentItem = 2}
            }
            true
        }

    }
}
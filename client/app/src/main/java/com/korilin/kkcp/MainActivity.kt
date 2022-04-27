package com.korilin.kkcp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.korilin.kkcp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            viewPager.adapter = ViewPager2Adapter(this@MainActivity)
            TabLayoutMediator(tableLayout, viewPager) { tab, position ->
                tab.text = tabs[position]
            }.attach()
        }
        setContentView(binding.root)
    }

    private val tabs = arrayOf("Account", "Record", "User")
    private val fragments = arrayOf(
        AccountFragment.newInstance(), OptionsFragment.newInstance(), UserFragment.newInstance()
    )

    inner class ViewPager2Adapter(activity: MainActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}

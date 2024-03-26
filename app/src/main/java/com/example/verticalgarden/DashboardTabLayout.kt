package com.example.verticalgarden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
class DashboardTabLayout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_tab_layout)

        val viewPager = findViewById<ViewPager>(R.id.viewpage)
        setupViewPager(viewPager)

        val tabLayout = findViewById<TabLayout>(R.id.tablayout)
        tabLayout.setupWithViewPager(viewPager)

        val iconSettings = findViewById<ImageButton>(R.id.iconsettings)
        iconSettings.setOnClickListener {
            // Mulai Activity baru ketika ikon diklik
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }
    }



    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(MonConFragment(), "Monitoring dan Controling")
        adapter.addFragment(EvalFragment(), "Evaluation")
        adapter.addFragment(PlantInfoFragment(), "Plant Info")
        viewPager.adapter = adapter
    }
}

class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<String>()

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}

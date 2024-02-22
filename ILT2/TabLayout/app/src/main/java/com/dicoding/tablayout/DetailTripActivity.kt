package com.dicoding.tablayout

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailTripActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_trip)

        val ivTripImage = findViewById<ImageView>(R.id.iv_tripImage)
        val tvTripName = findViewById<TextView>(R.id.tv_tripName)
        val tvTripDescription = findViewById<TextView>(R.id.tv_tripDescription)
        val tvTripPrice = findViewById<TextView>(R.id.tv_tripPrice)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

        val tripID = intent.getIntExtra(EXTRA_TRIPID, 0)
        val tripData = TripsData.getSpecificTrip(tripID)

        ivTripImage.setImageResource(tripData.image)
        tvTripName.text = tripData.name
        tvTripDescription.text = tripData.description
        tvTripPrice.text = buildSpannedString {
            append("Price: ")
            bold {
                append("$${tripData.price}")
            }
        }

        // TODO: 4. Explain about SectionsPagerAdapter which is a FragmentStateAdapter that allow the TabLayoutMediator to show 2 layout using a tab
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.tripID = tripData.id
        viewPager.adapter = sectionsPagerAdapter

        // TODO: 3. Explain about TabLayoutMediator as a way to show 2 layout using a tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0){
                tab.text = "Tour Guides"
            } else if (position == 1){
                tab.text = "People who booked this trip"
            }
        }.attach()

    }


    companion object {
        const val EXTRA_TRIPID = "tripID"
    }
}
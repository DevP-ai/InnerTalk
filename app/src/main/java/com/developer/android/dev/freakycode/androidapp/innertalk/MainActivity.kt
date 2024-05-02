package com.developer.android.dev.freakycode.androidapp.innertalk

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.developer.android.dev.freakycode.androidapp.innertalk.adapter.FragmentViewPagerAdapter
import com.developer.android.dev.freakycode.androidapp.innertalk.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private  var _binding: ActivityMainBinding?=null
    private val binding get() = _binding!!

    private lateinit var fragmentAdapter: FragmentViewPagerAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()

        setContentView(binding!!.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Check User login or not
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser!=null){
            startActivity(Intent(this,NavHostActivity::class.java))
            finish()
        }


        fragmentAdapter = FragmentViewPagerAdapter(supportFragmentManager,lifecycle)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Client"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Therapist"))

        binding.viewPager.adapter = fragmentAdapter

        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
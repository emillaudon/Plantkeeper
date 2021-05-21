package com.example.plantkeeper.activities

import android.content.Intent
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.plantkeeper.R
import com.example.plantkeeper.fragments.AddFriendFragment
import com.example.plantkeeper.fragments.CreateFragment
import com.example.plantkeeper.fragments.HomeFragment
import com.example.plantkeeper.fragments.ProfileFragment
import com.example.plantkeeper.models.NetworkHandler
import com.example.plantkeeper.models.User
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment=HomeFragment()
        val profileFragment=ProfileFragment()
        val createFragment=CreateFragment()

        setCurrentFragment(homeFragment)

        var userNameTextView = findViewById<TextView>(R.id.userNameTextView)
        var userName = User.name
        userNameTextView.text = userName

        var addFriendsImageView = findViewById<ImageView>(R.id.addFriendsButton)
        addFriendsImageView.setOnClickListener {
            val newFragment: Fragment = AddFriendFragment()
            setCurrentFragment(newFragment)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(homeFragment)
                R.id.user->setCurrentFragment(profileFragment)
                R.id.add->setCurrentFragment(createFragment)
            }
            true
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment =supportFragmentManager.findFragmentById(R.id.addfragment)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun replaceFragments(fragmentClass: Class<*>) {
        var fragment: Fragment? = null
        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Insert the fragment by replacing any existing fragment
        val fragmentManager: FragmentManager = supportFragmentManager
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.addfragment, fragment)
                .commit()
        }
    }
}

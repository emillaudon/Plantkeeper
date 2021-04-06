package com.example.plantkeeper.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.plantkeeper.R
import com.example.plantkeeper.fragments.CreateFragment
import com.example.plantkeeper.fragments.HomeFragment
import com.example.plantkeeper.fragments.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment=HomeFragment()
        val profileFragment=ProfileFragment()
        val createFragment=CreateFragment()

        setCurrentFragment(homeFragment)


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
        print("fjkajfa")
        super.onActivityResult(requestCode, resultCode, data)
        print(data)
        val fragment =supportFragmentManager.findFragmentById(R.id.addfragment)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    }

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

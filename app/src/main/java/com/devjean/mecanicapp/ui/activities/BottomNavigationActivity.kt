package com.devjean.mecanicapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.devjean.mecanicapp.R
import com.devjean.mecanicapp.ui.fragments.ListFragment
import com.devjean.mecanicapp.ui.fragments.ProfileFragment
import com.devjean.mecanicapp.ui.fragments.RegisterFragment
import com.devjean.mecanicapp.ui.fragments.ReportFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationActivity : AppCompatActivity() {

    private var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)

        openFragment(RegisterFragment())

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.item_register -> {
                    openFragment(RegisterFragment())
                    true
                }

                R.id.item_list -> {
                    openFragment(ListFragment())
                    true
                }

                R.id.item_report -> {
                    openFragment(ReportFragment())
                    true
                }

                R.id.item_profile -> {
                    openFragment(ProfileFragment())
                    true
                }

                else -> false

            }

        }

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
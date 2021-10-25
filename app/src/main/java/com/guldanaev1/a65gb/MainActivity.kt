package com.guldanaev1.a65gb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.guldanaev1.a65gb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Connection{

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)

        if (savedInstanceState == null) {
            contactInfoFragment()
        }
    }

     private fun contactInfoFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.dataContainer, ContactListFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun showContactDetailsFragment(id: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.dataContainer, ContactDetailsFragment.newInstance(id))
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}

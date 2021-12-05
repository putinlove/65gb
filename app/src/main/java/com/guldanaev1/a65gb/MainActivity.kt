package com.guldanaev1.a65gb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.guldanaev1.a65gb.AlarmReceiver.Companion.ID
import com.guldanaev1.a65gb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ContactDetailsListener {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.mainToolbar)
        if (savedInstanceState == null) {
            showContactListFragment()
        }
        val id: String? = intent.getStringExtra(ID)
        if (id != null) {
            showContactDetailsFragment(id)
        }
    }

    private fun showContactListFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.dataContainer, ContactListFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun showContactDetailsFragment(id: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.dataContainer, ContactDetailsFragment.newInstance(id))
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }
}

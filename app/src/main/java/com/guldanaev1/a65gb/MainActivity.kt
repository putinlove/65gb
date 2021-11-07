package com.guldanaev1.a65gb

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.guldanaev1.a65gb.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ContactDetailsListener, ServiceInterface {

    private var binding: ActivityMainBinding? = null
    private var contactService: ContactService? = null
    private var bound: Boolean = false
    private var savedInstance = true

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ContactService.ContactBinder
            contactService = binder.getService()
            bound = true
            if (!savedInstance) {
                showContactListFragment()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.mainToolbar)

        if (savedInstanceState == null) {
            savedInstance = false
        }
        Intent(this, ContactService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        binding = null
        if (bound) {
            unbindService(connection)
            bound = false
        }
        super.onDestroy()
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

    override fun getService() = contactService
}

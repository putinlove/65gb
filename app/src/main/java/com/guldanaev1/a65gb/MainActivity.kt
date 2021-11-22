package com.guldanaev1.a65gb

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.guldanaev1.a65gb.databinding.ActivityMainBinding

private const val uriPackageScheme = "package:"

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
            val id = intent.getStringExtra("id")
            if (id != null && !savedInstance) {
                showContactListFragment()
                showContactDetailsFragment(id)
            } else if (!savedInstance) {
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
        showContactPermission()
    }

    private fun showContactPermission() {
        val intent = Intent(this, ContactService::class.java)
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    bindService(intent, connection, Context.BIND_AUTO_CREATE)
                } else {
                    showNoContactRationale()
                }
            }
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            ) -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.contacts_rationale_title))
                    .setMessage(getString(R.string.contacts_rationale_text))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.contact_rationale_button_yes)) { _, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                    .setNegativeButton(getString(R.string.contact_rationale_button_no)) { dialog, _ ->
                        Toast.makeText(this, R.string.denied_toast, Toast.LENGTH_SHORT).show()
                        dialog.cancel()
                    }
                builder.create().show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    private fun showNoContactRationale() {
        binding?.dataContainer?.let {
            Snackbar.make(it, R.string.contacts_rationale_text, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.contact_rationale_button_settings) {
                    val appSettingsIntent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse(uriPackageScheme + this.packageName)
                    )
                    startActivity(appSettingsIntent)
                }
                .show()
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

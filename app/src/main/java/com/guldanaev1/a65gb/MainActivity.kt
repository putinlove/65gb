package com.guldanaev1.a65gb

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.guldanaev1.a65gb.AlarmReceiver.Companion.ID
import com.guldanaev1.a65gb.databinding.ActivityMainBinding

private const val URI_PACKAGE_SCHEME = "package:"

class MainActivity : AppCompatActivity(), ContactDetailsListener {

    private var binding: ActivityMainBinding? = null
    private var isCreated: Boolean = false
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted)
                addFragment()
            else {
                showNoContactRationale()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.mainToolbar)
        isCreated = savedInstanceState == null
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                addFragment()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            ) -> {
                showContactPermission(requestPermissionLauncher)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    private fun addFragment() {
        if (isCreated) {
            showContactListFragment()
        }
        val id: String? = intent.getStringExtra(ID)
        if (id != null) {
            showContactDetailsFragment(id)
        }
    }

private fun showContactPermission(requestPermissionLauncher: ActivityResultLauncher<String>) {
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
        .create()
}

private fun showNoContactRationale() {
    binding?.dataContainer?.let {
        Snackbar.make(it, R.string.contacts_rationale_text, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.contact_rationale_button_settings) {
                val appSettingsIntent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse(URI_PACKAGE_SCHEME + this.packageName)
                )
                startActivity(appSettingsIntent)
            }.show()
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

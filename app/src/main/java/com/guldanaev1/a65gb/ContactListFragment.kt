package com.guldanaev1.a65gb

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.guldanaev1.a65gb.databinding.FragmentContactListBinding

private const val URI_PACKAGE_SCHEME = "package:"

class ContactListFragment : Fragment(), SearchView.OnQueryTextListener {
    private var binding: FragmentContactListBinding? = null
    private var contactDetailsListener: ContactDetailsListener? = null
    private val viewModelContactList: ContactListViewModel by viewModels()

    private val readContactsPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    viewModelContactList.requestContactList("")
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_CONTACTS
                ) -> {
                    checkPermissionsAndRequestContactList()
                }
                else -> {
                    showNoContactRationale()
                }
            }
        }

    private fun showNoContactRationale() {
        binding?.fragmentList?.let {
            Snackbar.make(it, R.string.contacts_rationale_text, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.contact_rationale_button_settings) {
                    val appSettingsIntent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse(URI_PACKAGE_SCHEME + requireActivity().packageName)
                    )
                    startActivity(appSettingsIntent)
                }.show()
        }
    }

    private fun checkPermissionsAndRequestContactList() {
        readContactsPermission.launch(Manifest.permission.READ_CONTACTS)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contacts_search, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false
    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            viewModelContactList.requestContactList(newText)
        }
        return true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contactDetailsListener = context as ContactDetailsListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentContactListBinding.inflate(inflater, container, false)
        .apply { binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_your_contacts)
        val contactsAdapter =
            ContactListAdapter { contact ->
                contactDetailsListener?.showContactDetailsFragment(contact.id)
            }
        with(binding!!.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contactsAdapter
            addItemDecoration(
                SimpleOffsetDrawerItemDecoration(
                    offset = resources.getDimensionPixelSize(
                        R.dimen.main_padding
                    )
                )
            )
        }
        viewModelContactList.contactList.observe(viewLifecycleOwner, {
            contactsAdapter.submitList(it)
        })
        viewModelContactList.isLoading.observe(viewLifecycleOwner, { isLoading ->
            binding?.progressBar?.isVisible = isLoading
        })
        setHasOptionsMenu(true)
        checkPermissionsAndRequestContactList()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        contactDetailsListener = null
        super.onDetach()
    }
}

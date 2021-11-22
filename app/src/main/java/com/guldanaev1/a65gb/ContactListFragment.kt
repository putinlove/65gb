package com.guldanaev1.a65gb

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.guldanaev1.a65gb.databinding.FragmentContactListBinding

class ContactListFragment : Fragment() {
    private var binding: FragmentContactListBinding? = null
    private var contactDetailsListener: ContactDetailsListener? = null
    private var contactService: ServiceInterface? = null
    private lateinit var contactId: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contactService = context as ServiceInterface
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
        binding?.contactCell?.setOnClickListener {
            contactDetailsListener?.showContactDetailsFragment(contactId)
        }
        getContactList()
    }

    private val callback = object : ContactListLoadListener {
        override fun onContactListLoaded(contacts: List<ContactModel>) {
            requireActivity().runOnUiThread {
                binding?.apply {
                    nameView.text = contacts[0].contactName
                    contactId = contacts[0].id
                    if (contacts[0].numberList.isNotEmpty()) {
                        numberView.text = contacts[0].numberList[0]
                    }
                    if(contacts[0].photoResource != null) {
                        imageView.setImageURI(Uri.parse(contacts[0].photoResource))
                    }
                }

            }
        }
    }

    private fun getContactList() {
        contactService?.getService()?.getContactList(callback)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        contactDetailsListener = null
        contactService = null
        super.onDetach()
    }
}

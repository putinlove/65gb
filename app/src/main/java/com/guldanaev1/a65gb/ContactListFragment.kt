package com.guldanaev1.a65gb

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.guldanaev1.a65gb.databinding.FragmentContactListBinding

private const val CONTACT_ID = "id"

class ContactListFragment : Fragment() {

    private var binding: FragmentContactListBinding? = null
    private var contactDetailsListener: ContactDetailsListener? = null
    private var contactService: ServiceInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contactService = context as ServiceInterface
        contactDetailsListener = context as ContactDetailsListener
        getContactList()
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
            contactDetailsListener?.showContactDetailsFragment(CONTACT_ID)
        }
        getContactList()
    }

    private val callback = object : ContactListLoadListener {
        override fun onContactListLoaded(list: List<ContactModel>) {
            requireActivity().runOnUiThread {
                binding?.apply {
                    imageView.setImageResource(list[0].photoResourceId)
                    nameView.text = list[0].contactName
                    numberView.text = list[0].number
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

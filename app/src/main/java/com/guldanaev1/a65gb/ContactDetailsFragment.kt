package com.guldanaev1.a65gb

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.guldanaev1.a65gb.databinding.FragmentContactDetailBinding

private const val CONTACT_ID = "id"

class ContactDetailsFragment : Fragment() {

    private var id: Int? = null
    private var binding: FragmentContactDetailBinding? = null
    private var contactService: ServiceInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contactService = context as ServiceInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentContactDetailBinding.inflate(inflater, container, false)
        .apply { binding = this }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_details_contact)
        id = requireArguments().getInt(CONTACT_ID)
        getContactDetails()
    }

    private val callback = object : ContactDetailsLoadListener {
        override fun onContactDetailsLoaded(contact: ContactModel) {
            requireActivity().runOnUiThread {
                binding?.apply {
                    imageView.setImageResource(contact.photoResourceId)
                    nameView.text = contact.contactName
                    numberView.text = contact.number
                    emailView.text = contact.email
                    descriptionView.text = contact.description
                }
            }
        }
    }

    private fun getContactDetails() {
        contactService?.getService()?.getContactDetails(id!!,callback)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        contactService = null
        super.onDetach()
    }

    companion object {
        fun newInstance(id: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(CONTACT_ID to id)
        }
    }
}

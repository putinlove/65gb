package com.guldanaev1.presentation.view.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.guldanaev1.domain.entities.ContactModel
import com.guldanaev1.presentation.R
import com.guldanaev1.presentation.databinding.FragmentContactDetailBinding
import com.guldanaev1.presentation.di.AppContainer
import com.guldanaev1.presentation.utils.AlarmUtils
import com.guldanaev1.presentation.viewmodels.ContactDetailsViewModel
import javax.inject.Inject

class ContactDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelContactDetails: ContactDetailsViewModel by viewModels { viewModelFactory }
    private val contactId: String by lazy { requireArguments().getString(CONTACT_ID).toString() }
    private var binding: FragmentContactDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as AppContainer)
            .appComponent
            .plusContactDetailsComponent()
            .inject(this)
        viewModelContactDetails.requestContactDetails(contactId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentContactDetailBinding.inflate(inflater, container, false)
        .apply { binding = this }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_details_contact)
        viewModelContactDetails.contactDetails.observe(
            viewLifecycleOwner,
            Observer<ContactModel> { contact ->
                val emailList = contact.emailList
                val numberList = contact.numberList
                binding?.apply {
                    if (contact == null) {
                        Toast.makeText(context, R.string.contact_toast, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        nameView.text = contact.contactName
                        descriptionView.text = contact.description
                        numberView.text = if (numberList.isNotEmpty()) numberList[0] else "-"
                        numberView2.text = if (numberList.size > 1) numberList[1] else "-"
                        if (emailList != null) {
                            emailView.text = if (emailList.isNotEmpty()) emailList[0] else "-"
                            emailView2.text = if (emailList.size > 1) emailList[1] else "-"
                        }
                        if (contact.photoResource != null) {
                            imageView.setImageURI(Uri.parse(contact.photoResource))
                        }
                        if (contact.birthday != null) {
                            birthdayView.text = getString(R.string.contact_birthday)
                        }
                    }
                }
                binding?.let { binding ->
                    binding.switchNotify.setOnClickListener {
                        if (binding.switchNotify.isChecked) {
                            AlarmUtils.setupAlarm(
                                requireContext(),
                                contact?.contactName,
                                contactId,
                                contact?.birthday
                            )
                        } else {
                            AlarmUtils.cancelAlarm(requireContext(), contactId)
                        }
                    }
                }
                binding?.switchNotify?.isChecked =
                    AlarmUtils.setSwitchState(requireContext(), contactId)
            })
        viewModelContactDetails.isLoading.observe(viewLifecycleOwner,
            { isLoading ->
                binding?.progressBar?.isVisible = isLoading
            })
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {
        private const val CONTACT_ID = "id"
        fun newInstance(id: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(CONTACT_ID to id)
        }
    }
}

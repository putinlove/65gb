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
    private var contactDetailsListener: Connection? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contactDetailsListener = context as Connection
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactListBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (contactDetailsListener as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_your_contacts)
        binding?.contactCell?.setOnClickListener {
            contactDetailsListener?.showContactDetailsFragment(CONTACT_ID)
        }
    }

    override fun onDetach() {
        contactDetailsListener = null
        super.onDetach()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}

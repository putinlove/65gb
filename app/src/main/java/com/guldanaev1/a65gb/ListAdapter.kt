package com.guldanaev1.a65gb

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guldanaev1.a65gb.databinding.RecylerviewItemContactListBinding

class ContactListAdapter(private val onClick: (ContactModel) -> Unit) :
    ListAdapter<ContactModel, ContactListAdapter.ContactListViewHolder>(ContactDiffCallback) {

    class ContactListViewHolder(
        binding: RecylerviewItemContactListBinding,
        val onClick: (ContactModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageViewRecycler = binding.imageView
        private val numberView = binding.numberView
        private val nameView = binding.nameView
        private var currentContactModel: ContactModel? = null

        init {
            itemView.setOnClickListener {
                currentContactModel?.let {
                    onClick(it)
                }
            }
        }

        fun bind(contact: ContactModel) {
            currentContactModel = contact
            numberView.text = contact.numberList[0]
            nameView.text = contact.contactName
            if (contact.photoResource != null) {
                imageViewRecycler.setImageURI(Uri.parse(contact.photoResource))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = RecylerviewItemContactListBinding.inflate(view, parent, false)
        return ContactListViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }
}

object ContactDiffCallback : DiffUtil.ItemCallback<ContactModel>() {
    override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem.id == newItem.id && (oldItem.contactName == newItem.contactName)
                && (oldItem.numberList == newItem.numberList)
    }

    override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem == newItem
    }
}

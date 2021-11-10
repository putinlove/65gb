package com.guldanaev1.a65gb

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
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
    private var contactId: String = "1"
    private var contactName: String? = null
    private var contactBirthday: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contactService = context as ServiceInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentContactDetailBinding.inflate(inflater, container, false)
        .apply {
            binding = this
            createChannel(
                getString(R.string.birthday_notification_channel_id),
                getString(R.string.notification_title)
            )
        }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_details_contact)
        id = requireArguments().getInt(CONTACT_ID)
        getContactDetails()
        switchChecked()
        binding?.switchNotify?.setOnClickListener {
            if (binding?.switchNotify?.isChecked == true) {
                setupAlarm()
            } else {
                cancelAlarm()
            }
        }
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
                    birthdayView.text = contact.birthday
                }
                contactBirthday = contact.birthday
                contactName = contact.contactName

            }
        }
    }

    private fun getContactDetails() {
        contactService?.getService()?.getContactDetails(id!!, callback)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onDetach() {
        contactService = null
        super.onDetach()
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun setupAlarm() {
        val alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notifyBody = "Сегодня день рождение у $contactName"
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("name", notifyBody)
            putExtra("id", contactId)
            putExtra("date", contactBirthday)
        }
        val existingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        if (existingIntent == null) {
            val alarmIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val timeBeforeBirthdayInMills: Long = contactBirthday.countMills()
            alarmMgr.set(
                AlarmManager.RTC_WAKEUP,
                timeBeforeBirthdayInMills, alarmIntent
            )
        }
    }

    private fun cancelAlarm() {
        val alarmMgr: AlarmManager? = null
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            0
        )
        alarmMgr?.cancel(alarmIntent)
        alarmIntent.cancel()
    }

    private fun switchChecked() {
        val intent = Intent(context, AlarmReceiver::class.java)
        val existingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        binding?.switchNotify?.isChecked = existingIntent != null
    }

    companion object {
        fun newInstance(id: String) = ContactDetailsFragment().apply {
            arguments = bundleOf(CONTACT_ID to id)
        }
    }
}

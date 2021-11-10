package com.guldanaev1.a65gb

data class ContactModel(
    val photoResourceId: Int,
    val contactName: String,
    val number: String,
    val email: String,
    val description: String,
    val birthday: String,
)

val contactsList = listOf(
    ContactModel(
        R.drawable.ic_launcher_background,
        contactName = "Karim",
        number = "89128491077",
        email = "test@mail.ru",
        description = "test@mail.ru",
        "29/02/2000",
    )
)

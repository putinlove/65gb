package com.guldanaev1.a65gb

data class ContactModel(
    val photoResource: String?,
    val contactName: String,
    val numberList: List<String>,
    val emailList: List<String>?,
    val description: String?,
    val birthday: String?,
    val id: String,
)


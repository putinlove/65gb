package com.guldanaev1.domain.entities

data class ContactModel(
    val photoResource: String?,
    val contactName: String,
    val numberList: List<String>,
    val emailList: List<String>?,
    val description: String?,
    val birthday: String?,
    val id: String,
)

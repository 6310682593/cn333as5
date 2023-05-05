package com.example.phonebook.domain.model

const val NEW_CONTACT_ID = -1L

data class ContactModel(
    val id: Long = NEW_CONTACT_ID, // This value is used for new notes
    val name: String = "",
    val phoneNumber: String = "",
    val notes: String = "",
    val isFriend: Boolean = false,
    val tag: TagModel = TagModel.DEFAULT
)
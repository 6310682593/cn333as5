package com.example.phonebook.database

import com.example.phonebook.domain.model.ContactModel
import com.example.phonebook.domain.model.NEW_CONTACT_ID
import com.example.phonebook.domain.model.TagModel

class DbMapper {
    // Create list of ContactModels by pairing each note with a tag
    fun mapContacts(
        contactDbModels: List<ContactDbModel>,
        tagDbModels: Map<Long, TagDbModel>
    ): List<ContactModel> = contactDbModels.map {
        val tagDbModel = tagDbModels[it.tagId]
            ?: throw RuntimeException("Color for colorId: ${it.tagId} was not found. Make sure that all colors are passed to this method")

        mapContact(it, tagDbModel)
    }

    // convert ContactDbModel to ContactModel
    fun mapContact(contactDbModel: ContactDbModel, tagDbModel: TagDbModel): ContactModel {
        val tag = mapTag(tagDbModel)
        return with(contactDbModel) { ContactModel(id, name, phoneNumber,  notes, isFriend , tag,) }
    }

    // convert list of TagDdModels to list of TagModels
    fun mapTags(tagDbModels: List<TagDbModel>): List<TagModel> =
        tagDbModels.map { mapTag(it) }

    // convert TagDbModel to TagModel
    fun mapTag(tagDbModel: TagDbModel): TagModel =
        with(tagDbModel) { TagModel(id, name, hex) }

    // convert NoteModel back to NoteDbModel
    fun mapDbContact(contact: ContactModel): ContactDbModel =
        with(contact) {
            val isFriend = isFriend
            if (id == NEW_CONTACT_ID)
                ContactDbModel(
                    name = name,
                    phoneNumber = phoneNumber,
                    notes = "",
                    tagId = tag.id,
                    isInTrash = false,
                    isFriend = isFriend
                )
            else
                ContactDbModel(id, name, phoneNumber, notes, tag.id, false, isFriend)
        }
}
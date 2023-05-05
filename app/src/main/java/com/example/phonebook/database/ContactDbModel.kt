package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "tag_id") val tagId: Long,
    @ColumnInfo(name = "in_trash") val isInTrash: Boolean,
    @ColumnInfo(name = "is_friend") val isFriend: Boolean,

    ) {
    companion object {
        val DEFAULT_CONTACTS = listOf(
            ContactDbModel(
                1, "Enzyme",
                "0123456789",
                "",
                1,
                false,
                false,)
        )
    }
}
package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "hex") val hex: String,
    @ColumnInfo(name = "name") val name: String
) {
    companion object {
        val DEFAULT_TAGS = listOf(
            TagDbModel(1, "#194569","Mobile"),
            TagDbModel(2, "#5F84A2","Home"),
            TagDbModel(3, "#91AEC4","Work"),
            TagDbModel(4, "#B7D0E1","School"),
            TagDbModel(5, "#CADEED","Emergency"),
            TagDbModel(6, "#DBECF4","Other"),
        )
        val DEFAULT_TAG = DEFAULT_TAGS[0]
    }
}
package com.ruviapps.tacklingnephrotic.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = TableName.CaretakerTable)
data class DatabaseCareTaker(
    @PrimaryKey
    @ColumnInfo(ColumnCareTakerId)
    val ctId : String,
    @Embedded val fullName: FullName,
    @Embedded val contact : ContactInfo,

    ){
    companion object{
        const val ColumnCareTakerId = "caretaker_id"
    }
}
package com.ruviApps.tacklingnephrotic.database.entities

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(tableName = TableName.PatientTable,
  //  indices = [Index(value = [DatabasePatient.ColumnPatientCareTakerId], unique = true)],
    foreignKeys = [ForeignKey(entity = DatabaseCareTaker::class,
    parentColumns =   [DatabaseCareTaker.ColumnCareTakerId],
    childColumns = [DatabasePatient.ColumnPatientCareTakerId],
    onDelete = CASCADE)
])
data class DatabasePatient(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(ColumnPatientId)
    val patientId : Long,
    @Embedded val fullName : FullName?,
    val age : Int?,
    val weight: Float?,
    @ColumnInfo("snap_uri")
    val snapUri : String = "",
    @ColumnInfo(ColumnPatientCareTakerId,index=true)
    val patientCaretakerId : Long
){
    companion object DatabasePatientColumns{
        const val ColumnPatientId = "patient_id"
        const val ColumnPatientCareTakerId = "patient_caretaker_id"
    }
}

data class CareTakerWithPatients(
    @Embedded val caretaker: DatabaseCareTaker,
    @Relation(
        parentColumn = DatabaseCareTaker.ColumnCareTakerId,
        entityColumn = DatabasePatient.ColumnPatientCareTakerId
    )
    val patients : List<DatabasePatient>

)


data class FullName(
    @ColumnInfo("first_name")
    val firstName: String,
    @ColumnInfo("last_name")
    val lastName: String?
)

/**
 * @Transaction
@Query("SELECT * FROM CareTaker")
fun getCareTakerWithPatients(): List<CareTakerWithPatients>

 */
package com.ruviapps.tacklingnephrotic.database.entities

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import java.time.LocalDate
import java.util.*

@Entity(tableName = TableName.UrineResultTable,
    primaryKeys = [UrineResult.ColumnRecordedDate,UrineResult.ColumnPatientId] , //composite primary key
    foreignKeys = [
    ForeignKey(entity = DatabasePatient::class,
    parentColumns = [DatabasePatient.ColumnPatientId],
    childColumns = [UrineResult.ColumnPatientId],
    onDelete = CASCADE)])
data class UrineResult(
    @ColumnInfo(ColumnRecordedDate, defaultValue = "'CURRENT_TIMESTAMP'")
    val recordedDate : LocalDate,
    @ColumnInfo(ColumnResultCode)
    val resultCode : ResultCode,
    @ColumnInfo(ColumnRemarks)
    val remarks : String?,
    @ColumnInfo(ColumnPatientId,index=true)
    val urineResultOfPatientId : Long

){
    companion object{
      //  const val ColumnResultId = "result_id"
        const val ColumnPatientId = "urine_result_patient_id"
        const val ColumnRecordedDate = "recorded_date"
        const val ColumnResultCode = "result_code"
        const val ColumnRemarks = "remarks"
    }
}

data class PatientWithUrineResults(
    @Embedded val patient: DatabasePatient,
    @Relation(
        parentColumn = DatabasePatient.ColumnPatientId,
        entityColumn = UrineResult.ColumnPatientId
    )
    val urineResults : List<UrineResult>
)

//in dao usage
/*
@Transaction
@Query("Select * from Patient)
fun getPatientsWithUrineResults(): List<PatientWithUrineResults>
 */


enum class ResultCode(val value: Int){
    NEGATIVE(-1),
    TRACE(0),
    ONE_PLUS(1),
    TWO_PLUS(2),
    THREE_PLUS(3),
    FOUR_PLUS(4)
    }
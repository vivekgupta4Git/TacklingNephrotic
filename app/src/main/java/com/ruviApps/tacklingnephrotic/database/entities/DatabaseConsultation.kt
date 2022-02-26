package com.ruviApps.tacklingnephrotic.database.entities

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE
import java.util.*


@Entity(tableName = TableName.ConsultationTable, foreignKeys = [ForeignKey(
    entity = DatabasePatient::class,
    parentColumns = [DatabasePatient.ColumnPatientId],
    childColumns = [DatabaseConsultation.ColumnPatientId],
    onDelete = CASCADE
),ForeignKey(
    entity = DatabaseDoctor::class,
    parentColumns = [DatabaseDoctor.ColumnDoctorId],
    childColumns = [DatabaseConsultation.ColumnDoctorId],
    onDelete = CASCADE
)])
data class DatabaseConsultation(
    @PrimaryKey(true)
    @ColumnInfo(ColumnConsultId)
    val consultId : Long,
    @ColumnInfo(ColumnPatientId)
    val patientId : Long,
    @ColumnInfo("visit_date")
    val visitDate : Date,
    @ColumnInfo(ColumnDoctorId)
    val consultedDoctorId : Long

){
    companion object{
        const val ColumnConsultId = "consult_id"
        const val ColumnPatientId = "patient_id"
        const val ColumnDoctorId = "doctor_consulted_id"
    }
}


/**
 * One to many relationship between patient and consultations
 */

data class PatientWithConsultations(
    @Embedded
val patient : DatabasePatient,
    @Relation(
        parentColumn = DatabasePatient.ColumnPatientId,
        entityColumn = DatabaseConsultation.ColumnPatientId
    )
val consultations : List<DatabaseConsultation>
)

/**
 *@Transaction
 * @Query("Select * from Patient)
 * fun getPatientWithConsultations(): List<patientWithConsultations>
 */


// one to one relationship between Consultation and Doctor.
data class DoctorWithConsultations(
    @Embedded val doctor: DatabaseDoctor,
    @Relation(
        parentColumn = DatabaseDoctor.ColumnDoctorId,
        entityColumn = DatabaseConsultation.ColumnDoctorId
    )
    val consultations: List<DatabaseConsultation>
)

/**
 * @Transaction
 * @Query("Select * from Doctor")
 * fun getDoctorWithConsultations() : List<DoctorWithConsultations>
 */


@Entity(tableName = TableName.PrescriptionDetailsTable, foreignKeys = [ForeignKey(
    entity = DatabaseConsultation::class,
    parentColumns = [DatabaseConsultation.ColumnConsultId],
    childColumns = [PrescriptionDetails.ColumnConsultId],
    onDelete = CASCADE
),ForeignKey(
    entity = DatabaseDiseases::class,
    parentColumns = [DatabaseDiseases.ColumnDiseaseCode],
    childColumns = [PrescriptionDetails.ColumnDiseaseId]
)])
data class PrescriptionDetails(
    @PrimaryKey(true)
    @ColumnInfo(ColumnPrescriptionId)
    val prescriptionId : Long,
    @ColumnInfo(ColumnConsultId)
    val consultID:Long,
    @ColumnInfo(ColumnDiseaseId)
    val diseaseId : Long,
    val complaints : String = "",
    val diagnosis : String = "",
    val treatment : String = "",

    @ColumnInfo("next_follow_up_date")
    val nextFollowUpDate : Date

){
    companion object{
        const val ColumnPrescriptionId = "prescription_id"
        const val ColumnConsultId = "consult_id"
        const val ColumnDiseaseId = "disease_id"
    }
}

/**
 * In each Consultation  , there can be many prescription details for different diseases
 * so one to many relationship between Consultation  and Prescription Details
 */
data class ConsultationWithPrescriptionDetails(
    @Embedded
    val consultation: DatabaseConsultation,
    @Relation(
        parentColumn = DatabaseConsultation.ColumnConsultId,
        entityColumn = PrescriptionDetails.ColumnConsultId
    )
    val prescriptionDetails: List<PrescriptionDetails>
)

/**
 * @Transaction
 * @Query("Select * from ConsultationDetails")
 * fun getConsultationWithPrescriptionDetails() : List<ConsultationWithPrescriptionDetails>
 */


/**
 * There can be one Prescription Details and it can have multiple Medicine Details,
 * So one to many
 *
 * One medicine Unit can be in multiple Prescription Details
 * so one to many
 * similarly
 * One frequency unit can be in multiple prescription Details
 * so one to many
 *
 *And also,
 * one medicine can be in multiple prescription details
 * so one to many
 */


@Entity(tableName = TableName.PrescribedMedicinesTable, foreignKeys = [ForeignKey(
    entity = PrescriptionDetails::class,
    parentColumns = [PrescriptionDetails.ColumnPrescriptionId],
    childColumns = [PrescribedMedicines.ColumnPrescriptionId],
    onDelete = CASCADE
),
    ForeignKey(
        entity = MedicineUnit::class,
        parentColumns = [MedicineUnit.ColumnUnitId],
        childColumns = [PrescribedMedicines.ColumnMedicineUnitCode],
        onDelete = CASCADE
    ),

    ForeignKey(
        entity = Frequency::class,
        parentColumns = [Frequency.ColumnFrequencyCode],
        childColumns = [PrescribedMedicines.ColumnMedicineFrequencyId],
        onDelete = CASCADE
    ),

    ForeignKey(
        entity = DatabaseMedicine::class,
        parentColumns = [DatabaseMedicine.ColumnMedicineCode],
        childColumns = [PrescribedMedicines.ColumnMedicineId],
        onDelete = CASCADE
    )


])
data class PrescribedMedicines(
    @PrimaryKey(true)
    @ColumnInfo(ColumnMedicineDetailsId)
    val medicineDetailsId : Long,
    @ColumnInfo(ColumnPrescriptionId)
    val prescriptionId: Long,
    @ColumnInfo(ColumnMedicineId)
    val medicineId : Long,
    @ColumnInfo("medicine_qty")
    val medicineQty : String,
    @ColumnInfo(ColumnMedicineUnitCode)
    val medicineUnit : Long,
    @ColumnInfo(ColumnMedicineFrequencyId)
    val medicineFrequency: Long
    ){
    companion object{
        const val ColumnMedicineDetailsId = "medicine_details_id"
        const val ColumnPrescriptionId = "prescribed_id"
        const val ColumnMedicineUnitCode = "medicine_unit_code"
        const val ColumnMedicineId = "medicine_id"
        const val ColumnMedicineFrequencyId ="medicine_frequency_code"
    }
}


data class PrescriptionDetailsWithMedicineDetails(
    @Embedded
    val prescriptionDetails: PrescriptionDetails,
    @Relation(
        parentColumn = PrescriptionDetails.ColumnPrescriptionId,
        entityColumn = PrescribedMedicines.ColumnPrescriptionId
    )
    val medicine_details : List<PrescribedMedicines>
)

/**
 * @Transaction
 * @Query("Select * from PrescriptionDetails")
 * fun getPrescriptionDetailsWithMedicineDetails():
 * List<PrescriptionDetailsWithMedicineDetails>
 */

data class MedicineWithMedicineDetails(
    @Embedded val medicine: DatabaseMedicine,
    @Relation(
        parentColumn = DatabaseMedicine.ColumnMedicineCode,
        entityColumn = PrescribedMedicines.ColumnMedicineId
    )
    val medicineDetails : List<PrescribedMedicines>
)
/**
 * @Transaction
 * @Query("Select * from medicine")
 * fun getMedicineWithMedicineDetails():
 * List<MedicineWithMedicineDetails>
 */

data class MedicineUnitWithMedicineDetails(
    @Embedded val medicineUnit: MedicineUnit,
    @Relation(
        parentColumn = MedicineUnit.ColumnUnitId,
        entityColumn = PrescribedMedicines.ColumnMedicineUnitCode
    )
    val medicineDetails: List<PrescribedMedicines>
)

data class FrequencyWithMedicineDetails(
    @Embedded val medicineFrequency: Frequency,
    @Relation(
        parentColumn = Frequency.ColumnFrequencyCode,
        entityColumn = PrescribedMedicines.ColumnMedicineFrequencyId
    )
    val medicineDetails : List<PrescribedMedicines>
)
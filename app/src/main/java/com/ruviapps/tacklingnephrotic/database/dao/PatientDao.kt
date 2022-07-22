package com.ruviapps.tacklingnephrotic.database.dao

import androidx.room.*
import com.ruviapps.tacklingnephrotic.database.entities.DatabasePatient
import com.ruviapps.tacklingnephrotic.database.entities.PatientWithConsultations
import com.ruviapps.tacklingnephrotic.database.entities.PatientWithUrineResults
import com.ruviapps.tacklingnephrotic.database.entities.TableName
import com.ruviapps.tacklingnephrotic.domain.Patient

@Dao
interface PatientDao {

    @Insert(onConflict =OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient : DatabasePatient) : Long


    /**
     * Insert List of patient into database. Don't use this method if device has low resources
     *Reason : we used vararg here as a parameter here so while using this method we will be
     * using Kotlin's spread operator, which causes a full copy of the array to be created before calling a method, has
     * a very high performance penalty (and that might increase with the size of the array).
     * The Java version of vararg runs 200% faster than the seemingly equivalent Kotlin version .
     * more on given link : https://sites.google.com/a/athaydes.com/renato-athaydes/posts/kotlinshiddencosts-benchmarks
     */
    @Insert(onConflict =OnConflictStrategy.REPLACE)
    suspend fun insertAllPatient(vararg patient : DatabasePatient)

    @Query("Select * from  ${TableName.PatientTable}")
    suspend fun getAllPatients() : List<DatabasePatient>

    @Query("Select * from ${TableName.PatientTable} where ${DatabasePatient.ColumnPatientId} = :id")
    suspend fun getPatientById(id : Long) : DatabasePatient

    @Query("DELETE from ${TableName.PatientTable}")
    suspend fun deleteAllPatients()

    @Delete
    suspend fun deletePatient(databasePatient: DatabasePatient)

    @Transaction
    @Query("Select * from ${TableName.PatientTable} where ${DatabasePatient.ColumnPatientId} = :id")
    suspend fun getPatientWithResults(id : Long) :  List<PatientWithUrineResults>

    @Transaction
    @Query("Select * from ${TableName.PatientTable} where ${DatabasePatient.ColumnPatientId} = :id")
    suspend fun getAdvicesForPatient(id : Long) : List<PatientWithConsultations>

    @Query("Select * from ${TableName.PatientTable} where ${DatabasePatient.ColumnPatientCareTakerId} = :id")
    suspend fun getAllPatientForCareTaker(id : String) : List<DatabasePatient>
}
package com.ruviApps.tacklingnephrotic.database.datasources

import com.ruviApps.tacklingnephrotic.database.dto.QueryResult
import com.ruviApps.tacklingnephrotic.database.entities.CareTakerWithPatients
import com.ruviApps.tacklingnephrotic.database.entities.DatabaseCareTaker
import com.ruviApps.tacklingnephrotic.database.entities.DatabasePatient
import com.ruviApps.tacklingnephrotic.database.entities.PatientWithUrineResults

interface PatientsDataSource {

    suspend fun savePatient(databasePatient: DatabasePatient) : QueryResult<Unit>
    suspend fun getAllPatients() : QueryResult<List<DatabasePatient>>
    suspend fun getPatientById(id : Long) : QueryResult<DatabasePatient>
    suspend fun saveAllPatients(patient: List<DatabasePatient>) : QueryResult<Unit>
    suspend fun deleteAllPatients() : QueryResult<Unit>
    suspend fun getPatientsWithUrineResults(patientId : Long) : QueryResult<List<PatientWithUrineResults>>

}
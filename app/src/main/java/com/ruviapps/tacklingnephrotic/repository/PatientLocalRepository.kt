package com.ruviapps.tacklingnephrotic.repository

import android.util.Log
import com.ruviapps.tacklingnephrotic.database.dao.PatientDao
import com.ruviapps.tacklingnephrotic.database.datasources.PatientsDataSource
import com.ruviapps.tacklingnephrotic.database.dto.QueryResult
import com.ruviapps.tacklingnephrotic.database.entities.DatabasePatient
import com.ruviapps.tacklingnephrotic.database.entities.PatientWithConsultations
import com.ruviapps.tacklingnephrotic.database.entities.PatientWithUrineResults
import com.ruviapps.tacklingnephrotic.domain.Patient
import com.ruviapps.tacklingnephrotic.extension.toDomainPatient
import com.ruviapps.tacklingnephrotic.utility.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.Exception

class PatientLocalRepository @Inject constructor(
    val patientDao: PatientDao,
    @IoDispatcher val ioDispatcher : CoroutineDispatcher = Dispatchers.IO
) : PatientsDataSource{
    override suspend fun savePatient(databasePatient: DatabasePatient): QueryResult<Long>
    = withContext(ioDispatcher){
        return@withContext try {
            QueryResult.Success(patientDao.insertPatient(databasePatient))
        }catch (ex : Exception)
        {
            QueryResult.Error(ex.localizedMessage) }
    }

    override suspend fun getAllPatients(): QueryResult<List<DatabasePatient>> = withContext(ioDispatcher){
        return@withContext try {
            QueryResult.Success(patientDao.getAllPatients())
        }catch (ex : Exception)
        {  QueryResult.Error(ex.localizedMessage) }
    }

    override suspend fun getPatientById(id: Long): QueryResult<DatabasePatient> = withContext(ioDispatcher){
        return@withContext try {
            QueryResult.Success(patientDao.getPatientById(id))
        }catch (ex : Exception)
        {  QueryResult.Error(ex.localizedMessage) }
    }

    override suspend fun saveAllPatients(patient: List<DatabasePatient>): QueryResult<Unit> = withContext(ioDispatcher){
        return@withContext try {
            QueryResult.Success(patientDao.insertAllPatient(*patient.toTypedArray()))
        }catch (ex : Exception)
        {  QueryResult.Error(ex.localizedMessage )}
    }

       override suspend fun deleteAllPatients(): QueryResult<Unit> = withContext(ioDispatcher){
        return@withContext try{
            QueryResult.Success(patientDao.deleteAllPatients())
        }catch (ex  :Exception){
            QueryResult.Error(ex.localizedMessage)
        }
    }

    override suspend fun deletePatient(databasePatient: DatabasePatient): QueryResult<Unit> = withContext(ioDispatcher){
        return@withContext try{
            QueryResult.Success(patientDao.deletePatient(databasePatient))
        }catch (e : Exception){
            QueryResult.Error(e.localizedMessage)
        }

    }

    override suspend fun getPatientsWithUrineResults(patientId: Long): QueryResult<List<PatientWithUrineResults>> = withContext(ioDispatcher){
        return@withContext try{
            QueryResult.Success(patientDao.getPatientWithResults(patientId))
        }catch (ex : Exception){
            QueryResult.Error(ex.localizedMessage)
        }
    }

    override suspend fun getPatientsAllAdvices(patientId: Long): QueryResult<List<PatientWithConsultations>> {
        return withContext(ioDispatcher){
            try{
                QueryResult.Success(patientDao.getAdvicesForPatient(patientId))
            }catch (ex : Exception)
            {
                QueryResult.Error(ex.localizedMessage)
            }
        }
    }

    override suspend fun getAllPatientForCareTaker(careTakerId: String): QueryResult<List<Patient>> {
    return withContext(ioDispatcher){
        try {
            QueryResult.Success(patientDao.getAllPatientForCareTaker(careTakerId).toDomainPatient())
        }catch (ex : Exception){
            QueryResult.Error(ex.localizedMessage)
        }
    }
    }

}
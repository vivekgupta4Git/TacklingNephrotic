package com.ruviapps.tacklingnephrotic.database.datasources

import com.ruviapps.tacklingnephrotic.database.dto.QueryResult
import com.ruviapps.tacklingnephrotic.database.entities.CareTakerWithPatients
import com.ruviapps.tacklingnephrotic.database.entities.DatabaseCareTaker

interface CareTakerDataSource {

    suspend fun getCareTakers() : QueryResult<List<DatabaseCareTaker>>
    suspend fun getCareTaker(id : Long) : QueryResult<DatabaseCareTaker>
    suspend fun saveCareTaker(careTaker: DatabaseCareTaker) : QueryResult<Long>
    suspend fun saveAllCareTakers(caretakers : List<DatabaseCareTaker>) : QueryResult<List<Long>>
    suspend fun deleteCareTaker(careTaker: DatabaseCareTaker): QueryResult<Unit>
    suspend fun deleteAllCareTakers() : QueryResult<Unit>
    suspend fun getPatientsOfCareTaker(id : Long) : QueryResult<List<CareTakerWithPatients>>
    suspend fun getCareTakerWithPatients() : QueryResult<List<CareTakerWithPatients>>
}
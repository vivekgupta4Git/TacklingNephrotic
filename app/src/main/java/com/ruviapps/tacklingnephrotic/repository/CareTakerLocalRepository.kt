package com.ruviapps.tacklingnephrotic.repository

import com.ruviapps.tacklingnephrotic.database.dao.CareTakerDao
import com.ruviapps.tacklingnephrotic.database.datasources.CareTakerDataSource
import com.ruviapps.tacklingnephrotic.database.dto.QueryResult
import com.ruviapps.tacklingnephrotic.database.entities.CareTakerWithPatients
import com.ruviapps.tacklingnephrotic.database.entities.DatabaseCareTaker
import com.ruviapps.tacklingnephrotic.utility.IoDispatcher
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject


/**
 * An abstraction over Dao
 */
class CareTakerLocalRepository @Inject constructor(
     val careTakerDao: CareTakerDao,
  @IoDispatcher   val ioDispatcher : CoroutineDispatcher = Dispatchers.IO
) : CareTakerDataSource{

    override suspend fun getCareTakers(): QueryResult<List<DatabaseCareTaker>> = withContext(ioDispatcher){
        return@withContext try { QueryResult.Success(careTakerDao.getAllCareTakers())
        }catch (ex : Exception)
        {  QueryResult.Error(ex.localizedMessage) }
    }

    override suspend fun getCareTaker(id :String) : QueryResult<DatabaseCareTaker> = withContext(ioDispatcher){
      return@withContext try { QueryResult.Success(careTakerDao.getCareTakerById(id))
      }catch (ex : Exception)
      {  QueryResult.Error(ex.localizedMessage) }
  }

    override suspend fun saveCareTaker(careTaker: DatabaseCareTaker) : QueryResult<Unit> =
        withContext(ioDispatcher){
            return@withContext try {
                QueryResult.Success(careTakerDao.insertCareTaker(careTaker),"Insert Successfully!")
            }catch (ex : Exception)
            {
                QueryResult.Error(ex.localizedMessage)
            }
        }

    override suspend fun deleteCareTaker(careTaker: DatabaseCareTaker): QueryResult<Unit> =
        withContext(ioDispatcher){
            return@withContext try{
                QueryResult.Success(careTakerDao.deleteCareTaker(careTaker),"CareTaker Deleted")
            }catch (ex : Exception){
                QueryResult.Error(ex.localizedMessage)
            }
        }

    override suspend fun deleteAllCareTakers() : QueryResult<Unit> =
        withContext(ioDispatcher) {
          return@withContext  try {
            QueryResult.Success(careTakerDao.deleteAllCareTaker(),"All Records Deleted!")
            }catch (ex : Exception){
                QueryResult.Error(ex.localizedMessage)
            }
        }


    override suspend fun saveAllCareTakers(caretakers: List<DatabaseCareTaker>)
    =withContext(ioDispatcher){
        return@withContext try {
            QueryResult.Success(careTakerDao.insertAllCareTakers(*caretakers.toTypedArray()))
        }catch (ex : Exception)
        {
            QueryResult.Error(ex.localizedMessage)
        }
    }

    override suspend fun getPatientsOfCareTaker(id: String): QueryResult<List<CareTakerWithPatients>> =
        withContext(ioDispatcher){
            return@withContext try{
                QueryResult.Success(careTakerDao.patientsOfCareTaker(id))
            }catch (ex : Exception)
            {
                QueryResult.Error(ex.localizedMessage,ex.hashCode())

            }

        }

    override suspend fun getCareTakerWithPatients(): QueryResult<List<CareTakerWithPatients>> =
        withContext(ioDispatcher){
            return@withContext try{
                QueryResult.Success(careTakerDao.getListOfCareTakersWithPatients())
            }catch (ex : Exception)
            {
                QueryResult.Error(ex.localizedMessage,ex.hashCode())

            }

        }

}
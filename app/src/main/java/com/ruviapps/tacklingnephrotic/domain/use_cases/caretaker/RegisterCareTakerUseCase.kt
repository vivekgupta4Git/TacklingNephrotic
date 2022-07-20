package com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker

import android.content.Context
import com.ruviapps.tacklingnephrotic.R
import com.ruviapps.tacklingnephrotic.database.dto.QueryResult
import com.ruviapps.tacklingnephrotic.domain.CareTaker
import com.ruviapps.tacklingnephrotic.extension.toDatabaseCareTaker
import com.ruviapps.tacklingnephrotic.repository.CareTakerLocalRepository
import javax.inject.Inject


class RegisterCareTakerUseCase @Inject constructor(
    private val repository: CareTakerLocalRepository,
) {

    suspend operator fun invoke(
        uid : String,
        name: String,
        email: String = "",
        primaryContact: String,
        secondaryContact: String?
        ): QueryResult<Unit> {

        if(name.isBlank()||name.isEmpty())
            return QueryResult.Error("Name is not Valid", VALIDATION_ERROR)

        if(!isValidMail(email))
            return QueryResult.Error("Email is not Valid", VALIDATION_ERROR)

        if(! isValidMobile(primaryContact)  )
            return QueryResult.Error("Contact Number is not valid", VALIDATION_ERROR)

        if(!secondaryContact.isNullOrEmpty()){
            if(!isValidMobile(secondaryContact))
            {
                return QueryResult.Error("Contact is not valid", VALIDATION_ERROR)
            }
        }
        val  careTaker = CareTaker(uid,name,email,primaryContact,secondaryContact)

     /*   if(! IsValidCareTaker(repository).invoke(uid))
           return QueryResult.Error("Cannot save careTaker",
               VALIDATION_ERROR)
*/
        return repository.saveCareTaker(careTaker.toDatabaseCareTaker())
    }

    companion object{
        const val VALIDATION_ERROR=101
      }


    private fun isValidMobile(phone :String) : Boolean {
       return android.util.Patterns.PHONE.matcher(phone).matches()
     }


    private fun isValidMail(email: String): Boolean {
      return  android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
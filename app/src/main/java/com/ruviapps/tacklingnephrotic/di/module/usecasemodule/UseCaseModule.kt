package com.ruviapps.tacklingnephrotic.di.module.usecasemodule

import com.ruviapps.tacklingnephrotic.di.module.AppModule
import com.ruviapps.tacklingnephrotic.domain.use_cases.caretaker.*
import com.ruviapps.tacklingnephrotic.domain.use_cases.patient.*
import com.ruviapps.tacklingnephrotic.domain.use_cases.result.*
import com.ruviapps.tacklingnephrotic.domain.use_cases.stateCalculator.CalculateStateFromReadingUseCase
import com.ruviapps.tacklingnephrotic.domain.use_cases.stateCalculator.CalculateStateUseCases
import com.ruviapps.tacklingnephrotic.repository.CareTakerLocalRepository
import com.ruviapps.tacklingnephrotic.repository.PatientLocalRepository
import com.ruviapps.tacklingnephrotic.repository.ResultLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped


@InstallIn(ActivityComponent::class)
@Module

object UseCaseModule {

    @Provides
    @ActivityScoped
    fun provideResultUseCase(repo: ResultLocalRepository) : UrineResultUseCases{
        return UrineResultUseCases(
            insertUrineResultUseCase = InsertUrineResultUseCase(repo),
            deleteAllResultUseCase = DeleteAllResultUseCase(repo),
            deleteUrineResultUseCase = DeleteUrineResultUseCase(repo),
            updateUrineResultUseCase = UpdateUrineResultUseCase(repo),
            resultsOfAllPatientUseCase = ResultsOfAllPatientUseCase(repo),
            allResultForPatientUseCase = AllResultForPatientUseCase(repo),
            getReadingsByIdUseCase = GetReadingByIdUseCase(repo),
            getReadingsByDateUseCase = GetReadingByDateUseCase(repo),
            getMissedReadingDatesUseCase = GetMissedReadingDatesUseCase(repo)
            )
        }


    @Provides
    @ActivityScoped
    fun providesPatientUseCase(repo: PatientLocalRepository,resourcesProvider: AppModule.ResourcesProvider) : PatientUseCases{
        return PatientUseCases(
            addPatientUseCase = AddPatientUseCase(repo,resourcesProvider),
            removePatientUseCase = RemovePatientUseCase(repo),
            getPatientUseCases = GetPatientUseCase(repo),
            getAllPatientForCareTakerUseCase = GetAllPatientForCareTakerUseCase(repo)
        )
    }

    @Provides
    @ActivityScoped
    fun provideCalculateStateUseCase(repo: ResultLocalRepository) : CalculateStateUseCases{
        return CalculateStateUseCases(
            calculateStateFromReadingUseCae = CalculateStateFromReadingUseCase(repo =repo )
        )
    }

    @Provides
    @ActivityScoped
    fun provideCareTakerUseCase(repo: CareTakerLocalRepository) : CareTakerUseCases{
        return CareTakerUseCases(
            registerCareTakerUseCase = RegisterCareTakerUseCase(repo),
            getCareTakerUseCase = GetCareTakerUseCase(repo),
            getAllCareTakersUseCase = GetAllCareTakersUseCase(repo),
            patientsOfCareTakerUseCase = PatientsOfCareTakerUseCase(repo),
            deleteAllCareTakerUseCase = DeleteAllCareTakerUseCase(repo),
            deleteCareTakerUseCases = DeleteCareTakerUseCases(repo),
            listOfCareTakersWithPatients = ListOfCareTakersWithPatients(repo),
            isValidCareTaker =  IsValidCareTaker(repo)
        )
    }



}


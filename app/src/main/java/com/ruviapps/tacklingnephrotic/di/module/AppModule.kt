package com.ruviapps.tacklingnephrotic.di.module

import android.content.Context
import androidx.room.Room
import com.ruviapps.tacklingnephrotic.database.NephSyndDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): NephSyndDatabase {
        return Room.databaseBuilder(
            appContext,
            NephSyndDatabase::class.java,
            "nephDatabase"
        ).build()
    }



}
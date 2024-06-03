package com.sudipto_fahad.knowit.di

import android.content.Context
import com.sudipto_fahad.knowit.data.UserPref
import com.sudipto_fahad.knowit.data.UserPrefImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PreferenceModule {

    @Singleton
    @Provides
    fun providesUserPreferences(
        @ApplicationContext context: Context
    ): UserPref = UserPrefImpl(context)
}
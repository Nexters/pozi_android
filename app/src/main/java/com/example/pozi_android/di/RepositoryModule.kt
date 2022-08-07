package com.example.pozi_android.di

import com.example.pozi_android.data.remote.spec.address.GeoService
import com.example.pozi_android.data.repository.geo.GeoRepositoryImpl
import com.example.pozi_android.data.repository.pb.PBInfoRepositoryImpl
import com.example.pozi_android.domain.repository.geo.GeoRepository
import com.example.pozi_android.domain.repository.pb.PBInfoRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providePBInfoRepository(
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        firestore: FirebaseFirestore
    ): PBInfoRepository = PBInfoRepositoryImpl(ioDispatcher, firestore)

    @Singleton
    @Provides
    fun provideGeoRepository(
        api: GeoService,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher
    ): GeoRepository = GeoRepositoryImpl(api, ioDispatcher)

}
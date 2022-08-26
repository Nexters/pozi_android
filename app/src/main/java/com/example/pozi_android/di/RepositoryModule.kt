package com.example.pozi_android.di

import com.example.pozi_android.data.remote.network.RetrofitInterface
import com.example.pozi_android.data.repository.firebase.PBInfoRepositoryImpl
import com.example.pozi_android.domain.repository.PBInfoRepository
import com.example.pozi_android.domain.repository.ServiceRepository
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

//    @Singleton
//    @Provides
//    fun provideServiceRepository(
//        retrofitInterface: RetrofitInterface,
//        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher
//    ): ServiceRepository =
//        ServiceRepositoryImpl(retrofitInterface, ioDispatcher)
}
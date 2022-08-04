package com.example.pozi_android.di

import com.example.pozi_android.data.repository.pb.PBInfoRepositoryImpl
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
//    @Singleton
//    @Binds
//    @Nonnull
//    abstract fun providePBInfoRepository(impl: PBInfoRepositoryImpl): PBInfoRepository

    @Singleton
    @Provides
    fun providePBInfoRepository(
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher,
        firestore: FirebaseFirestore
    ): PBInfoRepositoryImpl = PBInfoRepositoryImpl(ioDispatcher, firestore)

}
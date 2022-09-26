package com.maestrovs.slovo.data.repository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val remoteData : MainRemoteData) {
    suspend fun getSlovo(slovo: String) = remoteData.getSlovo(slovo)
}
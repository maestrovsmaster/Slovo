package com.maestrovs.slovo.data.repository

import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService : MainService) {

    suspend fun getSlovo(slovo: String) = mainService.getSlovo(slovo)
}
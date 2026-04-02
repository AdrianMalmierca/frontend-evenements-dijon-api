package com.adrianmalmierca.dijonevents.data.api

import com.adrianmalmierca.dijonevents.data.model.*
import retrofit2.http.*

interface DijonEventsApi {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @GET("api/events")
    suspend fun getEvents(
        @Query("size") size: Int = 20,
        @Query("from") from: Int = 0,
        @Query("keyword") keyword: String? = null
    ): List<EventDto>

    @GET("api/events/{uid}")
    suspend fun getEventById(@Path("uid") uid: String): EventDto

    @GET("api/events/favorites")
    suspend fun getFavorites(@Header("Authorization") token: String): List<EventDto>

    @POST("api/events/favorites")
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Body request: FavoriteRequest
    )

    @DELETE("api/events/favorites/{uid}")
    suspend fun removeFavorite(
        @Header("Authorization") token: String,
        @Path("uid") uid: String
    )
}

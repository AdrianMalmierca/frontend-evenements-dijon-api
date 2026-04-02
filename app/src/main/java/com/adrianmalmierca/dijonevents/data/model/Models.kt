package com.adrianmalmierca.dijonevents.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EventDto(
    val uid: String,
    val title: String,
    val description: String?,
    val imageUrl: String?,
    val locationName: String?,
    val address: String?,
    val city: String?,
    val latitude: Double?,
    val longitude: Double?,
    val dateStart: String?,
    val dateEnd: String?,
    val categories: List<String>
)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val token: String,
    val email: String,
    val name: String
)

@JsonClass(generateAdapter = true)
data class FavoriteRequest(
    val uid: String,
    val title: String,
    val imageUrl: String?,
    val dateStart: String?,
    val latitude: Double?,
    val longitude: Double?
)

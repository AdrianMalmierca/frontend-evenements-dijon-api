package com.adrianmalmierca.dijonevents.data.repository

import com.adrianmalmierca.dijonevents.data.api.DijonEventsApi
import com.adrianmalmierca.dijonevents.data.model.EventDto
import com.adrianmalmierca.dijonevents.data.model.FavoriteRequest
import com.adrianmalmierca.dijonevents.util.Result
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val api: DijonEventsApi,
    private val tokenManager: TokenManager
) {
    suspend fun getEvents(size: Int = 20, keyword: String? = null): Result<List<EventDto>> {
        return try {
            Result.Success(api.getEvents(size = size, keyword = keyword))
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error charging the events")
        }
    }

    suspend fun getFavorites(): Result<List<EventDto>> {
        return try {
            val token = tokenManager.token.first() ?: return Result.Error("Not authenticated")
            Result.Success(api.getFavorites(tokenManager.bearerToken(token)))
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error charging the events")
        }
    }

    suspend fun addFavorite(event: EventDto): Result<Unit> {
        return try {
            val token = tokenManager.token.first() ?: return Result.Error("Not authenticated")
            api.addFavorite(
                tokenManager.bearerToken(token),
                FavoriteRequest(
                    uid = event.uid,
                    title = event.title,
                    imageUrl = event.imageUrl,
                    dateStart = event.dateStart,
                    latitude = event.latitude,
                    longitude = event.longitude
                )
            )
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error adding favourite event")
        }
    }

    suspend fun removeFavorite(uid: String): Result<Unit> {
        return try {
            val token = tokenManager.token.first() ?: return Result.Error("Not authenticated")
            api.removeFavorite(tokenManager.bearerToken(token), uid)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error removing favourite event")
        }
    }
}

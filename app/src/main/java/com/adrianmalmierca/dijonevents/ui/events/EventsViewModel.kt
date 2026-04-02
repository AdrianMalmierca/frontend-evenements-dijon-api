package com.adrianmalmierca.dijonevents.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrianmalmierca.dijonevents.data.model.EventDto
import com.adrianmalmierca.dijonevents.data.repository.EventRepository
import com.adrianmalmierca.dijonevents.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EventsUiState( //dataclass cause we need copy
    val events: List<EventDto> = emptyList(),
    val favorites: List<EventDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel //the vm is created by hilt and we can inject dependencies automatically
class EventsViewModel @Inject constructor(
    private val eventRepository: EventRepository //Hilt give us an instance of EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventsUiState()) //only the vm can modify
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow() //the ui can observe

    init {
        loadEvents()
        loadFavorites()
    }

    fun loadEvents(keyword: String? = null) {
        viewModelScope.launch { //coroutine linked to the lifecycle of the vm
            _uiState.value = _uiState.value.copy(isLoading = true) //spiner in the UI
            when (val result = eventRepository.getEvents(keyword = keyword)) {
                is Result.Success -> _uiState.value = _uiState.value.copy( //copy cause the state is inmutable
                    events = result.data, isLoading = false
                )
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    error = result.message, isLoading = false
                )
                else -> {}
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            when (val result = eventRepository.getFavorites()) {
                is Result.Success -> _uiState.value = _uiState.value.copy(favorites = result.data)
                else -> {}
            }
        }
    }

    fun toggleFavorite(event: EventDto) {
        viewModelScope.launch {
            val isFav = _uiState.value.favorites.any { it.uid == event.uid }
            if (isFav) {
                eventRepository.removeFavorite(event.uid)
                _uiState.value = _uiState.value.copy(
                    favorites = _uiState.value.favorites.filter { it.uid != event.uid }
                )
            } else {
                eventRepository.addFavorite(event)
                _uiState.value = _uiState.value.copy(
                    favorites = _uiState.value.favorites + event
                )
            }
        }
    }

    fun isFavorite(uid: String) = _uiState.value.favorites.any { it.uid == uid }
}

package com.example.api

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.Data.Model.Game
import com.example.api.Data.Network.ApiService
import com.example.api.Data.Network.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel

class MainViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {
    private val _games = MutableStateFlow<List<Game>>(emptyList())
    val games: StateFlow<List<Game>> get() = _games

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun fetchGames(category: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getGames(category)
                if (response.isSuccessful) {
                    _games.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: HttpException) {
                _error.value = "HttpException: ${e.message}"
            } catch (e: IOException) {
                _error.value = "IOException: ${e.message}"
            }
        }
    }
}

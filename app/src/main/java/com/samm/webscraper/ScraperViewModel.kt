package com.samm.webscraper

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.lang.Exception

class ScraperViewModel(private val repository: Repository): ViewModel() {

    var _state: MutableStateFlow<StockState?> = MutableStateFlow(null)
    val state = _state.asStateFlow()

    fun getData(symbol: String) = viewModelScope.launch(Dispatchers.IO) {
        _state.value = StockState(loading = true)
        try {
            val result = repository.scrapeData(symbol)
            _state.value = StockState(loading = false, data = result)
        } catch (e: Exception) {
           _state.value = StockState(error = e.localizedMessage ?: "Unexpected Error")
        }
    }
}
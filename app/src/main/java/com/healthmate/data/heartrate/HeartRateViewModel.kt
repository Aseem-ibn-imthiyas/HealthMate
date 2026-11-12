
package com.healthmate.data.heartrate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HeartRateViewModel : ViewModel() {

    private val _heartRate = MutableStateFlow<HeartRateReading?>(null)
    val heartRate: StateFlow<HeartRateReading?> = _heartRate.asStateFlow()

    init {
        viewModelScope.launch {
            FakeHeartRateApi.observeHeartRate().collect { reading ->
                _heartRate.value = reading
            }
        }
    }
}

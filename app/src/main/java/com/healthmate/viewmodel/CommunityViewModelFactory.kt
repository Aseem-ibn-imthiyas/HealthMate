package com.healthmate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.healthmate.data.repository.CommunityRepository

class CommunityViewModelFactory(
    private val repository: CommunityRepository,
    private val currentUserId: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            return CommunityViewModel(
                repository = repository,
                currentUserId = currentUserId
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
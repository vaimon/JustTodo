package ru.mmcs.justtodo

import androidx.lifecycle.MutableLiveData

object Utils {
    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}
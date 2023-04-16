package ru.mmcs.justtodo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mmcs.justtodo.databinding.FragmentDetailsBinding
import ru.mmcs.justtodo.models.Task

class DetailsFragmentViewModel(var binding: FragmentDetailsBinding?, task: Task) : ViewModel() {
    public val task: LiveData<Task> = MutableLiveData(task)

}
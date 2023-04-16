package ru.mmcs.justtodo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mongodb.kbson.ObjectId
import ru.mmcs.justtodo.databinding.FragmentDetailsBinding
import ru.mmcs.justtodo.models.Task
import ru.mmcs.justtodo.repositories.TaskDetailsRepository

class DetailsFragmentViewModel(var binding: FragmentDetailsBinding?, taskid: String) : ViewModel() {
    val task: MutableLiveData<Task> = MutableLiveData()
    private val repository = TaskDetailsRepository(this)

    init {
        repository.getTask(taskid)
    }

    override fun onCleared() {
        repository.detach()
        super.onCleared()
    }
}
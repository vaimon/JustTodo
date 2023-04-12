package ru.mmcs.justtodo.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.mmcs.justtodo.Utils.notifyObserver
import ru.mmcs.justtodo.adapters.TaskListRvAdapter
import ru.mmcs.justtodo.databinding.FragmentListBinding
import ru.mmcs.justtodo.models.Task

class ListFragmentViewModel(var binding: FragmentListBinding?) : ViewModel() {
    private val listItems = mutableListOf<Task>()
    val taskCount
        get() = listItems.size.toString()
    val completedCount
        get() = listItems.filter { t -> t.isDone }.size.toString()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    val rvListeners = object : TaskListRvAdapter.OnItemInteractionListener {
        override fun onBtnRemoveClicked(item: Task, position: Int) {
            listItems.removeAt(position)
            rvAdapter.notifyItemRemoved(position)

        }

        override fun onItemSelected(item: Task, position: Int) {
            listItems.set(position, item.apply { isDone = !isDone })
            rvAdapter.notifyItemChanged(position)
        }
    }

    private val rvAdapter: TaskListRvAdapter = TaskListRvAdapter(listItems, rvListeners)

    init {
        binding?.rvTasklist?.apply {
            adapter = rvAdapter
        }

        binding?.btnAdd?.setOnClickListener {
            onBtnAddClick()
        }
    }

    fun onBtnAddClick() {
        Log.d("DEBUG_RV", "State start: ${_uiState.value.isDialogShowing}")
        _uiState.update { currentUiState ->
            currentUiState.copy(isDialogShowing = true)
        }
    }

    fun onDialogShown(task: Task? = null) {
        _uiState.update { currentUiState ->
            currentUiState.copy(isDialogShowing = false)
        }
        Log.d("DEBUG_RV", "State end: ${_uiState.value.isDialogShowing}")
        if (task == null)
            return
        listItems.add(task)
        rvAdapter.notifyItemInserted(listItems.size - 1)
    }

    data class UiState(val isDialogShowing: Boolean = false)
}
package ru.mmcs.justtodo.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.mongodb.kbson.ObjectId
import ru.mmcs.justtodo.adapters.TaskListRvAdapter
import ru.mmcs.justtodo.databinding.FragmentListBinding
import ru.mmcs.justtodo.models.Task
import ru.mmcs.justtodo.repositories.TaskListRepository

class ListFragmentViewModel(var binding: FragmentListBinding?) : ViewModel() {
    private val repository = TaskListRepository(this)
    private val listItems = mutableListOf<Task>()
    val taskCount = ObservableField("0")
    val completedCount = ObservableField("0")

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    val rvListeners = object : TaskListRvAdapter.OnItemInteractionListener {
        override fun onBtnRemoveClicked(item: Task, position: Int) {
            listItems.removeAt(position)
            rvAdapter.notifyItemRemoved(position)
            rvAdapter.notifyItemRangeChanged(position,rvAdapter.getItemCount());
            taskCount.set(rvAdapter.itemCount.toString())
        }

        override fun onItemSelected(item: Task, position: Int) {
            listItems.set(position, item.apply { isDone = !isDone })
            rvAdapter.notifyItemChanged(position)
            completedCount.set(listItems.filter { t -> t.isDone }.size.toString())
        }

        override fun onItemClicked(item: Task) {
            onListItemClicked(item)
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
        _uiState.update { currentUiState ->
            currentUiState.copy(isDialogShowing = true)
        }
    }

    fun onListItemClicked(task: Task?){
        _uiState.update { currentUiState ->
            currentUiState.copy(navigationTarget = task?._id)
        }
    }

    fun onDialogShown(task: Task? = null) {
        if (task == null)
            return
        listItems.add(task)
        rvAdapter.notifyItemInserted(listItems.size - 1)
        taskCount.set(rvAdapter.itemCount.toString())
    }

    fun onDialogOpened() {
        _uiState.update { currentUiState ->
            currentUiState.copy(isDialogShowing = false)
        }
    }

    fun onDetailsFragmentOpened(){
        _uiState.update { currentUiState ->
            currentUiState.copy(navigationTarget = null)
        }
    }

    data class UiState(val isDialogShowing: Boolean = false, val navigationTarget: ObjectId? = null)
}
package ru.mmcs.justtodo.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import ru.mmcs.justtodo.adapters.TaskListRvAdapter
import ru.mmcs.justtodo.databinding.FragmentListBinding
import ru.mmcs.justtodo.models.Task
import ru.mmcs.justtodo.repositories.TaskListRepository

class ListFragmentViewModel(var binding: FragmentListBinding?) : ViewModel() {
    private val repository = TaskListRepository(this)
    val taskList: MutableLiveData<List<Task>> = MutableLiveData(listOf())
    val dataStatus: MutableLiveData<Pair<DataStatus, Int>> = MutableLiveData(DataStatus.None to -1)
    val taskCount = ObservableField("0")
    val completedCount = ObservableField("0")

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val rvListeners = object : TaskListRvAdapter.OnItemInteractionListener {
        override fun onBtnRemoveClicked(item: Task, position: Int) {
            viewModelScope.launch {
                repository.deleteTask(item)
            }
        }

        override fun onItemSelected(item: Task, position: Int) {
            viewModelScope.launch {
                repository.toggleTaskStatus(item._id, !item.isDone)
            }
        }

        override fun onItemClicked(item: Task) {
            onListItemClicked(item)
        }
    }

    private val rvAdapter: TaskListRvAdapter = TaskListRvAdapter(taskList, rvListeners)

    init {
        binding?.rvTasklist?.apply {
            adapter = rvAdapter
        }

        binding?.btnAdd?.setOnClickListener {
            onBtnAddClick()
        }

        taskList.observeForever {
            taskCount.set(it.size.toString())
            completedCount.set(it.filter { t -> t.isDone }.size.toString())
        }

        dataStatus.observeForever {
            when(it.first){
                DataStatus.Received -> onTasksReceived()
                DataStatus.Changed -> onTaskChanged(it.second)
                DataStatus.Added -> onTaskAdded(it.second)
                DataStatus.Removed -> onTaskRemoved(it.second)
                else -> {}
            }
        }
        repository.subscribeToTasksUpdates()
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

    fun onTaskAdded(position: Int){
        rvAdapter.notifyItemInserted(position)
        taskCount.set(rvAdapter.itemCount.toString())
    }
    fun onTaskRemoved(position: Int){
        rvAdapter.notifyItemRemoved(position)
        rvAdapter.notifyItemRangeChanged(position,rvAdapter.getItemCount());
        taskCount.set(rvAdapter.itemCount.toString())
    }
    fun onTasksReceived(){
        rvAdapter.notifyItemRangeChanged(0,rvAdapter.getItemCount());
    }

    fun onTaskChanged(position: Int){
        rvAdapter.notifyItemChanged(position)
    }

    fun onDialogShown(task: Task? = null) {
        if (task == null)
            return
        viewModelScope.launch {
            repository.putTask(task)
        }
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

    override fun onCleared() {
        super.onCleared()
        repository.detach()
    }

    data class UiState(val isDialogShowing: Boolean = false, val navigationTarget: ObjectId? = null)

    enum class DataStatus{ None, Received, Changed, Added, Removed }
}
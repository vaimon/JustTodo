package ru.mmcs.justtodo.repositories

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.*
import org.mongodb.kbson.ObjectId
import ru.mmcs.justtodo.models.Task
import ru.mmcs.justtodo.viewmodels.ListFragmentViewModel

class TaskListRepository(private val viewModel: ListFragmentViewModel) {
    private val realm = Realm.open(RealmConfiguration.create(schema = setOf(Task::class)))
    private lateinit var dbReferenceJob: Job
    fun subscribeToTasksUpdates(){
        val query = realm.query(Task::class)
        // flow.collect() is blocking -- run it in a background context
        dbReferenceJob = CoroutineScope(Dispatchers.Default).launch {
            // create a Flow from that collection, then add a listener to the Flow
            val charactersFlow = query.asFlow()
            val subscription = charactersFlow.collect { changes: ResultsChange<Task> ->
                when (changes) {
                    is UpdatedResults -> {
                            viewModel.taskList.postValue(changes.list)
                            if(changes.insertions.isNotEmpty()){
                                viewModel.dataStatus.postValue(ListFragmentViewModel.DataStatus.Added to changes.insertions.first())
                            }
                            if(changes.changes.isNotEmpty()){
                                viewModel.dataStatus.postValue(ListFragmentViewModel.DataStatus.Changed to changes.changes.first())
                            }
                            if(changes.deletions.isNotEmpty()){
                                viewModel.dataStatus.postValue(ListFragmentViewModel.DataStatus.Removed to changes.deletions.first())
                            }
                    }
                    is InitialResults ->{
                        viewModel.taskList.postValue(changes.list)
                        viewModel.dataStatus.postValue(ListFragmentViewModel.DataStatus.Received to -1)
                    }
                    else -> {
                        Log.i("REALM_SERVICE","Undefined collection in flow")
                    }
                }
            }
        }
    }

    suspend fun toggleTaskStatus(taskId: ObjectId, isDone: Boolean){
        realm.write {
            val task: Task? =
                this.query(Task::class,"_id == oid(${taskId.toHexString()})").first().find()
            task?.isDone = isDone
        }
    }

    suspend fun putTask(task: Task){
        realm.write { this.copyToRealm(task) }
    }

    fun getTask(id: ObjectId) : Task?{
        return realm.query(Task::class, "_id == oid(${id.toHexString()})").first().find()
    }

    suspend fun deleteTask(task: Task){
        realm.write {
            val writeTransactionItems = query(Task::class, "_id == oid(${task._id.toHexString()})").find()
            delete(writeTransactionItems.first())
        }
    }

    fun detach(){
        realm.close()
        dbReferenceJob.cancel()
    }

}
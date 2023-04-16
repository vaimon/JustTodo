package ru.mmcs.justtodo.repositories

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.notifications.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import ru.mmcs.justtodo.models.Task
import ru.mmcs.justtodo.viewmodels.DetailsFragmentViewModel
import ru.mmcs.justtodo.viewmodels.ListFragmentViewModel

class TaskDetailsRepository(private val viewModel: DetailsFragmentViewModel) {
    private val realm = Realm.open(RealmConfiguration.create(schema = setOf(Task::class)))
    private lateinit var dbReferenceJob: Job

    fun getTask(id: String) {
        val query = realm.query(Task::class, "_id == oid(${id})").first()
        dbReferenceJob = CoroutineScope(Dispatchers.Default).launch {
            val charactersFlow = query.asFlow()
            val subscription = charactersFlow.collect { change: SingleQueryChange<Task> ->
                if (change is InitialObject) {
                    viewModel.task.postValue(change.obj)
                }
            }
        }
    }

    fun detach(){
        realm.close()
        dbReferenceJob.cancel()
    }
}
package ru.mmcs.justtodo.repositories

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.mongodb.kbson.ObjectId
import ru.mmcs.justtodo.models.Task
import ru.mmcs.justtodo.viewmodels.ListFragmentViewModel

class TaskListRepository(val viewModel: ListFragmentViewModel) {
    private val realm = Realm.open(RealmConfiguration.create(schema = setOf(Task::class)))

    fun getTaskList(): List<Task>{
        return realm.query(Task::class).find()
    }

    suspend fun putTask(task: Task){
        realm.write { this.copyToRealm(task) }
    }

    fun getTask(id: ObjectId) : Task?{
        return realm.query(Task::class, "_id == $id").first().find()
    }

    suspend fun deleteTask(task: Task){
        realm.write {
            val writeTransactionItems = query<Task>(Task::class, "_id == ${task._id}").find()
            delete(writeTransactionItems.first())
        }
    }

    fun detach(){
        realm.close()
    }

}
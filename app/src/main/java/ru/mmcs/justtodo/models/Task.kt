package ru.mmcs.justtodo.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Task(var title: String,
           var description: String,
           var isDone: Boolean) : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    constructor() : this("","",false)

    override fun toString(): String {
        return "$title: $description ($isDone)"
    }
}
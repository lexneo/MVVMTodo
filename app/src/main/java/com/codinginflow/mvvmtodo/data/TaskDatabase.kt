package com.codinginflow.mvvmtodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.codinginflow.mvvmtodo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
            private val database: Provider<TaskDatabase>,
            @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        //This method will be executed the first time we start the app not every time
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            //db operations
            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Wash the dishes"))
                dao.insert(Task("Wash the dishes1"))
                dao.insert(Task("Wash the dishes2",important = true))
                dao.insert(Task("Wash the dishes3",completed = true))
                dao.insert(Task("Wash the dishes4"))
                dao.insert(Task("Wash the dishes5",completed = true))
                dao.insert(Task("Wash the dishes6"))
                dao.insert(Task("Wash the dishes7"))
            }


        }
    }
}
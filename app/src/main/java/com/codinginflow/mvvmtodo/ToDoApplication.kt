package com.codinginflow.mvvmtodo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//this is just setup that is necessary to activate Dagger hilt shite
@HiltAndroidApp
class ToDoApplication : Application() {
}
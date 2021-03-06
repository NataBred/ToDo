package com.example.todo.di

import androidx.room.Room
import com.example.todo.data.DataBase
import com.example.todo.data.TaskType
import com.example.todo.task.create.CreateTaskViewModel
import com.example.todo.task.show.TasksViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val daoModule = module {
  single {
    Room.databaseBuilder(androidContext(), DataBase::class.java, "task_database")
      .build()
      .taskDao()
  }
}

private val viewModelModule = module {
  viewModel {( taskType: TaskType) ->
    TasksViewModel(get(), taskType)
  }

  viewModel {
    CreateTaskViewModel(get())
  }
}

val modules = listOf(
  daoModule,
  viewModelModule
)
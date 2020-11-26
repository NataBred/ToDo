package com.example.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class DataBase : RoomDatabase() {
  abstract fun taskDao(): TaskDao
}
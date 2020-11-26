package com.example.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_table")
data class Task(
  @PrimaryKey
  val id: Int? = null,
  val text: String? = null,
  val date: String? = null,
  val isDone: Boolean = false
)
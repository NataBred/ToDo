package com.example.todo.data

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable

@Dao
interface TaskDao {
  @Query("SELECT * FROM tasks_table ORDER BY id DESC")
  fun getTasks(): DataSource.Factory<Int, Task>

  @Query("SELECT * FROM tasks_table where isDone = :isDone ORDER BY id DESC")
  fun getTasks(isDone: Boolean): DataSource.Factory<Int, Task>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(task: Task): Completable

  @Update
  fun update(task: Task): Completable

  @Delete
  fun delete(task: Task): Completable

  @Query("DELETE FROM tasks_table")
  fun deleteAll(): Completable
}
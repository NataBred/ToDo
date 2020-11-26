package com.example.todo.task.show

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.example.todo.data.Task
import com.example.todo.data.TaskDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class TasksViewModel(private val taskDao: TaskDao) : ViewModel() {

  private val disposeBag = CompositeDisposable()

  val tasks = BehaviorSubject.create<PagedList<Task>>()
  val tasksDone = BehaviorSubject.create<PagedList<Task>>()
  val tasksNotDone = BehaviorSubject.create<PagedList<Task>>()

  private val pagedListConfig = PagedList.Config.Builder()
    .setPageSize(20)
    .setEnablePlaceholders(false)
    .build()

  private val pagedList =
    RxPagedListBuilder(taskDao.getTasks(), pagedListConfig).buildObservable()
  private val pagedListDone =
    RxPagedListBuilder(taskDao.getTasks(true), pagedListConfig).buildObservable()
  private val pagedListNotDone =
    RxPagedListBuilder(taskDao.getTasks(false), pagedListConfig).buildObservable()

  val navigateToCreateTaskScreen = PublishSubject.create<Unit>()

  init {
    handleTasksLoaded()
  }

  private fun handleTasksLoaded() {
    pagedList.subscribe { list ->
      tasks.onNext(list)
    }.addTo(disposeBag)

    pagedListDone.subscribe { list ->
      tasksDone.onNext(list)
    }.addTo(disposeBag)

    pagedListNotDone.subscribe { list ->
      tasksNotDone.onNext(list)
    }.addTo(disposeBag)
  }

  fun onDeleteTaskClicked(task: Task) {
    deleteTask(task)
  }

  fun onDeleteAllClicked() {
    deleteAllTask()
  }

  fun onTaskIsDoneClicked(task: Task) {
    updateTask(task.copy(isDone = task.isDone.not()))
  }

  fun onCreateTaskClicked() {
    navigateToCreateTaskScreen.onNext(Unit)
  }

  private fun updateTask(task: Task) {
    taskDao.update(task)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({}, {})
      .addTo(disposeBag)
  }

  private fun deleteTask(task: Task) {
    taskDao.delete(task)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({}, {})
      .addTo(disposeBag)
  }

  private fun deleteAllTask() {
    taskDao.deleteAll()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({}, {})
      .addTo(disposeBag)
  }

  override fun onCleared() {
    super.onCleared()
    disposeBag.clear()
  }
}
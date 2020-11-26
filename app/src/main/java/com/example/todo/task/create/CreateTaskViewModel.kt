package com.example.todo.task.create

import androidx.lifecycle.ViewModel
import com.example.todo.data.Task
import com.example.todo.data.TaskDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.text.SimpleDateFormat
import java.util.*

class CreateTaskViewModel(private val taskDao: TaskDao) : ViewModel() {

  private val disposeBag = CompositeDisposable()
  val navigateToBackScreen = PublishSubject.create<Unit>()

  fun onSaveTaskClicked(task: String) {
    if (task.isBlank()) return
    createTask(
      Task(
        text = task,
        date = getDate(),
        isDone = false
      )
    )
  }

  private fun createTask(task: Task) {
    taskDao.insert(task)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        navigateToBackScreen.onNext(Unit)
      }
      .addTo(disposeBag)
  }

  private fun getDate(): String {
    return  SimpleDateFormat("dd.MM.yyyy HH:mm").format(Calendar.getInstance().time)
  }

  override fun onCleared() {
    super.onCleared()
    disposeBag.clear()
  }
}
package com.example.todo.task.show

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.data.Task
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_task.view.checkbox_task
import kotlinx.android.synthetic.main.item_task.view.layout_delete_task
import kotlinx.android.synthetic.main.item_task.view.text_view_task_date

class TasksAdapter : PagedListAdapter<Task, TasksAdapter.ViewHolder>(DiffUtilCallback()) {
  val deleteTaskClicked = PublishSubject.create<Task>()
  val taskIsDoneClicked = PublishSubject.create<Task>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val itemView = inflater.inflate(R.layout.item_task, parent, false)
    return ViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val task = getItem(position)
    holder.showTask(task)
  }

  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun showTask(task: Task?) {
      if (task == null) {
        return
      }

      showTaskText(task)
      showTaskDate(task)
      showTaskIsDone(task)

      handleDeleteTaskClicked(task)
      handleTaskIsDoneClicked(task)
    }

    private fun showTaskText(task: Task) {
      itemView.checkbox_task.text = task.text
    }

    private fun showTaskDate(task: Task) {
      itemView.text_view_task_date.text = task.date.toString()
    }

    private fun showTaskIsDone(task: Task) {
      itemView.checkbox_task.isChecked = task.isDone
      itemView.checkbox_task.paintFlags =
        if (task.isDone) Paint.STRIKE_THRU_TEXT_FLAG else Paint.ANTI_ALIAS_FLAG
    }

    private fun handleDeleteTaskClicked(task: Task) {
      itemView.layout_delete_task.setOnClickListener {
        deleteTaskClicked.onNext(task)
      }
    }

    private fun handleTaskIsDoneClicked(task: Task) {
      itemView.checkbox_task.setOnClickListener {
        taskIsDoneClicked.onNext(task)
      }
    }
  }

  class DiffUtilCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
      return oldItem == newItem
    }
  }
}
package com.example.todo.task.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.data.Task
import com.example.todo.data.TaskType
import com.example.todo.task.create.CreateTaskFragment
import com.example.todo.task.delete.DeleteAllDialogFragment
import com.example.todo.task.delete.DeleteTaskDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_tasks.button_create_task
import kotlinx.android.synthetic.main.fragment_tasks.recycler_view_task
import kotlinx.android.synthetic.main.fragment_tasks.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TasksFragment : Fragment(), DeleteAllDialogFragment.Listener, DeleteTaskDialogFragment.Listener {
  private val disposeBag = CompositeDisposable()
  private val adapter = TasksAdapter()
  companion object {
    private const val TASK_TYPE_ARGUMENTS_KEY = "TASK_TYPE_ARGUMENTS_KEY"
    fun newInstance(taskType: TaskType): TasksFragment {
      return TasksFragment().apply {
        arguments = Bundle().apply {
          putSerializable(TASK_TYPE_ARGUMENTS_KEY, taskType)
        }
      }
    }
  }
  private val taskType by lazy {
    arguments?.getSerializable(TASK_TYPE_ARGUMENTS_KEY) as? TaskType ?: TaskType.ALL
  }
  private val viewModel: TasksViewModel by viewModel {
    parametersOf(taskType)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_tasks, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    loadTasks()
    initToolbar()
    initNotesRecyclerView()
    handleCreateTaskClicked()
    handleToolbarDeleteAllClicked()
    handleDeleteTackClicked()
    handleTaskIsDoneClicked()
    onNavigateToCreateTaskScreen()
    onShowDeleteAllDialog()
    onShowDeleteTaskDialog()
    onShowError()
  }

  private fun initToolbar() {
    toolbar.inflateMenu(R.menu.menu_toolbar_main)
  }

  private fun initNotesRecyclerView() {
    recycler_view_task.layoutManager = LinearLayoutManager(requireContext())
    recycler_view_task.adapter = adapter
  }

  private fun loadTasks() {
    viewModel.tasks
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { tasks ->
        adapter.submitList(tasks)
      }
      .addTo(disposeBag)
  }

  private fun handleCreateTaskClicked() {
    button_create_task.setOnClickListener {
      viewModel.onCreateTaskClicked()
    }
  }

  private fun onNavigateToCreateTaskScreen() {
    viewModel.navigateToCreateTaskScreen
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        navigateToCreateTaskScreen()
      }.addTo(disposeBag)
  }

  private fun handleToolbarDeleteAllClicked() {
    toolbar.setOnMenuItemClickListener {
      if (it.itemId == R.id.item_delete_all) {
        viewModel.onDeleteAllClicked()
      }
      return@setOnMenuItemClickListener true
    }
  }

  private fun handleDeleteTackClicked() {
    adapter.deleteTaskClicked.subscribe { task ->
      viewModel.onDeleteTaskClicked(task)
    }.addTo(disposeBag)
  }

  private fun handleTaskIsDoneClicked() {
    adapter.taskIsDoneClicked.subscribe { task ->
      viewModel.onTaskIsDoneClicked(task)
    }.addTo(disposeBag)
  }

  private fun navigateToCreateTaskScreen() {
    replaceFragment(CreateTaskFragment())
  }

  private fun replaceFragment(fragment: Fragment) {
    parentFragmentManager.beginTransaction()
      .replace(R.id.layout_fragment_container, fragment)
      .addToBackStack(null)
      .commit()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    disposeBag.clear()
  }

  override fun onDeleteAllClicked() {
    viewModel.onDeleteAllConfirmClicked()
  }

  override fun onDeleteTaskClicked(task: Task) {
    viewModel.onDeleteTaskConfirmClicked(task)
  }

  private fun onShowError() {
    viewModel.showError
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { errorMessage ->
        val message = if (errorMessage.isEmpty()) getString(R.string.error) else errorMessage
        Toast.makeText(context, message,Toast.LENGTH_SHORT).show()
      }.addTo(disposeBag)
  }

  private fun onShowDeleteAllDialog() {
    viewModel.showDeleteAllDialog
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        val dialog = DeleteAllDialogFragment()
        dialog.show(childFragmentManager, "")
      }.addTo(disposeBag)
  }

  private fun onShowDeleteTaskDialog() {
    viewModel.showDeleteTaskDialog
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        val dialog = DeleteTaskDialogFragment(it)
        dialog.show(childFragmentManager, "")
      }.addTo(disposeBag)
  }
}
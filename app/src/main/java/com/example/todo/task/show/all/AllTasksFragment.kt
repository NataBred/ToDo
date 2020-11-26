package com.example.todo.task.show.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.task.create.CreateTaskFragment
import com.example.todo.task.show.AllTasksAdapter
import com.example.todo.task.show.TasksViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_tasks.button_create_task
import kotlinx.android.synthetic.main.fragment_tasks.recycler_view_task
import kotlinx.android.synthetic.main.fragment_tasks.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllTasksFragment : Fragment() {
  private val disposeBag = CompositeDisposable()
  private val viewModel: TasksViewModel by viewModel()
  private val adapter = AllTasksAdapter()

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
}
package com.example.todo.task.create

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.todo.MainActivity
import com.example.todo.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_create_task.edit_text_task
import kotlinx.android.synthetic.main.fragment_create_task.toolbar_create_task
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateTaskFragment : Fragment() {
  private val disposeBag = CompositeDisposable()
  private val viewModel: CreateTaskViewModel by viewModel()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_create_task, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    (requireActivity() as MainActivity).hideBottomNavigation()

    initToolbar()
    showKeyboard()
    handleToolbarSaveTaskClicked()
    onNavigateToBackScreen()
  }

  private fun initToolbar() {
    toolbar_create_task.inflateMenu(R.menu.menu_toolbar_save_task)
    toolbar_create_task.setNavigationOnClickListener {
      navigateToBackScreen()
    }
  }

  private fun handleToolbarSaveTaskClicked() {
    toolbar_create_task.setOnMenuItemClickListener {
      if (it.itemId == R.id.item_save_task) {
        val task = edit_text_task.text.toString()
        viewModel.onSaveTaskClicked(task)
      }
      return@setOnMenuItemClickListener true
    }
  }

  private fun onNavigateToBackScreen() {
    viewModel.navigateToBackScreen
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        navigateToBackScreen()
      }
      .addTo(disposeBag)
  }

  private fun navigateToBackScreen() {
    requireActivity().onBackPressed()
  }

  private fun showKeyboard() {
    val inputMethodManager =
      context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager? ?: return
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)

    edit_text_task.apply {
      isFocusable = true
      isFocusableInTouchMode = true
      requestFocus()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    (requireActivity() as MainActivity).showBottomNavigation()
    disposeBag.clear()
  }
}
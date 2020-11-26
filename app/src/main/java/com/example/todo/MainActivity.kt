package com.example.todo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.todo.task.show.all.AllTasksFragment
import com.example.todo.task.show.done.DoneTasksFragment
import com.example.todo.task.show.notdone.NotDoneTasksFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.bottom_navigation_view

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    showCreateNoteFragment()
    setBottomNavigationListener()
  }

  private fun showCreateNoteFragment() {
    replaceFragment(AllTasksFragment())
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    if (item.itemId == bottom_navigation_view.selectedItemId) {
      return false
    }
    val fragment = when (item.itemId) {
      R.id.item_all_tasks -> AllTasksFragment()
      R.id.item_done_tasks -> DoneTasksFragment()
      R.id.item_not_done_tasks -> NotDoneTasksFragment()
      else -> AllTasksFragment()
    }
    replaceFragment(fragment)
    return true
  }

  private fun setBottomNavigationListener() {
    bottom_navigation_view.setOnNavigationItemSelectedListener(this)
  }

  fun showBottomNavigation() {
    bottom_navigation_view.isVisible = true
  }

  fun hideBottomNavigation() {
    bottom_navigation_view.isVisible = false
  }

  private fun replaceFragment(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
      .replace(R.id.layout_fragment_container, fragment)
      .commit()
  }
}
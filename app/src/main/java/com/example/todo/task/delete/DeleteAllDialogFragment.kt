package com.example.todo.task.delete

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.todo.R

class DeleteAllDialogFragment : DialogFragment() {

    interface Listener {
        fun onDeleteAllClicked()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = parentFragment as? Listener

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle(R.string.fragment_delete_all_dialog)
            .setNegativeButton(R.string.fragment_dialog_negative_button, null)
            .setPositiveButton(R.string.fragment_dialog_positive_button) { _, _ ->
                listener?.onDeleteAllClicked()
            }
            .create()
    }
}
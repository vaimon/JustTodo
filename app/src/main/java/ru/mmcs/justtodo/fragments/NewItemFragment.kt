package ru.mmcs.justtodo.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.mmcs.justtodo.R
import ru.mmcs.justtodo.databinding.FragmentListBinding
import ru.mmcs.justtodo.databinding.FragmentNewItemBinding
import ru.mmcs.justtodo.models.Task
import ru.mmcs.justtodo.viewmodels.ListFragmentViewModel
import ru.mmcs.justtodo.viewmodels.NewItemFragmentViewModel

class NewItemFragment(private val dialogInteraction: DialogInteraction) : DialogFragment() {
    private var _binding: FragmentNewItemBinding? = null

    private lateinit var viewModel: NewItemFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewItemBinding.inflate(inflater, container, false)
        viewModel = NewItemFragmentViewModel(_binding, dialogInteraction)
        _binding?.btnOk?.setOnClickListener {
            viewModel.onBtnOkClick()
            dialog?.cancel()
        }
        _binding?.etDescription?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.onTextChanged(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        return _binding!!.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.onDismiss()
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.binding = null
    }

    interface DialogInteraction{
        fun onTaskCreated(task: Task)
        fun onDismiss()
    }

    companion object{
        val TAG = "NewItemFragment"
    }
}
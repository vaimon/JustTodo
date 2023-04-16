package ru.mmcs.justtodo.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import ru.mmcs.justtodo.databinding.FragmentNewItemBinding
import ru.mmcs.justtodo.models.Task
import ru.mmcs.justtodo.viewmodels.NewItemFragmentViewModel

class NewItemFragment(private val dialogInteraction: DialogInteraction) : DialogFragment() {
    private var _binding: FragmentNewItemBinding? = null

    private lateinit var viewModel: NewItemFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewItemBinding.inflate(inflater, container, false)
        _binding?.lifecycleOwner = this
        viewModel = NewItemFragmentViewModel(_binding, dialogInteraction)
        _binding?.btnOk?.setOnClickListener {
            viewModel.onBtnOkClick()
            dialog?.cancel()
        }
        _binding?.etDescription?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.onTitleChanged(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        _binding?.etFullDescription?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.onDescriptionChanged(p0.toString())
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

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
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
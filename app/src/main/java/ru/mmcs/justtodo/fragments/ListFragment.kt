package ru.mmcs.justtodo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.mmcs.justtodo.R
import ru.mmcs.justtodo.databinding.FragmentListBinding
import ru.mmcs.justtodo.models.Task
import ru.mmcs.justtodo.viewmodels.ListFragmentViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null

    private lateinit var viewModel: ListFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        viewModel = ListFragmentViewModel(_binding)
        _binding?.viewModel = viewModel
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    if(uiState.isDialogShowing){
                        viewModel.onDialogOpened()
                        NewItemFragment(object : NewItemFragment.DialogInteraction {
                            override fun onTaskCreated(task: Task) {
                                viewModel.onDialogShown(task)
                            }

                            override fun onDismiss() {
                                viewModel.onDialogShown()
                            }
                        }).show(childFragmentManager, NewItemFragment.TAG)
                    }
                    if(uiState.navigationTarget != null){
                        val args = Bundle()
                        args.putString("task_id", uiState.navigationTarget.toHexString())
                        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, args)
                        viewModel.onDetailsFragmentOpened()
                    }
                }
            }
        }
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.binding = null
    }
}
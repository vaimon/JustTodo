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
import kotlinx.coroutines.launch
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
        Log.d("DEBUG_RV", "Hi Fragment")
        _binding = FragmentListBinding.inflate(inflater, container, false)
        viewModel = ListFragmentViewModel(_binding)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    if(uiState.isDialogShowing){
                        NewItemFragment(object : NewItemFragment.DialogInteraction {
                            override fun onTaskCreated(task: Task) {
                                viewModel.onDialogShown(task)
                            }

                            override fun onDismiss() {
                                viewModel.onDialogShown()
                            }
                        }).show(childFragmentManager, NewItemFragment.TAG)
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
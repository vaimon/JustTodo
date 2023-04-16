package ru.mmcs.justtodo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.mmcs.justtodo.R
import ru.mmcs.justtodo.databinding.FragmentDetailsBinding
import ru.mmcs.justtodo.models.Task
import ru.mmcs.justtodo.viewmodels.DetailsFragmentViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    public lateinit var viewModel: DetailsFragmentViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        viewModel = DetailsFragmentViewModel(_binding, Task("Example","", true))
        _binding?.viewModel = viewModel
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.binding = null
    }
}
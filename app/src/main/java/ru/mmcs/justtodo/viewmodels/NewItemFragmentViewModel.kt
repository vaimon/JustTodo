package ru.mmcs.justtodo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mmcs.justtodo.databinding.FragmentNewItemBinding
import ru.mmcs.justtodo.fragments.NewItemFragment
import ru.mmcs.justtodo.models.Task

class NewItemFragmentViewModel(var binding: FragmentNewItemBinding?,
                               val dialogInteraction: NewItemFragment.DialogInteraction
) : ViewModel() {
    val description = MutableLiveData<String>()

    fun onBtnOkClick(){
        val desc = description.value
        if(desc != null){
            dialogInteraction.onTaskCreated(Task(desc, false))
        }else{
            onDismiss()
        }
    }

    fun onTextChanged(text: String){
        description.value = text
    }

    fun onDismiss(){
        dialogInteraction.onDismiss()
    }
}
package ru.mmcs.justtodo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mmcs.justtodo.databinding.FragmentNewItemBinding
import ru.mmcs.justtodo.fragments.NewItemFragment
import ru.mmcs.justtodo.models.Task

class NewItemFragmentViewModel(var binding: FragmentNewItemBinding?,
                               val dialogInteraction: NewItemFragment.DialogInteraction
) : ViewModel() {
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    fun onBtnOkClick(){
        val titleInput = title.value
        if(titleInput != null){
            dialogInteraction.onTaskCreated(Task(titleInput, description.value ?: "Нет описания",false))
        }else{
            onDismiss()
        }
    }

    fun onTitleChanged(text: String){
        title.value = text
    }

    fun onDescriptionChanged(text:String){
        description.value = text
    }

    fun onDismiss(){
        dialogInteraction.onDismiss()
    }
}
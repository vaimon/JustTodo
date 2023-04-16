package ru.mmcs.justtodo.adapters

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import ru.mmcs.justtodo.databinding.ItemTaskBinding
import ru.mmcs.justtodo.models.Task


class TaskListRvAdapter(
    var items: LiveData<List<Task>>,
    private val onItemInteractionListener: OnItemInteractionListener? = null
) : RecyclerView.Adapter<TaskListRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.value!!.get(position)
        holder.binding.task = item
        holder.binding.tvTask.apply {
            paintFlags = if (item.isDone) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.binding.cbDone.setOnClickListener {
            onItemInteractionListener?.onItemSelected(item, position)
        }

        holder.binding.btnDelete.setOnClickListener {
            onItemInteractionListener?.onBtnRemoveClicked(item, position)
        }

        holder.binding.root.setOnClickListener {
            onItemInteractionListener?.onItemClicked(item)
        }
    }

    override fun getItemCount(): Int = items.value!!.size

    inner class ViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    interface OnItemInteractionListener {
        fun onBtnRemoveClicked(item: Task, position: Int)
        fun onItemSelected(item: Task, position: Int)
        fun onItemClicked(item: Task)
    }

}

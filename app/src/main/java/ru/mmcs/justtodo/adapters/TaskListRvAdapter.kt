package ru.mmcs.justtodo.adapters

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import ru.mmcs.justtodo.databinding.ItemTaskBinding
import ru.mmcs.justtodo.models.Task


class TaskListRvAdapter(
    var items: MutableList<Task>,
    private val onItemInteractionListener: OnItemInteractionListener? = null
) : RecyclerView.Adapter<TaskListRvAdapter.ViewHolder>() {

    private val mOnClickListener = View.OnClickListener {
        val item = it.tag as Pair<Task, Int>
        onItemInteractionListener?.onBtnRemoveClicked(item.first, item.second)
    }

    private val onCheckedListener = CompoundButton.OnCheckedChangeListener { btn, isChecked ->
        val item = btn.tag as Pair<Task, Int>
        onItemInteractionListener?.onItemSelected(item.first, item.second)
    }

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
        val item = items.get(position)
        Log.d("DEBUG_RV", "Bind: ${item}")
        holder.binding.task = item
        holder.binding.tvTask.apply {
            paintFlags = if (item.isDone) paintFlags or Paint.STRIKE_THRU_TEXT_FLAG else paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.binding.cbDone.setOnCheckedChangeListener { compoundButton, b ->

            onItemInteractionListener?.onItemSelected(item, position)
            Log.d("DEBUG_RV", "Click: ${position}")
        }

        onItemInteractionListener?.also {
            with(holder.binding.btnDelete) {
                tag = Pair(item, position)
                setOnClickListener(mOnClickListener)
            }
            with(holder.binding.cbDone){
                tag = Pair(item, position)
                setOnCheckedChangeListener(onCheckedListener)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    interface OnItemInteractionListener {
        fun onBtnRemoveClicked(item: Task, position: Int)
        fun onItemSelected(item: Task, position: Int)
    }

}

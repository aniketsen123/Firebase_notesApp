package com.tec.firebase_notesapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tec.firebase_notesapp.databinding.EachTodoItemBinding

class TOdoAdapter(private val list:MutableList<TODOtask>):RecyclerView.Adapter<TOdoAdapter.ToDOViewHolder>() {
    private var listner:TodoAdapterClickListner?=null

    fun setListner(listner: TodoAdapterClickListner)
    {
        this.listner=listner
    }

    inner  class ToDOViewHolder(val binding: EachTodoItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDOViewHolder {
      val binding=EachTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDOViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDOViewHolder, position: Int) {
       with(holder){
           with(list[position]){
               binding.todoTask.text=this.task
               binding.deleteTask.setOnClickListener {
                                     listner?.TodoDelete(this)
               }
               binding.editTask.setOnClickListener {
                listner?.TodoUpdate(this)
               }
           }
       }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface TodoAdapterClickListner{
        fun TodoDelete(todo:TODOtask)
        fun  TodoUpdate(todo:TODOtask)
    }
}
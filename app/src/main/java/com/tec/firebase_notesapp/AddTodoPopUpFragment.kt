package com.tec.firebase_notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.tec.firebase_notesapp.databinding.FragmentAddTodoPopUpBinding
import com.tec.firebase_notesapp.databinding.FragmentHomeBinding


class AddTodoPopUpFragment : DialogFragment() {

    private lateinit var binding: FragmentAddTodoPopUpBinding
    private lateinit var listner:DialogNextBtnClickLitner
     private  var TodoData:TODOtask?=null
    fun setListener(listner:DialogNextBtnClickLitner)
    {
        this.listner=listner
    }
    companion object{
        const val TAG="AddTodoPopupFragment"
        @JvmStatic
        fun newinstance(taskId:String,task:String)=AddTodoPopUpFragment().apply {
            arguments=Bundle().apply {
                putString("taskId",taskId)
                putString("task",task)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddTodoPopUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments!=null)
        {
            TodoData=TODOtask(arguments?.getString("taskId").toString(),
                              arguments?.getString("task").toString())
        }
        binding.todoEt.setText(TodoData?.task)
        register()
    }

    private fun register() {
        binding.todoNextBtn.setOnClickListener {
            val todoTask=binding.todoEt.text.toString()
            if(todoTask.isNotEmpty() )
            {
                if(TodoData==null)
                listner.onSaveTask(todoTask,binding.todoEt)
                else
                {TodoData?.task=todoTask
                listner.onUpdateTask(TodoData!!,binding.todoEt)
                }
            }
            else
            {
                Toast.makeText(context,"Enter The required things",Toast.LENGTH_SHORT).show()
            }
        }
        binding.todoClose.setOnClickListener {
            dismiss()
        }
    }
    interface DialogNextBtnClickLitner{
        fun onSaveTask(todo:String,todoEt:TextInputEditText)
        fun onUpdateTask(TodoData:TODOtask,todoEt:TextInputEditText)
    }

}
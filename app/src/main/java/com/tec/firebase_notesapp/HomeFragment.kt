package com.tec.firebase_notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tec.firebase_notesapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), AddTodoPopUpFragment.DialogNextBtnClickLitner,
    TOdoAdapter.TodoAdapterClickListner {

   private lateinit var auth: FirebaseAuth
   private lateinit var databaseRef:DatabaseReference
   private lateinit var navController: NavController
   private lateinit var binding: FragmentHomeBinding
   private  var popUpFragment:AddTodoPopUpFragment?=null
   private lateinit var adapter: TOdoAdapter
   private lateinit var list:MutableList<TODOtask>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        getDataFirebase()
        registerEvents()

    }

    private fun getDataFirebase() {
        databaseRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               list.clear()
                for(takesnapshots in snapshot.children)
                {
                    val todoTask=takesnapshots.key?.let {
                        TODOtask(it,takesnapshots.value.toString())

                    }
                    if(todoTask!=null)
                    { binding.progressBar2.visibility=View.GONE
                        list.add(todoTask)
                        }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
              Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun registerEvents() {
        binding.addBtn.setOnClickListener {
            if(popUpFragment!=null)
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
             popUpFragment= AddTodoPopUpFragment()
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(childFragmentManager,AddTodoPopUpFragment.TAG)
        }
    }

    private fun init(view: View) {
auth= FirebaseAuth.getInstance()
        navController=Navigation.findNavController(view)
        databaseRef=FirebaseDatabase.getInstance().reference.child("task").child(auth.currentUser?.uid.toString())

        binding.recycleview.setHasFixedSize(true)
        binding.recycleview.layoutManager=LinearLayoutManager(context)
        list= mutableListOf()
         adapter= TOdoAdapter(list)
adapter.setListner(this)

        binding.recycleview.adapter=adapter

    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        binding.progressBar2.visibility=View.VISIBLE
databaseRef.push().setValue(todo).addOnCompleteListener {
    if(it.isSuccessful)
    {
        binding.progressBar2.visibility=View.GONE
           Toast.makeText(context,"Todo Saved Successfully",Toast.LENGTH_SHORT).show()
        todoEt.text=null
    }
    else
    {
        binding.progressBar2.visibility=View.GONE
        Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
    }
    popUpFragment!!.dismiss()
}
    }

    override fun onUpdateTask(TodoData: TODOtask, todoEt: TextInputEditText) {
     val map=HashMap<String,Any>()
        map[TodoData.taskId]=TodoData.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if(it.isSuccessful)
                Toast.makeText(context,"Updated Suceesfully",Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            todoEt.text=null
            popUpFragment!!.dismiss()
        }
    }

    override fun TodoDelete(todo: TODOtask) {
        databaseRef.child(todo.taskId).removeValue().addOnCompleteListener {
            if(it.isSuccessful)
            {
                Toast.makeText(context,"Deleted SuccessFully",Toast.LENGTH_SHORT).show()
            }
            else Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
        }
    }

    override fun TodoUpdate(todo: TODOtask) {
        if(popUpFragment!=null)
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()

        popUpFragment=AddTodoPopUpFragment.newinstance(todo.taskId,todo.task)
        popUpFragment!!.setListener(this)
        popUpFragment!!.show(childFragmentManager,AddTodoPopUpFragment.TAG)
    }

}
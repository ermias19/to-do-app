package com.example.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.room.Room
import com.example.schedule.databinding.FragmentRegisterBinding
import com.example.schedule.models.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//import com.example.shedule.databinding.FragmentRegisterBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [Register.newInstance] factory method to
 * create an instance of this fragment.
 */
class Register : Fragment() {
    var man=true;


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentRegisterBinding =DataBindingUtil.inflate(
            inflater,R.layout.fragment_register,container,false
        )
        binding.register.setOnClickListener{
            Toast.makeText(activity,"register", Toast.LENGTH_LONG).show()
            GlobalScope.launch {
                val db = AppDatabase.getDatabase(requireContext())
                // TODO: Input validation of some sort
                val user = User(uid = 1, username = binding.inputEmail.text.toString(), pin = binding.inputPassword.text.toString().toInt(), askOnStart = binding.toggleButton1.isChecked, hasLoggedIn = true)
                db.userDao().insertUser(user)
                it.findNavController().navigate(RegisterDirections.actionRegister2ToMainPage2())
            }
        }
        return binding.root
    }


}

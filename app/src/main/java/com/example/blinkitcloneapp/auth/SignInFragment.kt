package com.example.blinkitcloneapp.auth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.blinkitcloneapp.R
import com.example.blinkitcloneapp.Utils
import com.example.blinkitcloneapp.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {
private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun onContinueButtonClick(){
        binding.btncontinue.setOnClickListener{
            val number = binding.etUserNumber.text.toString()

            if(number.isEmpty() || number.length != 10){
                Utils.showToast(requireContext(),"Please enter valid phone number")
            }
            else{
                val bundle= Bundle()
                bundle.putString("number",number)
                findNavController().navigate(R.id.action_signInFragment_to_OTPFragment,bundle)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor()
        getUserNumber()
        onContinueButtonClick()
    }


    private fun getUserNumber(){
        binding.etUserNumber.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(number: CharSequence?, start: Int, before: Int, count: Int) {
               val len=number?.length
                if(len == 10){
                    binding.btncontinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.green))
                }
                else{
                    binding.btncontinue.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey))
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

            })
    }

    private fun setStatusBarColor(){
        activity?.window?.apply{
            val statusBarColors = ContextCompat.getColor(requireContext(), R.color.yellow)
            statusBarColor=statusBarColors
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}
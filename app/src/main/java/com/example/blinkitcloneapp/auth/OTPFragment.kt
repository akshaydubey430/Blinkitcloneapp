package com.example.blinkitcloneapp.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.blinkitcloneapp.R
import com.example.blinkitcloneapp.Utils
import com.example.blinkitcloneapp.activity.UsersMainActivity
import com.example.blinkitcloneapp.databinding.FragmentOTPBinding
import com.example.blinkitcloneapp.models.Users
import com.example.blinkitcloneapp.viewmodels.AuthViewModels
import kotlinx.coroutines.launch

class OTPFragment : Fragment() {
    private val viewModels:AuthViewModels by viewModels()
    private var _binding: FragmentOTPBinding? = null
    private val binding get() = _binding!!
    private lateinit var userNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOTPBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserNumber()
        customizingEnteringOTP()
        onBackButtonClicked()
        sendOTP()
        onLoginButtonClicked()
    }
    private fun onLoginButtonClicked(){
        binding.btnLogin.setOnClickListener{
Utils.showDialog(requireContext(),"Signing you...")
            val editText = arrayOf(
                binding.etOtp1, binding.etOtp2, binding.etOtp3,
                binding.etOtp4, binding.etOtp5, binding.etOtp6
            )
            val otp= editText.joinToString( "") { it.text.toString() }
            if(otp.length<editText.size){
                Utils.showToast(requireContext(),"Please enter right otp")
            }
            else{
                editText.forEach { it.text?.clear();it.clearFocus() }
                verifyOtp(otp)
            }
        }
    }
    private fun verifyOtp(otp:String){
        val user=Users(uid = Utils.getCurrentUserId(),userPhoneNumber = userNumber,userAddress = null)
        viewModels.signInWithPhoneAuthCredential(otp,userNumber,user)
        lifecycleScope.launch{
            viewModels.isSignedSuccessfully.collect{
                if(it){
                    Utils.hideDialog()
                    Utils.showToast(requireContext(),"Logged in...")
                    startActivity(Intent( requireActivity(),UsersMainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

    }

    private fun sendOTP(){
Utils.showDialog(requireContext(),"Sending OTP")

        viewModels.apply {
            sendOTP(userNumber,requireActivity())
            lifecycleScope.launch {
                otpSent.collect{
                    if(it==true){
                        Utils.hideDialog()
                        Utils.showToast(requireContext(),"otp sent..")
                    }

            } }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onBackButtonClicked() {
        binding.tbOtpFragment.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_signInFragment)
        }
    }

    private fun customizingEnteringOTP() {
        val editText = arrayOf(
            binding.etOtp1, binding.etOtp2, binding.etOtp3,
            binding.etOtp4, binding.etOtp5, binding.etOtp6
        )
        for (i in editText.indices) {
            editText[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Do nothing
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        if (i < editText.size - 1) {
                            editText[i + 1].requestFocus()
                        }
                    } else if (s?.length == 0) {
                        if (i > 0) {
                            editText[i - 1].requestFocus()
                        }
                    }
                }
            })
        }
    }

    private fun getUserNumber() {
        val bundle = arguments
        userNumber = bundle?.getString("number").orEmpty()
        binding.tvUserNumber.text = userNumber.ifEmpty { "Unknown Number" }
    }
}
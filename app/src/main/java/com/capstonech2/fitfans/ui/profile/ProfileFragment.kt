package com.capstonech2.fitfans.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import com.capstonech2.fitfans.databinding.FragmentProfileBinding
import com.capstonech2.fitfans.ui.welcomepage.WelcomePageActivity
import com.capstonech2.fitfans.utils.State
import com.capstonech2.fitfans.utils.capitalizeFirstLetter
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.pow

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModel()
    private var lastKnownEmail: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        settingsMenu()
        val currentUserEmail = auth.currentUser?.email.toString()
        if (lastKnownEmail != currentUserEmail) {
            lastKnownEmail = currentUserEmail
            showUserData(currentUserEmail)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun settingsMenu(){
        binding.profileSetting.setOnClickListener { view ->
            val popup = PopupMenu(requireContext(), view)
            popup.menuInflater.inflate(R.menu.settings_menu, popup.menu)

            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                when(menuItem.itemId){
                    R.id.menu_edit_profile ->{
                        startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
                        true
                    }
                    R.id.menu_logout -> {
                        auth.signOut()
                        startActivity(Intent(requireActivity(), WelcomePageActivity::class.java))
                        requireActivity().finish()
                        true
                    }
                    else -> false
                }
            }
            popup.setOnDismissListener { menu ->
                menu.dismiss()
            }
            popup.show()
        }
    }

    private fun showUserData(email: String){
        viewModel.getUserDataByEmail(email).observe(requireActivity()){
            if(it != null){
                when(it){
                    is State.Loading -> {
                        // TODO
                    }
                    is State.Success -> {
                        setData(it.data.usersResponse[0])
                    }
                    is State.Error -> {
                        // TODO
                    }
                }
            }
        }
    }

    private fun calculateBMI(weight: Double, height: Double): Double {
        return weight / (height / 100).pow(2)
    }

    private fun setData(data: UsersResponseItem){
        binding.profileName.text = data.fullName.capitalizeFirstLetter()
        binding.profileEmail.text = data.email
        binding.userAge.text = data.age.toString()
        binding.userGender.text = data.gender
        binding.userWeight.text = data.weight.toString()
        binding.userHeight.text = data.height.toString()
        binding.userBmi.text = calculateBMI(data.weight, data.height).toString()
    }
}
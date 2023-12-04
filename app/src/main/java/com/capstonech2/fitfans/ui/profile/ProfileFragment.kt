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
import kotlin.math.round

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        settingsMenu()
        showUserData(auth.currentUser?.email.toString())
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
            popup.setOnDismissListener { menu -> menu.dismiss() }
            popup.show()
        }
    }

    private fun showUserData(email: String){
        viewModel.checkUserData(email)
        viewModel.userData.observe(viewLifecycleOwner){
            if(it != null){
                when(it){
                    is State.Loading -> {}
                    is State.Success -> { setData(it.data) }
                    is State.Error -> {}
                }
            }
        }
    }

    private fun calculateBMI(weight: Double, height: Double): Double {
        return round((weight / (height/100).pow(2)) * 10) / 10.0
    }

    private fun setData(data: List<UsersResponseItem>){
        binding.profileName.text = data[0].fullName.capitalizeFirstLetter()
        binding.profileEmail.text = data[0].email
        binding.userAge.text = data[0].age.toString()
        binding.userGender.text = data[0].gender

        val bmi = calculateBMI(data[0].weight, data[0].height)
        val weight = data[0].weight.toString()
        val height = data[0].height.toString()

        binding.userWeight.text = String.format(getString(R.string.user_weight), weight)
        binding.userHeight.text = String.format(getString(R.string.user_height), height)
        binding.userBmi.text = String.format(getString(R.string.user_bmi), bmi)
    }
}
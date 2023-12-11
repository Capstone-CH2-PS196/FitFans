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
import com.capstonech2.fitfans.data.model.User
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import com.capstonech2.fitfans.databinding.FragmentProfileBinding
import com.capstonech2.fitfans.ui.welcomepage.WelcomePageActivity
import com.capstonech2.fitfans.utils.EXTRA_PROFILE_KEY
import com.capstonech2.fitfans.utils.State
import com.capstonech2.fitfans.utils.calculateBMI
import com.capstonech2.fitfans.utils.capitalizeFirstLetter
import com.capstonech2.fitfans.utils.loadImage
import com.capstonech2.fitfans.utils.show
import com.capstonech2.fitfans.utils.showDialog
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModel()
    private var currentImage: String? = null

    private var weight: Double = 0.0
    private var height: Double = 0.0

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
                        navigateToEdit()
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
                    is State.Loading -> binding.progressBarProfile.show(true)
                    is State.Success -> handleSuccessState(it.data)
                    is State.Error -> handleErrorState()
                }
            }
        }
    }

    private fun navigateToEdit(){
        binding.apply {
            val image = currentImage
            val name = profileName.text.toString()
            val email = profileEmail.text.toString()
            val gender = userGender.text.toString()
            val age = userAge.text.toString()

            val data = User(
                full_name = name,
                email = email,
                gender = gender,
                age = age.toInt(),
                weight = weight,
                height = height,
                image = image
            )
            val intent = Intent(requireActivity(), EditProfileActivity::class.java)
            intent.putExtra(EXTRA_PROFILE_KEY, data)
            startActivity(intent)
        }
    }

    private fun handleSuccessState(data: List<UsersResponseItem>){
        binding.apply {
            progressBarProfile.show(false)
            currentImage = data[0].image
            profileName.text = data[0].fullName.capitalizeFirstLetter()
            profileEmail.text = data[0].email
            userAge.text = data[0].age.toString()
            userGender.text = data[0].gender

            weight = data[0].weight
            height = data[0].height
            val bmi = calculateBMI(data[0].weight, data[0].height)

            userWeight.text = String.format(getString(R.string.user_weight), weight.toString())
            userHeight.text = String.format(getString(R.string.user_height), height.toString())
            userBmi.text = String.format(getString(R.string.user_bmi), bmi)

            if (currentImage == null || currentImage == "") profileImage.setImageResource(R.drawable.ic_profile_user)
            else profileImage.loadImage(currentImage!!)
        }
    }

    private fun handleErrorState(){
        binding.apply {
            progressBarProfile.show(false)
            profileImage.setImageResource(R.drawable.ic_profile_user)
            profileName.text = getString(R.string.empty)
            profileEmail.text = getString(R.string.empty)
            userAge.text = getString(R.string.empty)
            userGender.text = getString(R.string.empty)
            userWeight.text = getString(R.string.empty)
            userHeight.text = getString(R.string.empty)
            userBmi.text = getString(R.string.empty)
        }
        showDialog(requireActivity(), getString(R.string.connection_error))
    }
}
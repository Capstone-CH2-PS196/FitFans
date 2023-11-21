package com.capstonech2.fitfans.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        settingsMenu()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun settingsMenu(){
        binding.profileSetting.setOnClickListener {
            val popUpMenu = PopupMenu(requireContext(), view)
            popUpMenu.menuInflater.inflate(R.menu.settings_menu, popUpMenu.menu)

            popUpMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menu_edit_profile ->{
                        startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
                        true
                    }
                    R.id.menu_logout -> {
                        auth.signOut()
                        true
                    }
                    else -> false
                }
            }
        }
    }
}
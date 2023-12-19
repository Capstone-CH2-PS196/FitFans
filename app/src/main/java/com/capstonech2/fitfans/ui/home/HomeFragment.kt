package com.capstonech2.fitfans.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import com.capstonech2.fitfans.databinding.FragmentHomeBinding
import com.capstonech2.fitfans.ui.auth.basicinformation.BasicInformationActivity
import com.capstonech2.fitfans.ui.home.menulist.MenuAdapter
import com.capstonech2.fitfans.ui.home.menulist.MenuDataSource
import com.capstonech2.fitfans.utils.State
import com.capstonech2.fitfans.utils.calculateBMI
import com.capstonech2.fitfans.utils.capitalizeFirstLetter
import com.capstonech2.fitfans.utils.loadImage
import com.capstonech2.fitfans.utils.show
import com.capstonech2.fitfans.utils.showDialog
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        showMenuList()
        getUserData()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showMenuList(){
        binding.menuList.layoutManager = LinearLayoutManager(requireContext())
        binding.menuList.adapter = MenuAdapter(MenuDataSource.menu)
    }

    private fun getUserData(){
        viewModel.checkUserData(auth.currentUser?.email.toString())
        viewModel.userData.observe(viewLifecycleOwner){
            if(it != null){
                when(it){
                    is State.Loading -> binding.progressBarHome.show(true)
                    is State.Success -> {
                        if (it.data.isEmpty()){
                            startActivity(Intent(requireActivity(), BasicInformationActivity::class.java))
                            requireActivity().finish()
                        } else {
                            handleSuccessState(it.data)
                        }
                    }
                    is State.Error -> handleErrorState()
                }
            }
        }
    }

    private fun handleErrorState() {
        binding.apply {
            progressBarHome.show(false)
            textUsername.text = getString(R.string.empty)
            imageProfileUser.setImageResource(R.drawable.ic_profile_user)
            caloriesValue.text = getString(R.string.empty)
            bmiValue.text = getString(R.string.empty)
        }
        showDialog(requireActivity(), getString(R.string.connection_error))
    }

    private fun handleSuccessState(data: List<UsersResponseItem>) {
        binding.apply {
            progressBarHome.show(false)
            textUsername.text = data[0].fullName.capitalizeFirstLetter()
            caloriesValue.text = data[0].totalCalories.toString()
            bmiValue.text = calculateBMI(data[0].weight, data[0].height).toString()
            if (data[0].image == "" || data[0].image == null) imageProfileUser.setImageResource(R.drawable.ic_profile_user)
            else imageProfileUser.loadImage(data[0].image)
        }
    }
}
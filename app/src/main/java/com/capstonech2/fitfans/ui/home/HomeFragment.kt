package com.capstonech2.fitfans.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import com.capstonech2.fitfans.databinding.FragmentHomeBinding
import com.capstonech2.fitfans.ui.home.menulist.MenuAdapter
import com.capstonech2.fitfans.ui.home.menulist.MenuDataSource
import com.capstonech2.fitfans.ui.home.progressreport.ProgressReportAdapter
import com.capstonech2.fitfans.ui.home.progressreport.Report
import com.capstonech2.fitfans.ui.home.progressreport.ReportDataSource
import com.capstonech2.fitfans.utils.State
import com.capstonech2.fitfans.utils.capitalizeFirstLetter
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
        showProgressReport(ReportDataSource.report)
        auth = FirebaseAuth.getInstance()
        showMenuList()
        getUserData()
        return binding.root
    }

    private fun showProgressReport(report : List<Report>){
        binding.reportList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = ProgressReportAdapter()
        adapter.submitList(report)
        binding.reportList.adapter = adapter
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
                    is State.Loading -> {}
                    is State.Success -> { setData(it.data) }
                    is State.Error -> {}
                }
            }
        }
    }

    private fun setData(data: List<UsersResponseItem>){
        binding.textUsername.text = data[0].fullName.capitalizeFirstLetter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
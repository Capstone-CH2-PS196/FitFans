package com.capstonech2.fitfans.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.databinding.FragmentHomeBinding
import com.capstonech2.fitfans.ui.home.menulist.MenuAdapter
import com.capstonech2.fitfans.ui.home.menulist.MenuDataSource
import com.capstonech2.fitfans.ui.home.progressreport.ProgressReportAdapter
import com.capstonech2.fitfans.ui.home.progressreport.Report
import com.capstonech2.fitfans.ui.home.progressreport.ReportDataSource

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        showProgressReport(ReportDataSource.report)
        showMenuList()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
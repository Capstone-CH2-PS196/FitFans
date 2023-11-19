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

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        showMenuList()
        return binding.root
    }

    private fun showMenuList(){
        val layoutManager = LinearLayoutManager(requireContext())
        binding.menuList.layoutManager = layoutManager
        val adapter = MenuAdapter(MenuDataSource.menu)
        binding.menuList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
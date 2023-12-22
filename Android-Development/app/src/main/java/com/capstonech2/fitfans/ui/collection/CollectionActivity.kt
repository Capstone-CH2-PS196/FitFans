package com.capstonech2.fitfans.ui.collection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityCollectionBinding
import com.capstonech2.fitfans.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class CollectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCollectionBinding
    private val viewModel: CollectionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.collection)
            setDisplayHomeAsUpEnabled(true)
        }

        showAllCollection()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showAllCollection() {
        val layoutManager = LinearLayoutManager(this)
        binding.listCollection.layoutManager = layoutManager

        viewModel.getAllCollection().observe(this){ listCollect ->
            if(listCollect.isNotEmpty()){
                binding.emptyCollectionText.show(false)
                binding.listCollection.show(true)
                val adapter = CollectionAdapter()
                adapter.submitList(listCollect)
                binding.listCollection.adapter = adapter
            } else {
                binding.emptyCollectionText.show(true)
                binding.listCollection.show(false)
            }
        }
    }
}
package com.dicoding.myunlimitedquotes.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myunlimitedquotes.adapter.QuoteListAdapter
import com.dicoding.myunlimitedquotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvQuote.layoutManager = LinearLayoutManager(this)

        getData()
    }

    // TODO hapus pemanggilan fungsi getQuote dan ubahlah kode untuk memasukkan data ke adapter pada MainActivity
    private fun getData() {
        val adapter = QuoteListAdapter()

        // TODO tambahkan kode berikut pada MainActivity untuk mengimplementasikan LoadStateAdapter pada footer
        binding.rvQuote.adapter = adapter
        mainViewModel.getQuote()
        mainViewModel.quote.observe(this) {
            adapter.submitList(it)
        }
    }
}
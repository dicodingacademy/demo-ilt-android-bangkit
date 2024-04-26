package com.dicoding.myunlimitedquotes.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.myunlimitedquotes.data.QuoteRepository
import com.dicoding.myunlimitedquotes.di.Injection
import com.dicoding.myunlimitedquotes.network.QuoteResponseItem
import kotlinx.coroutines.launch

// TODO buka bagian kelas MainViewModel dan ubahlah isinya
class MainViewModel(private val quoteRepository: QuoteRepository) : ViewModel() {
    private val _quote = MutableLiveData<List<QuoteResponseItem>>()
    var quote: LiveData<List<QuoteResponseItem>> = _quote

    fun getQuote() {
        viewModelScope.launch {
            _quote.postValue(quoteRepository.getQuote())
        }
    }
}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.dicoding.myunlimitedquotes.data

import com.dicoding.myunlimitedquotes.database.QuoteDatabase
import com.dicoding.myunlimitedquotes.network.ApiService
import com.dicoding.myunlimitedquotes.network.QuoteResponseItem

class QuoteRepository(private val quoteDatabase: QuoteDatabase, private val apiService: ApiService) {

    //    TODO beralih ke QuoteRepository. Hapus suspend function dan ubah kembalian menjadi LiveData pada fungsi getQuote
    suspend fun getQuote(): List<QuoteResponseItem> {
        return apiService.getQuote(1, 5)
    }
}
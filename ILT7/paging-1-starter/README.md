// TODO tambahkan library yang dibutuhkan build.gradle (module: app)
```
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
```

// TODO Buatlah dengan nama QuotePagingSource, tambahkan parameter ApiService yang sudah disiapkan.
```
class QuoteListAdapter :
PagingDataAdapter<QuoteResponseItem, QuoteListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: QuoteResponseItem) {
            binding.tvItemQuote.text = data.en
            binding.tvItemAuthor.text = data.author
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<QuoteResponseItem>() {
            override fun areItemsTheSame(oldItem: QuoteResponseItem, newItem: QuoteResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: QuoteResponseItem, newItem: QuoteResponseItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
```

//    TODO beralih ke QuoteRepository. Hapus suspend function dan ubah kembalian menjadi LiveData pada fungsi getQuote

```
    fun getQuote(): LiveData<PagingData<QuoteResponseItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                QuotePagingSource(apiService)
            }
        ).liveData
    }
```


// TODO buka bagian kelas MainViewModel dan ubahlah isinya
```
class MainViewModel(quoteRepository: QuoteRepository) : ViewModel() {
    val quote: LiveData<PagingData<QuoteResponseItem>> =
         quoteRepository.getQuote().cachedIn(viewModelScope)
}
```

// TODO ubah kelas induk pada QuoteListAdapter menjadi PagingDataAdapter
```
class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadingStateViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }
}
```

// TODO ubah kelas induk pada QuoteListAdapter menjadi PagingDataAdapter
```
class QuoteListAdapter :
ListAdapter<QuoteResponseItem, QuoteListAdapter.MyViewHolder>(DIFF_CALLBACK) {
  ...
}
```

// TODO hapus pemanggilan fungsi getQuote dan ubahlah kode untuk memasukkan data ke adapter pada MainActivity
```
private fun getData() {
    val adapter = QuoteListAdapter()
    binding.rvQuote.adapter = adapter.withLoadStateFooter(
        footer = LoadingStateAdapter {
         adapter.retry()
      }
    )
    mainViewModel.quote.observe(this, {
        adapter.submitData(lifecycle, it)
    })
}`
```

// TODO Buat layout baru bernama item_loading pada res/layout

``` 
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:orientation="vertical"
android:padding="8dp">

    <TextView
        android:id="@+id/error_msg"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:textAlignment="center"
        android:textColor="?android:textColorPrimary"
        tools:text="Timeout" />

    <ProgressBar
        android:id="@+id/progress_bar"
        style="?android:attr/progressBarStyle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center" />

    <Button
        android:id="@+id/retry_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:text="@string/retry" />
</LinearLayout>
```

// TODO Jangan lupa tambahkan resource baru pada res/values/strings.xml untuk teks pada tombol
```
    <string name="retry">Coba lagi</string>
```

// TODO: buat sebuah file baru pada package adapter dengan nama LoadingStateAdapter
```
class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
   override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
       val binding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return LoadingStateViewHolder(binding, retry)
   }
   override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
       holder.bind(loadState)
   }
   class LoadingStateViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit) :
       RecyclerView.ViewHolder(binding.root) {
       init {
           binding.retryButton.setOnClickListener { retry.invoke() }
       }
       fun bind(loadState: LoadState) {
           if (loadState is LoadState.Error) {
               binding.errorMsg.text = loadState.error.localizedMessage
           }
           binding.progressBar.isVisible = loadState is LoadState.Loading
           binding.retryButton.isVisible = loadState is LoadState.Error
           binding.errorMsg.isVisible = loadState is LoadState.Error
       }
   }
}
```

// TODO tambahkan kode berikut pada MainActivity untuk mengimplementasikan LoadStateAdapter pada footer
```
private fun getData() {
    val adapter = QuoteListAdapter()
    binding.rvQuote.adapter = adapter.withLoadStateFooter(
        footer = LoadingStateAdapter {
            adapter.retry()
        }
    )
    mainViewModel.quote.observe(this, {
        adapter.submitData(lifecycle, it)
    })
}
```
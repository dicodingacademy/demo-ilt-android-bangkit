[1] Ensure your project is connected to Firebase Realtime Database and don't forget to update the google-services.json file.
[2] Add the required libraries, such as Firebase Realtime Database, Firebase UI, and Glide.

`    implementation("com.google.firebase:firebase-database-ktx:20.2.2")
    implementation("com.firebaseui:firebase-ui-database:8.0.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")`

[3] Add a data model to hold user message information.
`@IgnoreExtraProperties
data class Message(
    val text: String? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val timestamp: Long? = null
){
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}`

[4] Next, add the following text to res/values/strings.xml first.
`<string name="send_success">Berhasil terkirim</string>
<string name="send_error">Gagal terkirim</string>`

[5] Initialize the database and perform the insert process when the submit button is clicked.
`class MainActivity : AppCompatActivity() {
   ...

    private lateinit var db: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        ...

        db = Firebase.database
        val messagesRef = db.reference.child(MESSAGES_CHILD)

        binding.sendButton.setOnClickListener {
            val friendlyMessage = Message(
                binding.messageEditText.text.toString(),
                firebaseUser.displayName.toString(),
                firebaseUser.photoUrl.toString(),
                Date().time
            )
            messagesRef.push().setValue(friendlyMessage) { error, _ ->
                if (error != null) {
                    Toast.makeText(this, getString(R.string.send_error) + error.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, getString(R.string.send_success), Toast.LENGTH_SHORT).show()
                }
            }
            binding.messageEditText.setText("")
        }
    }

    ...

    companion object {
        const val MESSAGES_CHILD = "messages"
    }
}`

[6] Create a FirebaseMessageAdapter to display each message in a RecyclerView.
`class FirebaseMessageAdapter(
  options: FirebaseRecyclerOptions<Message>,
  private val currentUserName: String?
) : FirebaseRecyclerAdapter<Message, FirebaseMessageAdapter.MessageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_message, parent, false)
        val binding = ItemMessageBinding.bind(view)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int,
        model: Message
    ) {
        holder.bind(model)
    }

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.tvMessage.text = item.text
            setTextColor(item.name, binding.tvMessage)
            binding.tvMessenger.text = item.name
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .circleCrop()
                .into(binding.ivMessenger)
            if (item.timestamp != null) {
                binding.tvTimestamp.text = DateUtils.getRelativeTimeSpanString(item.timestamp)
            }
        }
        private fun setTextColor(userName: String?, textView: TextView) {
            if (currentUserName == userName && userName != null) {
                textView.setBackgroundResource(R.drawable.rounded_message_blue)
            } else {
                textView.setBackgroundResource(R.drawable.rounded_message_yellow)
            }
        }
    }
}`

[7] Initialize Firebase Realtime Database and fetch data to display in a RecyclerView.

`    private lateinit var adapter: FirebaseMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        ...

        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager

        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(messagesRef, Message::class.java)
            .build()
        adapter = FirebaseMessageAdapter(options, firebaseUser.displayName)
        binding.messageRecyclerView.adapter = adapter
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }
    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }`
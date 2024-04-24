
[1] Connect Firebase Auth to Project. Ensure you have added the **google-services.json** file.
[2] Make sure you add the library, include add library Firebase, Credential Manager API, and Lifecycle. Add code for Toml file.
`[versions]
# TODO Dependency Version
googleServices = "4.4.1"
credentials = "1.2.2"
googleid = "1.1.0"
lifecycleRuntime = "2.7.0"
firebaseBom = "32.8.1"

[libraries]
# TODO Dependency Libraries
google-services = { group = "com.google.gms", name = "google-services", version.ref = "googleServices" }
credentials = { group = "androidx.credentials", name = "credentials", version.ref = "credentials" }
credentials-play-services-auth = { group = "androidx.credentials", name = "credentials-play-services-auth", version.ref = "credentials" }
googleid = { group = "com.google.android.libraries.identity.googleid", name = "googleid", version.ref = "googleid" }
lifecycle-runtime = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntime" }
firebase-bom = { module = "com.google.firebase:firebase-bom", version.ref = "firebaseBom" }
firebase-auth-ktx = { module = "com.google.firebase:firebase-auth-ktx" }

[bundles]
# TODO Add Bundles Dependency
firebase = [ "firebase-auth-ktx" ]
creadential = [ "credentials", "credentials-play-services-auth", "googleid"]`

[3] Add Dependency at build.gradle.kts (Project: My_Firebase_Chat)
`// TODO Add Google Service
buildscript { 
  dependencies { 
    classpath(libs.google.services)
  }
}`

[4] Add Dependency at build.gradle.kts (Module :app)
`plugins {
  ...
  // Add Plugin
  id("com.google.gms.google-services")
}

dependencies {
    // TODO: Add Dependency
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    implementation(libs.bundles.credential)
    implementation(libs.lifecycle.runtime)
}`

[5] Add Sign In Button in activity_login.xml.
`<com.google.android.gms.common.SignInButton
android:id="@+id/signInButton"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
app:layout_constraintBottom_toBottomOf="parent"
app:layout_constraintEnd_toEndOf="parent"
app:layout_constraintStart_toStartOf="parent"
app:layout_constraintTop_toTopOf="parent" />`

[6] Instance Firebase Auth on LoginActivity.
`private lateinit var auth: FirebaseAuth
override fun onCreate(savedInstanceState: Bundle?) {
  ...
  // Initialize Firebase Auth
  auth = Firebase.auth

  // Initialize signInButton onClickListener 
  binding.signInButton.setOnClickListener {
    signIn()
  } 
}`

[7] Configuration for credentialManager
`val credentialManager = CredentialManager.create(this) //import from androidx.CredentialManager

val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.web_client_id)) //from https://console.firebase.google.com/project/my-firebase-chat-2aac3/authentication/providers
            .build()

val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
            .addCredentialOption(googleIdOption)
            .build()

lifecycleScope.launch {
  try {
    val result = credentialManager.getCredential( //import from androidx.CredentialManager
      request = request,
      context = this@LoginActivity,
    )
    handleSignIn(result)
  } catch (e: GetCredentialException) { //import from androidx.CredentialManager
    Log.d("Error", e.message.toString())
  }
}`

[8] Handle the successfully returned credential.
`private fun handleSignIn(result: GetCredentialResponse) { 
  // Handle the successfully returned credential. 
  when (val credential = result.credential) { 
    is CustomCredential -> {
      if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
          // Use googleIdTokenCredential and extract id to validate and authenticate on your server.
          val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
          firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } catch (e: GoogleIdTokenParsingException) {
          Log.e(TAG, "Received an invalid google id token response", e)
        }
      } else {
        // Catch any unrecognized custom credential type here.
        Log.e(TAG, "Unexpected type of credential")
      }
    } else -> {
      // Catch any unrecognized credential type here.
      Log.e(TAG, "Unexpected type of credential")
    }
  }
}`

[9]  Sign in with Google using id Token from Credential
`val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
auth.signInWithCredential(credential)
  .addOnCompleteListener(this) { task ->
    if (task.isSuccessful) {
      // Sign in success, update UI with the signed-in user's information
      Log.d(TAG, "signInWithCredential:success")
      val user: FirebaseUser? = auth.currentUser
      updateUI(user)
    } else {
      // If sign in fails, display a message to the user.
      Log.w(TAG, "signInWithCredential:failure", task.exception)
      updateUI(null)
    } 
}`

[10] Check if user is signed in (non-null) and update UI accordingly.
`override fun onStart() {
  super.onStart()
  val currentUser = auth.currentUser
  updateUI(currentUser)
}

private fun updateUI(currentUser: FirebaseUser?) {
  if (currentUser != null) {
    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    finish()
  }
}`

[11] Instance Firebase Auth and check currentUser on MainActivity.
`private lateinit var binding: ActivityMainBinding
override fun onCreate(savedInstanceState: Bundle?) { 
  super.onCreate(savedInstanceState)
  ...
  // Initialize Firebase Auth
  auth = Firebase.auth

  // Check Current User from Firebase Auth
  val firebaseUser = auth.currentUser
  if (firebaseUser == null) {
    // Not signed in, launch the Login activity
    startActivity(Intent(this, LoginActivity::class.java))
    finish()
    return
  }
}`

[12] Add signOut() to logout feature.
`auth.signOut()`
 
[13] Add Web client id from //from https://console.firebase.google.com/project/[your_project_firebase_id]/authentication/providers
`<string name="web_client_id">829724428149-1e2h9rvc8m3j4g8c4sq9hn61e9u9b6pp.apps.googleusercontent.com</string>`
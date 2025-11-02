package com.example.playground.ui.features.auth.login.viewModel

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playground.R
import com.example.playground.ui.features.auth.authShared.AuthState
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//  ViewModel for Google Authentication
class GoogleAuthViewModel : ViewModel() {

    // firebase auth instance = get the one and only firebsae instance
    private val auth = FirebaseAuth.getInstance()

    // Mutable state flow for authentication state
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    //public state flow for authentication state
    //other class can access this but cannot modify
    val authState: StateFlow<AuthState> = _authState

    // COMPANION OBJECT: Constants and helper functions
    // Shared "classroom rules" for all instances
    companion object {
        private const val TAG = "GoogleAuthViewModel"  // Logging tag ; putting a name on a notebook
    }

    // Suspend fun : can pause without freezing the app
    suspend fun signInWithGoogle(context: Context) {
       // Set the auth state to loading before attempting to show loading
        _authState.value = AuthState.Loading

        val webClientId = context.getString(R.string.web_client_id)


        // try catch block to handle exceptions
        try { // "Try to do everything in this block"

            // Build the option
            val googleOption = GetGoogleIdOption.Builder() // builder pattern
                .setFilterByAuthorizedAccounts(false) // show all google accounts or true if you want only previously authorized accounts
                .setServerClientId(webClientId) // app id
                .setNonce(null) // provide a nonce if you use one for extra security
                .build() // finalize options

            // Build the request
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleOption) // add google option to request
                .build()

            // Create CredentialManager instance ( the magician )
            val credentialManager = CredentialManager.create(context)

            // Launch the credential retrieval flow. The system will show the bottom sheet / consent UI as needed.
            //call the suspen fun to get the credential
            val result = credentialManager.getCredential(context, request)
            // google checks credential 'yes' this is legit token
            val credential = result.credential

            // Handle the credential result
            if (credential is GoogleIdTokenCredential) { // if its a goole token
                // we got a valid token
                handleSignInResult(credential)
            } else {
                // got something unexpected
                _authState.value =
                    AuthState.Error("Unexpected credential type: ${credential::class.java.simpleName}")
            }

        } catch (e: GetCredentialException) {
            // Handle specific credential exceptions
            //here user cancles or no internet
            Log.e(TAG, "Google sign-in getCredential failed", e)
            _authState.value = AuthState.Error("Google sign-in failed: ${e.message}")

        } catch (e: Exception) { // catch any other errors
            // Handle other potential exceptions

            // app crash or memory issues etc
            Log.e(TAG, "Google sign-in setup failed", e)
            _authState.value = AuthState.Error("Google sign-in setup failed: ${e.message}")
        }
    }

    //  Process the Google credential
    fun handleSignInResult(credential: GoogleIdTokenCredential) {
        _authState.value = AuthState.Loading // show loading

        viewModelScope.launch { // dont freeze the app do it in background
            try {
                // EXTRACT GOOGLE ID TOKEN: secrete password
                val googleIdToken = credential.idToken

                if (googleIdToken != null) { // if we got a valid token

                    // CREATE FIREBASE CREDENTIAL WITH GOOGLE TOKEN
                    val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

                    //  AUTHENTICATE WITH FIREBASE : send credential to firebase
                    // await = wait here until firebase response but dont freeze the app
                    val authResult: AuthResult = auth.signInWithCredential(firebaseCredential).await()

                    // CHECK IF USER SUCCESSFULLY AUTHENTICATED
                    val user = authResult.user
                    if (user != null) { // if firebase return a user

                        // SUCCESS: User signed in with Google + Firebase
                        Log.d(TAG, "Google sign-in successful: ${user.email}")
                        _authState.value = AuthState.Success("Welcome, ${user.displayName ?: "User"}!")
                    } else {
                        // ❌ FAILED: Firebase authentication didn't return user
                        Log.e(TAG, "Firebase authentication failed - no user returned")
                        _authState.value = AuthState.Error("Authentication failed - please try again")
                    }
                } else {
                    // ❌ FAILED: No Google ID token in credential
                    Log.e(TAG, "No Google ID token received")
                    _authState.value = AuthState.Error("Authentication failed - no token received")
                }

            } catch (e: Exception) { // firebase errors
                // ❌ ERROR: Handle authentication failures
                Log.e(TAG, "Google sign-in authentication failed: ${e.message}", e)
                _authState.value = AuthState.Error("Sign-in failed: ${e.message}")
            }
        }
    }

    // RESET AUTH STATE
    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    // SIGN OUT: From both Google and Firebase
    fun signOut() {
        viewModelScope.launch { // background task
            try {
                auth.signOut()  // sign out from Firebase
                _authState.value = AuthState.Idle // reset
                Log.d(TAG, "User signed out successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Sign out failed: ${e.message}", e)
                _authState.value = AuthState.Error("Sign out failed: ${e.message}")
            }
        }
    }

    // GET CURRENT USER: Check if user is already signed in
    fun getCurrentUser() = auth.currentUser
}
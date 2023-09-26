package me.varoa.nongki.ui.screen.login

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import logcat.logcat
import me.varoa.nongki.BuildConfig
import me.varoa.nongki.R
import me.varoa.nongki.databinding.FragmentLoginBinding
import me.varoa.nongki.ext.navigateTo
import me.varoa.nongki.ext.toast
import me.varoa.nongki.utils.viewbinding.viewBinding

// refer to firebase auth snippets-android
// https://github.com/firebase/snippets-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/kotlin/GoogleSignInActivity.kt
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding<FragmentLoginBinding>()

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val signInLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = requireNotNull(task.getResult(ApiException::class.java))
                logcat { "firebaseAuthWithGoogle:${account.idToken}" }
                account.idToken?.let(::firebaseAuthWithGoogle)
            } catch (e: ApiException) {
                logcat { e.message.toString() }
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                logcat { e.message.toString() }
                e.printStackTrace()
            }
        }

    private fun firebaseAuthWithGoogle(idToken: String) {
        logcat { "firebaseAuthWithGoogle($idToken)" }
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    logcat { "signInWithCredential:success" }
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    logcat { "signInWithCredential:failure" }
                    updateUI(null)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.WEB_OAUTH_CLIENT_ID)
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnLogin.setOnClickListener {
                onLogin()
            }
        }
    }

    private fun onLogin() {
        val loginIntent = googleSignInClient.signInIntent
        signInLauncher.launch(loginIntent)
    }

    private fun updateUI(user: FirebaseUser?) {
        user?.let {
            toast("Hi ${it.displayName}, selamat datang!")
            navigateTo(LoginFragmentDirections.actionLoginToHome())
        }
    }
}

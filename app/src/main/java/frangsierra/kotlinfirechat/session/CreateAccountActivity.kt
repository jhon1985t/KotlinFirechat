package frangsierra.kotlinfirechat.session

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import frangsierra.kotlinfirechat.R
import frangsierra.kotlinfirechat.common.flux.FluxActivity
import frangsierra.kotlinfirechat.session.store.SessionStore
import frangsierra.kotlinfirechat.util.GoogleLoginCallback
import frangsierra.kotlinfirechat.util.dismissProgressDialog
import frangsierra.kotlinfirechat.util.onError
import frangsierra.kotlinfirechat.util.toast
import kotlinx.android.synthetic.main.create_account_activity.*
import javax.inject.Inject


class CreateAccountActivity : FluxActivity(), GoogleLoginCallback {

    @Inject
    lateinit var sessionStore: SessionStore

    override val googleApiClient: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
    }

    override val googleSingInClient: GoogleSignInClient by lazy { GoogleSignIn.getClient(this, googleApiClient) }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(frangsierra.kotlinfirechat.R.layout.create_account_activity)

        initializeInterface()
        startListeningStoreChanges()
    }

    private fun startListeningStoreChanges() {

    }

    private fun initializeInterface() {
        createAccountButton.setOnClickListener { signInWithEmailAndPassword() }
        signInButton.setOnClickListener { logInWithGoogle(this) }
    }

    private fun signInWithEmailAndPassword() {
        if (!fieldsAreFilled()) return
    }

    //How to retrieve SHA1 for Firebase Google Sign In https://stackoverflow.com/questions/15727912/sha-1-fingerprint-of-keystore-certificate
    private fun signInWithCredential(credential: AuthCredential, email: String) {

    }

    override fun onGoogleCredentialReceived(credential: AuthCredential, account: GoogleSignInAccount) {
        signInWithCredential(credential, account.email!!)
    }

    override fun onGoogleSignInFailed(e: ApiException) {
        dismissProgressDialog()
        toast(e.toString())
    }

    private fun fieldsAreFilled(): Boolean {
        editTextUsername.text.toString().takeIf { it.isEmpty() }?.let {
            inputUsername.onError(getString(frangsierra.kotlinfirechat.R.string.error_cannot_be_empty))
            return false
        }
        inputUsername.onError(null, false)

        editTextEmail.text.toString().takeIf { it.isEmpty() }?.let {
            inputEmail.onError(getString(frangsierra.kotlinfirechat.R.string.error_cannot_be_empty))
            return false
        }
        inputEmail.onError(null, false)

        editTextPassword.text.toString().takeIf { it.isEmpty() }?.let {
            inputPassword.onError(getString(frangsierra.kotlinfirechat.R.string.error_cannot_be_empty))
            return false
        }
        editTextPassword.text.toString().takeIf { it.length < 6 }?.let {
            inputPassword.onError(getString(frangsierra.kotlinfirechat.R.string.error_invalid_password_not_valid))
            return false
        }
        inputPassword.onError(null, false)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        manageGoogleResult(requestCode, data)
    }
}

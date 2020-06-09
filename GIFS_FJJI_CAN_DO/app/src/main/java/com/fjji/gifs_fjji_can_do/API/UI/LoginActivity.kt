package com.fjji.gifs_fjji_can_do.API.UI

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fjji.gifs_fjji_can_do.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_login.*

// google info: https://www.youtube.com/watch?v=i18IGN3MAbw

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var googleSignInClient: GoogleSignInClient


    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions

    val RC_SIGN_IN: Int = 1
    lateinit var signOut: Button
    companion object {
        private const val TAG = "EmailPassword"
        var activeUser: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtEmail            = findViewById(R.id.et_login_email)
        txtPassword         = findViewById(R.id.et_login_password)
        progressBar         = findViewById(R.id.progressBarLogin)


        val provider = intent.extras?.getString("provider")


        //Google
        val signIn = findViewById<View>(R.id.sign_in_button_google) as SignInButton

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()

        signOut = findViewById<View>(R.id.sign_out_test) as Button
        signOut.visibility = View.INVISIBLE
        signIn.setOnClickListener{
            view: View? -> signInGoogle ()
        }

        // User/ PassWord
        txtEmail.afterTextChanged {
            if (!it.isEmailValid()) {
                txtEmail.error = "Is not a valid email"
            }
        }
        txtPassword.afterTextChanged {
            if( it.length <6){
                txtPassword.error = "The password is not length enough "
            }
        }


    }

    private fun signInGoogle(){
        var signIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task= GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                activeUser = account!!.displayName
                startActivity(Intent(this, MenuActivity::class.java))
                updateUI(account!!)
                mGoogleSignInClient.signOut().addOnCompleteListener{
                task: Task<Void> ->  tv_test_user.text = ""
                signOut.visibility = View.INVISIBLE}

            }catch (e: ApiException){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                val tv_test_user = findViewById<View>(R.id.tv_test_user) as TextView
                tv_test_user.text = "fail"
            }
        }
    }


    private fun updateUI(account: GoogleSignInAccount){
        val tv_test_user = findViewById<View>(R.id.tv_test_user) as TextView
        tv_test_user.text = account.displayName
        signOut.visibility = View.VISIBLE
        signOut.setOnClickListener{
            view: View? -> mGoogleSignInClient.signOut().addOnCompleteListener{
            task: Task<Void> ->  tv_test_user.text = ""
            signOut.visibility = View.INVISIBLE
        }
        }
    }

    fun forgotPassword(view: View){
        startActivity(Intent(this, ForgotActivity::class.java))
    }
    fun register(view: View){
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }
    fun login(view: View){
        loginUser()
    }
    private fun loginUser(){
        val correo:String = txtEmail.text.toString()
        val password:String = txtPassword.text.toString()
        if (!TextUtils.isEmpty(correo) && !TextUtils.isEmpty(password)){
            progressBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(this) {
                task ->

                if (task.isSuccessful){
                    if(auth.currentUser!!.displayName != null){
                        activeUser = auth.currentUser!!.displayName
                    }
                    else{
                        activeUser = auth.currentUser!!.email
                    }
                    action()
                    progressBar.visibility = View.INVISIBLE
                }
                else{
                    Toast.makeText(this,"Error al hacer Log in ¿ingresaste bien tus datos?", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
        else{
            Toast.makeText(this,"Error al hacer Log in ¿ingresaste bien tus datos?", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun action(){
        startActivity(Intent(this, MenuActivity::class.java))
    }
}
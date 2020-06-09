package com.fjji.gifs_fjji_can_do.API.UI

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fjji.gifs_fjji_can_do.API.UI.MenuActivity
import com.fjji.gifs_fjji_can_do.R
import com.fjji.gifs_fjji_can_do.afterTextChanged
import com.fjji.gifs_fjji_can_do.isEmailValid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {

    private lateinit var txtNombreUsuario: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        txtNombreUsuario    = findViewById(R.id.txtNombreUsuario)
        txtEmail            = findViewById(R.id.txtEmail)
        txtPassword         = findViewById(R.id.txtPassword)

        progressBar         = findViewById(R.id.progressBar)

        database            = FirebaseDatabase.getInstance()
        auth                = FirebaseAuth.getInstance()

        dbReference         = database.reference.child("User")
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

    fun register ( view: View){
        createNewAccount()
    }
    private fun createNewAccount(){
        val name: String        = txtNombreUsuario.text.toString()
        val email: String       = txtEmail.text.toString()
        val password: String    = txtPassword.text.toString()

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            progressBar.visibility = View.VISIBLE
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
                task ->

                if(task.isComplete){
                    var user:FirebaseUser? = auth.currentUser
                    if (user == null){
                        return@addOnCompleteListener
                    }
                    val userDB = dbReference.child(user!!.uid)

                    val profileUpdates =
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build()

                    user!!.updateProfile(profileUpdates)
                    verifyEmail(user)

                    userDB.child("Name").setValue(name)
                    action()
                }
                else{
                    Toast.makeText(this,"Que pasa", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun action(){
        LoginActivity.activeUser = txtNombreUsuario.text.toString()
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }

    private fun verifyEmail(user:FirebaseUser?){
        user?.sendEmailVerification()?.addOnCompleteListener(this){
            task ->

            if(task.isComplete){
                Toast.makeText(this,"Email enviado", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,"Error al enviar email", Toast.LENGTH_LONG).show()
            }
        }
    }
}
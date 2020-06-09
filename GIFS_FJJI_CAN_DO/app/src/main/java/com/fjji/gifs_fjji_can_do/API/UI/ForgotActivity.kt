package com.fjji.gifs_fjji_can_do.API.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.ViewAnimator
import com.fjji.gifs_fjji_can_do.R
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : AppCompatActivity() {

    private lateinit var txtEmail: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        txtEmail            = findViewById(com.fjji.gifs_fjji_can_do.R.id.forgotEmail)
        progressBar         = findViewById(com.fjji.gifs_fjji_can_do.R.id.progressBarForgot)

        auth = FirebaseAuth.getInstance()
    }
    fun send(v: View){
        val email = txtEmail.text.toString()
        progressBar.visibility = View.VISIBLE
        if(!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){
                    finish()
                }
                else{
                    Toast.makeText(this,"Error al enviar email ¿ingresaste correo?", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.INVISIBLE

                }
            }
        }
    }
}
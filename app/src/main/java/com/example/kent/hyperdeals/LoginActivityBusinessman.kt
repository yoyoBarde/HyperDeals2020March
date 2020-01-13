package com.example.kent.hyperdeals

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.kent.hyperdeals.Model.UserBusinessmanvarParce
import com.example.kent.hyperdeals.NavigationBar.DashboardActivity
import com.example.kent.hyperdeals.NavigationBar.DrawerActivityBusinessman
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.loginactivity.*
import kotlinx.android.synthetic.main.loginactivitybusinessman.*
import kotlinx.android.synthetic.main.registrationactivitybusinessman.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class LoginActivityBusinessman : AppCompatActivity() {

val TAG = "LoginBusinessman"
    private var mAuth: FirebaseAuth? = null
var database = FirebaseFirestore.getInstance()

    companion object {
        lateinit var globalUserBusinessman: UserBusinessmanvarParce
         var userBusinessManUsername:String = "juriusu25@gmail.com"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginactivitybusinessman)


        BusinessmanloginEmail.setText("juriusu25@gmail.com")
        BusinessmanloginPassword.setText("febuary25")
        val login = findViewById<View>(R.id.BusinessmanloginButton)

        login.setOnClickListener    {

            val loginEmail = findViewById<View>(R.id.BusinessmanloginEmail) as EditText
            val loginPassword = findViewById<View>(R.id.BusinessmanloginPassword) as EditText
            val loginProgressBar = findViewById<View>(R.id.loginProgressBarBM) as ProgressBar


            var LoginEmail = loginEmail.text.toString()
            var LoginPassword = loginPassword.text.toString()

            mAuth = FirebaseAuth.getInstance()

            if (!LoginEmail.isEmpty() && !LoginPassword.isEmpty()) {
                LoginActivity.userUIDS = LoginEmail
                doAsync {

                    signinAuth(LoginEmail,LoginPassword)

                }

            }
            else {
                Toast.makeText(this,"Enter necessary credentials", Toast.LENGTH_SHORT).show()

            }
        }


    }

    fun signinAuth(LoginEmail:String,LoginPassword:String){

        mAuth!!.signInWithEmailAndPassword(LoginEmail, LoginPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful){

                        userBusinessManUsername = LoginEmail
                        Toast.makeText(this,"Login Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivityBusinessman, DrawerActivityBusinessman::class.java))
                    } else {
                        Toast.makeText(this,"Login Failure", Toast.LENGTH_SHORT).show()

                    }

                }
    }
}

package com.example.kent.hyperdeals

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.kent.hyperdeals.Admin.Admin
import com.example.kent.hyperdeals.FragmentsBusiness.Business_PromoProfile
import com.example.kent.hyperdeals.NavigationBar.DashboardActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.loginactivity.*

class LoginActivity : AppCompatActivity() {
companion object{

    var userUIDS="juriusu25@gmail.com"

}

    private val TAG = "LoginActivity"
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginactivity)

   //     startActivity(Intent(this,InitializeCategory::class.java))

      //  startActivity(Intent(this,DashboardActivity::class.java))
loginEmail.setText("juriusu25@gmail.com")
loginPassword.setText("febuary25")
        val login = findViewById<View>(R.id.loginButton)

        login.setOnClickListener{


            val loginEmail = findViewById<View>(R.id.loginEmail) as EditText
            val loginPassword = findViewById<View>(R.id.loginPassword) as EditText
            val loginButton = findViewById<View>(R.id.loginButton) as Button
            val loginForgotPassword = findViewById<View>(R.id.loginForgotPassword) as TextView
            val loginProgressBar = findViewById<View>(R.id.loginProgressBar) as ProgressBar



            if(loginEmail.text.toString()=="admin" && loginPassword.text.toString() == "admin"){
                val intent = Intent(this, Admin::class.java)
                startActivity(intent)


            }

            var LoginEmail = loginEmail.text.toString()
            var LoginPassword = loginPassword.text.toString()

            mAuth = FirebaseAuth.getInstance()

            if (!LoginEmail.isEmpty() && !LoginPassword.isEmpty()) {
                        loginProgressBar.visibility = View.VISIBLE

                mAuth!!.signInWithEmailAndPassword(LoginEmail, LoginPassword)
                        .addOnCompleteListener(this) { task ->
                            loginProgressBar.visibility = View.INVISIBLE

                            if (task.isSuccessful){
                                userUIDS = LoginEmail
                                Toast.makeText(this,"Login Successful!", Toast.LENGTH_SHORT).show()
                            //    getUserUID(LoginEmail)
                                val intent = Intent(this, DashboardActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this,"Login Failure", Toast.LENGTH_SHORT).show()

                            }

                        }
            }

            else {
                Toast.makeText(this,"Enter necessary credentials", Toast.LENGTH_SHORT).show()

            }
        }

    }


}



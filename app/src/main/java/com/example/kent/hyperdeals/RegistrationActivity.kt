package com.example.kent.hyperdeals

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.example.kent.hyperdeals.Model.EmailUID
import com.example.kent.hyperdeals.Model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.registrationactivity.*
import java.util.HashMap
import android.widget.RadioButton
import android.widget.RadioGroup
import org.jetbrains.anko.toast


class RegistrationActivity : AppCompatActivity() {


    val TAG = "RegistrationAct"
    var mDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    companion object {
       var globalRegisteredUsername:String = "juriusu25@gmail.com"
    }
    var mDatabaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth= FirebaseAuth.getInstance()

    var mDatabase: FirebaseDatabase? = null
    var userGender = "Male"
    var userStatus = "Single"
    val UserModel = HashMap<String, Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrationactivity)

        radioGroupGender.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->

            var radio = group.findViewById(checkedId) as RadioButton
            Log.e(TAG," result checked - ${radio.text.toString()} ")
            userGender = radio.text.toString()

        })
        radioGroupStatus.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            var radio = group.findViewById(checkedId) as RadioButton
            Log.e(TAG," result checked - ${radio.text.toString()} ")
            userStatus = radio.text.toString()

        })




        val submit = findViewById<View>(R.id.btnsubmit) as Button

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")


        submit.setOnClickListener {


            val tvLastName = findViewById<View>(R.id.lastname) as EditText
            val tvFirstName = findViewById<View>(R.id.firstname) as EditText
            val tvEmail = findViewById<View>(R.id.email) as EditText
            val tvPassword = findViewById<View>(R.id.password) as EditText
            val tvAge = findViewById<View>(R.id.etAge) as EditText
            val rbMale = findViewById<View>(R.id.radioButtonMale) as RadioButton
            val rbSingle = findViewById<View>(R.id.radioButtonSingle) as RadioButton


            var email = tvEmail.text.toString()
            var password = tvPassword.text.toString()
            var lastname = tvLastName.text.toString()
            var firstname = tvFirstName.text.toString()
            var age = tvAge.text.toString()



            var myUser =UserModel(firstname,lastname,email,password,age,userGender,userStatus)


            var message =" "

            if (!email.isEmpty() && !password.isEmpty() && !lastname.isEmpty() && !firstname.isEmpty()) {
                var Boolean = true
                if(age.isEmpty()){
                    Boolean = false
                    message = "Age is empty"
                }
                if(!password.matches(password2.text.toString().toRegex())){
                    Boolean = false
                    message = "Password doesn't match"

                }

                if(Boolean){

                mAuth!!.createUserWithEmailAndPassword(email!!, password!!)
                        .addOnCompleteListener(this) {task ->


                            if (task.isSuccessful)
                            {
                                var myEmailUID= EmailUID(email,mAuth.currentUser!!.uid)
                                mDb.collection("Users").document(myUser.Email).set(myUser)
                                mDb.collection("EmailUID").document(myUser.Email).set(myEmailUID)
                                val userID = mAuth!!.currentUser!!.uid
                                val currentUserDb = mDatabaseReference!!.child(userID)
                                currentUserDb.child("firstName").setValue(firstname)
                                currentUserDb.child("lastName").setValue(lastname)
                                Toast.makeText(this, "Registered Success!", Toast.LENGTH_SHORT).show()

                               // val intent = Intent(this,LoginActivity::class.java)
                                globalRegisteredUsername = email

                                val intent = Intent(this,InitializeCategory::class.java)
                                startActivity(intent)

                            } else {
                                Toast.makeText(this,"Error!", Toast.LENGTH_SHORT).show()

                            }
                        }
            }
                else{
                    toast(message)
                    Log.e(TAG,message)
                }
                }

            else {
                Toast.makeText(this, "Enter credentials!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
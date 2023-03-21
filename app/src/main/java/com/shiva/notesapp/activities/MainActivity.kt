package com.shiva.notesapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.shiva.notesapp.R

class MainActivity : AppCompatActivity() {


    lateinit var googleSignIn:Button
    lateinit var googleSignInClint:GoogleSignInClient
    lateinit var firebaseAuth: FirebaseAuth
    private val RC_SIGN_IN = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        googleSignIn = findViewById(R.id.google_signIn_Button)
        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()

        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("446262868011-2ifrmhv0upa0d3f5lf41cgal2gsmse0f.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClint = GoogleSignIn.getClient(this , googleSignInOption)

        googleSignIn.setOnClickListener {
            val intent = googleSignInClint.signInIntent

            startActivityForResult(intent , RC_SIGN_IN)
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when(requestCode) {
            RC_SIGN_IN-> {
                val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)

                try {
                    val account = accountTask.getResult(ApiException::class.java)
                    firebaseAuthWithGoogleAccount(account)
                }
                catch (e:Exception) {
                    Toast.makeText(this , "SignIn Failed" , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                startActivity(Intent(this,NotesActivity::class.java))

                overridePendingTransition(0,0)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this , it.message , Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser!=null) {
            startActivity(Intent(this,NotesActivity::class.java))

            overridePendingTransition(0,0)
            finish()
        } else{
            firebaseAuth.signOut()
        }
    }
}
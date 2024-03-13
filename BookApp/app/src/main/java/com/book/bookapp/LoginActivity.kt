package com.book.bookapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.book.bookapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    val context = this
    lateinit var bin: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bin.root)

        clickListener()

    }

    private fun clickListener() {

        bin.btnLogin.setOnClickListener {
            checkValidation()
        }
    }

    private fun checkValidation() {
        if (bin.email.text.toString().trim().isEmpty()) {
            Func.showDialog(context, getString(R.string.alert), getString(R.string.enter_email))
            return
        }
        if (bin.pass.text.toString().trim().isEmpty()) {
            Func.showDialog(context, getString(R.string.alert), getString(R.string.enter_password))
            return
        } else {
            Func.loadingDialog(true, context)
            loginAccount()
        }
    }

    private fun loginAccount() {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(
                bin.email.text.toString().trim(),
                bin.pass.text.toString().trim()
            )
            .addOnCompleteListener {
                Func.loadingDialog(false, context)
                if (it.isSuccessful) {
                    Toast.makeText(context,
                        getString(R.string.login_successfully_), Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(context, MainActivity::class.java)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }
            .addOnFailureListener {
                Func.loadingDialog(false, context)
                Func.showDialog(context, getString(R.string.alert),
                    getString(R.string.email_or_password_wrong_please_try_again_))

            }
    }


}
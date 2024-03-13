package com.book.bookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.book.bookapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.index.IndexEntry

class MainActivity : AppCompatActivity() {

    var context = this
    lateinit var bin: ActivityMainBinding

    var aryList: ArrayList<Book> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bin.root)

        clickListener()

    }

    override fun onResume() {
        super.onResume()

        getData()

    }

    private fun getData() {
        Func.loadingDialog(true, context)
        FirebaseFirestore.getInstance().collection("books")
            .get()
            .addOnCompleteListener {

                Func.loadingDialog(false, context)
                aryList.clear()

                if (it.isSuccessful && it.result.size() > 0){
                    aryList.addAll(it.result.toObjects(Book::class.java))
                }else{
                    Toast.makeText(context, getString(R.string.no_book_found_), Toast.LENGTH_SHORT).show()
                }

                bin.recycler.adapter = BookAdapter(context, aryList)

            }
    }

    private fun clickListener() {

        bin.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, LoginActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }

    }

    private fun _init() {

    }


}
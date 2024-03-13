package com.book.bookapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.book.bookapp.databinding.ActivityBookBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class BookActivity : AppCompatActivity() {

    var context = this
    lateinit var bin: ActivityBookBinding

    lateinit var book: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityBookBinding.inflate(layoutInflater)
        setContentView(bin.root)

        setData()

        clickListener()

    }

    private fun clickListener() {

        bin.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        bin.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                book.readBy.add(FirebaseAuth.getInstance().currentUser!!.uid)
                Func.loadingDialog(true, context)
                FirebaseFirestore.getInstance().collection("books")
                    .document(book.id)
                    .set(book)
                    .addOnCompleteListener {
                        Func.loadingDialog(false, context)
                        if (it.isSuccessful){
                            Toast.makeText(context,
                                getString(R.string.book_is_marked_as_read_), Toast.LENGTH_SHORT).show()
                            bin.bookName.setTextColor(getColor(R.color.green))
                            bin.authorName.setTextColor(getColor(R.color.green))
                        }else{
                            Toast.makeText(context,
                                getString(R.string.update_failed_), Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                book.readBy.remove(FirebaseAuth.getInstance().currentUser!!.uid)
                Func.loadingDialog(true, context)
                FirebaseFirestore.getInstance().collection("books")
                    .document(book.id)
                    .set(book)
                    .addOnCompleteListener {
                        Func.loadingDialog(false, context)
                        if (it.isSuccessful){
                            Toast.makeText(context,
                                getString(R.string.book_is_removed_as_read_), Toast.LENGTH_SHORT).show()
                            bin.bookName.setTextColor(getColor(R.color.red))
                            bin.authorName.setTextColor(getColor(R.color.red))
                        }else{
                            Toast.makeText(context, getString(R.string.update_failed_), Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }

    private fun setData() {
        book = Gson().fromJson(intent.getStringExtra("data").toString(), Book::class.java)

        bin.bookName.text = book.bookName
        bin.authorName.text = getString(R.string.author)+": "+book.authorName
        bin.publicationYear.text = getString(R.string.publishing_in)+": "+book.bookName
        bin.descTV.text = book.bookDesc

        Glide.with(context).load(book.imageUrl).into(bin.image)

        if (book.readBy.contains(FirebaseAuth.getInstance().currentUser?.uid)){
            bin.checkbox.visibility = View.GONE
            bin.bookName.setTextColor(getColor(R.color.green))
            bin.authorName.setTextColor(getColor(R.color.green))
        }else{
            // visible
            bin.bookName.setTextColor(getColor(R.color.red))
            bin.authorName.setTextColor(getColor(R.color.red))
        }

    }


}
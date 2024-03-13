package com.book.bookapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.book.bookapp.databinding.BookItemListBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

@Suppress("DEPRECATION")
class BookAdapter(
    var context: Context,
    var arrayList: ArrayList<Book>,
) :
    RecyclerView.Adapter<BookAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.book_item_list, parent, false)
        return VH(view)

    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        holder.binding.bookName.text = arrayList[position].bookName
        holder.binding.authorName.text = context.getString(R.string.author)+": "+arrayList[position].authorName
        Glide.with(context).load(arrayList[position].imageUrl).into(holder.binding.image)
        
        if (arrayList[position].readBy.contains(FirebaseAuth.getInstance().currentUser?.uid)){
            holder.binding.bookName.setTextColor(context.getColor(R.color.green))
            holder.binding.authorName.setTextColor(context.getColor(R.color.green))
        }else{
            holder.binding.bookName.setTextColor(context.getColor(R.color.red))
            holder.binding.authorName.setTextColor(context.getColor(R.color.red))
        }

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, BookActivity::class.java)
                .putExtra("data", Gson().toJson(arrayList[position]))
            )
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var binding: BookItemListBinding = BookItemListBinding.bind(itemView)

    }


}
package com.book.bookapp

data class Book (
    var id: String = "",
    var bookName: String = "",
    var authorName: String = "",
    var publicationYear: String = "",
    var imageUrl: String = "",
    var bookDesc: String = "",
    var readBy: ArrayList<String> = ArrayList(),
)
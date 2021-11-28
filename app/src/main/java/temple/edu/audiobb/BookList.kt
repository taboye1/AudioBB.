package temple.edu.audiobb

import java.io.Serializable

class BookList : Serializable{
    private var myBooksFor = mutableListOf<Book>()
    companion object {
        val BOOKLIST_KEY = "bookList"
    }
    fun add (_book: Book){
        myBooksFor.add(_book)
    }

    fun remove(_book: Book){
        myBooksFor.remove(_book)
    }
    fun addBooks (newBookList: BookList){
        myBooksFor.clear()
        myBooksFor.addAll(newBookList.myBooksFor)
    }

    fun get(index: Int) : Book{
        return myBooksFor[index]
    }

    fun size() : Int {
        return myBooksFor.size
    }
}
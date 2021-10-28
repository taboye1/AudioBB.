package temple.edu.audiobb

import java.io.Serializable

class BookList : Serializable{
    private var myBooksFor = mutableListOf<Book>()

    fun add (_book: Book){
        myBooksFor.add(_book)
    }

    fun remove(_book: Book){
        myBooksFor.remove(_book)
    }

    fun get(index: Int) : Book{
        return myBooksFor[index]
    }

    fun size() : Int {
        return myBooksFor.size
    }
}
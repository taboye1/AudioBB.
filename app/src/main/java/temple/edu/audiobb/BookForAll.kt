package temple.edu.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookForAll : ViewModel() {
    private val myBook : MutableLiveData<Book> by lazy {
        MutableLiveData<Book>()
    }
    private var empty: Boolean = true

    fun getBook() : LiveData<Book> {
        return myBook
    }

    fun setBook(book: Book) {
        this.myBook.value = book
        empty = false
    }

    fun isEmpty() : Boolean {
        return empty
    }
}

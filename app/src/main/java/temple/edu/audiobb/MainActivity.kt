package temple.edu.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), BookListFragment.MyInterface  {
    private var twoPane : Boolean = false
    lateinit var bookView: BookForAll
    private var bList = BookList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        twoPane = findViewById<View>(R.id.container2) != null
        bookView = ViewModelProvider(this).get(BookForAll::class.java)

        bList.add(Book(0, "", "", ""))
        //val booksList = BookList()
        //showedBooks(booksList)

        val bookListFragment = BookListFragment.newInstance(bList)

        if (supportFragmentManager.findFragmentById(R.id.container1) is BookDetailsFragment
            && twoPane) {
            supportFragmentManager.popBackStack()
        }
        if (supportFragmentManager.findFragmentById(R.id.container2) is BookDetailsFragment
            && !twoPane) {
            if (bookView.getBook().value?.id != -1
                && !bookView.isEmpty()) {
                bookSelected()
            }
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, bookListFragment)
                .commit()
        }
        if(twoPane){
            supportFragmentManager.beginTransaction()
                .replace(R.id.container2, BookDetailsFragment())
                .addToBackStack(null)
                .commit()
        }else if(twoPane){
            supportFragmentManager.beginTransaction()
                .replace(R.id.container2, BookDetailsFragment())
                .commit()
        }
    }
    //private fun showedBooks(bList: BookList) {
        //val titles = resources.getStringArray(R.array.BookTitles)
        //val authors = resources.getStringArray(R.array.BookAuthors)
        //for (i in titles.indices) {
            //val _book = Book(titles[i], authors[i])
            //bList.add(_book)
       // }
   // }
    override fun bookSelected() {
        if (!twoPane) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, BookDetailsFragment())
                .addToBackStack(null)
                .commit()
        }
        else{
            if(supportFragmentManager.findFragmentById(R.id.container2) == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container2, BookDetailsFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        //ViewModelProvider(this).get(BookForAll::class.java).setBook(Book("", ""))
        bookView.setBook(Book(-1,"","",""))
    }
}

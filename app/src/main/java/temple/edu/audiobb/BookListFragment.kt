package temple.edu.audiobb

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "edu.temple.audiobb.BookListFragment.BOOK_LIST"
private const val BOOK_LIST = "Book for All"

class BookListFragment : Fragment() {

    private val bookList: BookList by lazy {
      ViewModelProvider(requireActivity()).get(BookList::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookForAllModel = ViewModelProvider(requireActivity()).get(SelectedBookForAll::class.java)

        val onClick : (Book) -> Unit = {
                book: Book -> bookForAllModel.setSelectedBook(book)
            (activity as MyInterface).bookSelected(book)
        }
        with (view as RecyclerView) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = BooksAdapter (bookList, onClick)
        }
    }

    fun bookListUpdate() {
        view?.apply {
           (this as RecyclerView).adapter?.notifyDataSetChanged()
       }
    }
    companion object {
        @JvmStatic
        fun newInstance(bookList: BookList) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BOOK_LIST, bookList)
                }
            }
    }

    interface MyInterface {
        fun bookSelected(book:Book)
    }

    }

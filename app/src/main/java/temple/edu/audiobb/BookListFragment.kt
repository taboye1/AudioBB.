package temple.edu.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "Book for All"

class BookListFragment : Fragment() { private lateinit var recyclerView: RecyclerView
    private lateinit var bList: BookList
    private lateinit var bModel: BookForAll


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bList = it.getSerializable(ARG_PARAM1) as BookList
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_book_list, container, false)

        bModel = ViewModelProvider(requireActivity()).get(BookForAll::class.java)

        recyclerView = layout.findViewById(R.id.bookListRView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = BooksAdapter(bList) { position ->
            onClick(position)
        }
        recyclerView.adapter = adapter

        return layout
    }

    private fun onClick(position: Int) {
        (activity as MyInterface).bookSelected()
        bModel.setBook(bList.get(position))
    }
    companion object {
        @JvmStatic
        fun newInstance(bookList: BookList) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, bookList)
                }
            }
    }

    interface MyInterface {
        fun bookSelected()
    }
}
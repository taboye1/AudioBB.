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

private const val ARG_PARAM1 = "Book for All"
private const val BOOK_LIST = "edu.temple.audiobb.BookListFragment.BOOK_LIST"

class BookListFragment : Fragment() { private lateinit var recyclerView: RecyclerView
    private lateinit var bList: BookList
    private lateinit var bModel: BookForAll
    private lateinit var adapter: BooksAdapter

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == AppCompatActivity.RESULT_OK) {
            bList = it.data?.getSerializableExtra(RESULTS) as BookList
            adapter.updateList(bList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bList = if (savedInstanceState == null) {
                it.getSerializable(ARG_PARAM1) as BookList
            } else {
                savedInstanceState.getSerializable(BOOK_LIST) as BookList
            }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_book_list, container, false)

        val launchSearchButton = layout.findViewById<Button>(R.id.launchSButton)
        launchSearchButton.setOnClickListener {
            val intent = Intent(requireContext(), BookSearchActivity::class.java)
            launcher.launch(intent)
        }
        bModel = ViewModelProvider(requireActivity()).get(BookForAll::class.java)

        recyclerView = layout.findViewById(R.id.bookListRView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = BooksAdapter(bList) { position ->
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
        fun newInstance(bList: BookList) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, bList)
                }
            }
    }

    interface MyInterface {
        fun bookSelected()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(BOOK_LIST, bList)
    }
}
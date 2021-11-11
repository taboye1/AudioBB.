package temple.edu.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import androidx.lifecycle.ViewModelProvider

class BookDetailsFragment : Fragment() {

    lateinit var titleTextView : TextView
    lateinit var authorTextView : TextView
    private lateinit var bookCover : ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_details, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleTextView = view.findViewById(R.id.titleTextView)
        authorTextView =  view.findViewById(R.id.authorTextView)
        bookCover = view.findViewById(R.id.coverImageView)
        ViewModelProvider(requireActivity()).get(BookForAll::class.java).getBook()
            .observe(requireActivity()) { reviewBDetails(it)
            }
    }
    private fun reviewBDetails(_book: Book) {
        titleTextView.text = _book.title
        authorTextView.text = _book.author
        Picasso.get().load(_book.coverUrl).into(bookCover)
        //view?.findViewById<TextView>(R.id.titleTextView)?.text = _book.title
        //view?.findViewById<TextView>(R.id.authorTextView)?.text = _book.author
    }

}
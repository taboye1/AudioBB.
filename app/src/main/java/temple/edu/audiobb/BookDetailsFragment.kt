package temple.edu.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

class BookDetailsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_details, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewModelProvider(requireActivity()).get(BookForAll::class.java).getBook()
            .observe(requireActivity()) { reviewBDetails(it)
            }
    }

    private fun reviewBDetails(_book: Book) {
        view?.findViewById<TextView>(R.id.titleTextView)?.text = _book.title
        view?.findViewById<TextView>(R.id.authorTextView)?.text = _book.author
    }

}
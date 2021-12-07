package temple.edu.audiobb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.net.Uri
import com.squareup.picasso.Picasso
import java.net.URL
import androidx.recyclerview.widget.RecyclerView

class BooksAdapter(bList: BookList, onClick : (Book) -> Unit) : RecyclerView.Adapter<BooksAdapter.ViewHolder>() {
    val bookList = bList
    val _onClick = onClick
    class ViewHolder(itemView: View, val _onClick: (Book) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleRView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorRView)
        val idTextView: TextView = itemView.findViewById(R.id.idRView)
        lateinit var book: Book
        //var currentBook: Book? = null

        init {
            itemView.setOnClickListener {
                _onClick(book)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_adapter, parent, false)
        return ViewHolder(view, _onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = bookList.get(position).title
        holder.authorTextView.text = bookList.get(position).author
        holder.idTextView.text = bookList.get(position).id.toString()
        }

    override fun getItemCount(): Int {
        return bookList.size()!!
    }

    }


package temple.edu.audiobb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BooksAdapter(private val bList: BookList, private val onClick : (position: Int) -> Unit) : RecyclerView.Adapter<BooksAdapter.ViewHolder>() {

    class ViewHolder(itemView: View, val onClick : (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.titleRView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorRView)

        init {
            itemView.setOnClickListener {
                onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_adapter, parent, false)
        return ViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleTextView.text = bList.get(position).title
        holder.authorTextView.text = bList.get(position).author
    }

    override fun getItemCount() = bList.size()

}

package temple.edu.audiobb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException


class BookSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)


       findViewById<ImageButton>(R.id.search_button).setOnClickListener{

           val coverUrl = "https://kamorris.com/lab/cis3515/search.php?term="
              findViewById<EditText>(R.id.searchEditText).text.toString()
           Volley.newRequestQueue(this).add(
               JsonArrayRequest(Request.Method.GET,coverUrl,null,{
                   setResult(RESULT_OK,Intent().putExtra(BookList.BOOKLIST_KEY,BookList().apply { populateBooks(it) })
                   )
                   finish()
               },{}))
       }
    }
}






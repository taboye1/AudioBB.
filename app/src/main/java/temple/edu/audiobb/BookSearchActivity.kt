package temple.edu.audiobb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

const val RESULTS = "edu.temple.audiobb.BookSearchActivity.RESULTS"

class BookSearchActivity : AppCompatActivity() {
    private companion object {
        private const val Url = "https://kamorris.com/lab/cis3515/search.php?term="
    }

    private val queue by lazy {
        Volley.newRequestQueue(this)
    }
    private val bList by lazy {
        BookList()
    }
    lateinit var searchTools: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)
        searchTools = findViewById(R.id.searchEditText)
        val button = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)
        button.setOnClickListener {
            startSearch()
            queue.addRequestFinishedListener<JsonArrayRequest> {
                if (bList.size() == 0) { 
                    bList.add(Book(0, getString(R.string.can_not_found), "", "",0))
                    setResult(RESULT_OK, Intent().putExtra(RESULTS, bList))
                } else
                    setResult(RESULT_OK, Intent().putExtra(RESULTS, bList))
                    finish() }
            }
            button2.setOnClickListener {
                finish()
            }
        }
        private fun startSearch() {
            val url = Url + searchTools.text

            val arrayRequest = JsonArrayRequest(Request.Method.GET, url, null,{
                    try {
                        for (i in 0 until it.length()) {
                            val bookData = it.getJSONObject(i)
                            val id = bookData.getString("id").toInt()
                            val title = bookData.getString("title")
                            val author = bookData.getString("author")
                            val coverURL = bookData.getString("cover_url")
                            val duration = bookData.getString("duration").toInt()
                            val book = Book(id, title, author, coverURL,duration)
                            bList.add(book) }
                    } catch (e: JSONException) {
                    } },
                {
                    Toast.makeText(this, "Retrieving errors!", Toast.LENGTH_SHORT).show() })
            queue.add(arrayRequest)
        }
        override fun onStop() {
            super.onStop()
            queue.cancelAll(this)
        }
    }






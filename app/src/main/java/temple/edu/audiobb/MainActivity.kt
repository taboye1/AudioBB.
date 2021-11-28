package temple.edu.audiobb

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import edu.temple.audlibplayer.PlayerService

class MainActivity : AppCompatActivity(), BookListFragment.MyInterface  {
    private lateinit var startForResult: ActivityResultLauncher<Intent>
    private lateinit var controlBinder: PlayerService.MediaControlBinder
    private var twoPane : Boolean = false
    var connection = false
    lateinit var bookView: BookForAll
    private var bList = BookList()

    private val isSingleContainer : Boolean by lazy{
        findViewById<View>(R.id.container2) == null
    }
    private val bModel : BookList by lazy{
        ViewModelProvider(this).get(BookList::class.java)
    }
    private val selectedBookView : BookForAll by lazy{
        ViewModelProvider(this).get(BookForAll::class.java)
    }
    val durationBarHandler = Handler(Looper.getMainLooper()){
        if (it.obj != null){
            val audioDurationObj = it.obj as PlayerService.BookProgress
            val durationTime = audioDurationObj.progress
            //val duration = selectedBookView.getBook().value?.duration

            var nowPlayingTView = findViewById<TextView>(R.id.durationText)
            nowPlayingTView.text = durationTime.toString()

            var seekBar = findViewById<SeekBar>(R.id.durationBar)
            seekBar.progress = durationTime
        }
        true
    }
    val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            connection = true
            controlBinder = service as PlayerService.MediaControlBinder
            controlBinder.setProgressHandler(durationBarHandler)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            connection = false
        }
    }
    private val searchRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        supportFragmentManager.popBackStack()
        it.data?.run{
            bModel.addBooks(getSerializableExtra(BookList.BOOKLIST_KEY) as BookList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        twoPane = findViewById<View>(R.id.container2) != null
        bookView = ViewModelProvider(this).get(BookForAll::class.java)

        bList.add(Book(0, "", "", "", 0))
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
        bookView.setBook(Book(-1,"","","", 0))
    }
    fun selectionMade(book: Book) {
        if (isSingleContainer) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, BookDetailsFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
    }

    fun Search(){
        startForResult.launch(Intent(this, BookSearchActivity::class.java))
    }

    fun play(durationTime: Int) {
        val selectedBook = selectedBookView.getBook().value
        if(selectedBook != null){
            if (durationTime > 0){
                controlBinder.seekTo(durationTime)
                controlBinder.pause()
            } else {
                controlBinder.play(selectedBook.id)
            }
        }
    }

    fun pause() {
        controlBinder.pause()
    }

    fun stop() {
        controlBinder.stop()
    }
}


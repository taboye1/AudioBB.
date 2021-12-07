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
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import edu.temple.audlibplayer.PlayerService

class MainActivity : AppCompatActivity(), BookListFragment.MyInterface,ControlFragment.ControlInterface  {
    private lateinit var bookListFragment :BookListFragment
    var connection = false
    lateinit var controlBinder: PlayerService.MediaControlBinder


    val progressHandler = Handler(Looper.getMainLooper()){
        if (it.obj != null){
            val audioDurationObj = it.obj as PlayerService.BookProgress
            val durationTime = audioDurationObj.progress
            //val duration = selectedBookView.getBook().value?.duration

            var nowPlayingTView = findViewById<TextView>(R.id.durationText)
            var seekBar = findViewById<SeekBar>(R.id.durationBar)
            nowPlayingTView.text = durationTime.toString()
            seekBar.progress = durationTime
        }
        true
    }
    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            connection = true
            controlBinder = service as PlayerService.MediaControlBinder
            controlBinder.setProgressHandler(progressHandler)
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            connection = false
        }
    }
    private val searchRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        supportFragmentManager.popBackStack()
        it.data?.run{
            bookForAllModel.copyBooks(getSerializableExtra(BookList.BOOKLIST_KEY)as BookList)
            bookListFragment.bookListUpdate()
        }
    }
    private val isSingleContainer : Boolean by lazy{
        findViewById<View>(R.id.container2) == null
    }

    private val selectedBookForAll : SelectedBookForAll by lazy {
        ViewModelProvider(this).get(SelectedBookForAll::class.java)
    }

    private val bookForAllModel : BookList by lazy {
        ViewModelProvider(this).get(BookList::class.java)
    }
    companion object {
        const val BOOK_FRAGMT_KEY = "BListFragment"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.taskView,ControlFragment()).commit()
        bindService(Intent(this, PlayerService::class.java), serviceConnection, BIND_AUTO_CREATE)
        //val bookListFragment = BookListFragment.newInstance(bList)

        if (supportFragmentManager.findFragmentById(R.id.container1) is BookDetailsFragment
            && selectedBookForAll.getSelectedBook().value !=null) {
            supportFragmentManager.popBackStack()
        }

        if (savedInstanceState == null) {
            bookListFragment = BookListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.container1, bookListFragment,BOOK_FRAGMT_KEY)
                .commit()

        }else {
            bookListFragment = supportFragmentManager.findFragmentByTag(BOOK_FRAGMT_KEY)as BookListFragment
            if (isSingleContainer && selectedBookForAll.getSelectedBook().value != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, BookDetailsFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
    }
        if (!isSingleContainer && supportFragmentManager.findFragmentById(R.id.container2) !is BookDetailsFragment)
            supportFragmentManager.beginTransaction()
                .add(R.id.container2, BookDetailsFragment())
                .commit()

        findViewById<ImageButton>(R.id.searchButton).setOnClickListener{
            searchRequest.launch(Intent(this,BookSearchActivity::class.java))
        }
    }
    override fun onBackPressed() {
        selectedBookForAll.setSelectedBook(null)
        super.onBackPressed()
        //ViewModelProvider(this).get(BookForAll::class.java).setBook(Book("", ""))
        //bookView.setBook(Book(-1,"","","", 0))
    }
    override fun bookSelected(book: Book) {
        if (isSingleContainer) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, BookDetailsFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    override fun playClick(durationTime: Int) {
        val currentBook = selectedBookForAll.getSelectedBook().value
        if(currentBook != null){
            if (durationTime > 0){
                controlBinder.seekTo(durationTime)
            } else {
                controlBinder.play(currentBook.id)
            }
        }
    }

    override fun pauseClick() {
        controlBinder.pause()
    }

    override fun stopClick() {
        controlBinder.stop()
    }

    override fun seekClick(){

    }
}




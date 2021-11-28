package temple.edu.audiobb

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import java.lang.RuntimeException


class ControlFragment : Fragment() {
    private lateinit var play: Button
    private lateinit var stop: Button
    private lateinit var pause: Button
    private lateinit var seekBar: SeekBar
    private lateinit var nowPlayingTView: TextView
    private lateinit var titleText: TextView
    var initialTime: Int = 0

    //fun ControlFragment(){

   // }
   override fun onCreateView(
       inflater: LayoutInflater, container: ViewGroup?,
       savedInstanceState: Bundle?
   ): View? {
       val l: View = inflater.inflate(R.layout.fragment_control, container, false)
       play = l.findViewById(R.id.playBtn)
       pause = l.findViewById(R.id.pauseBtn)
       stop = l.findViewById(R.id.stopBtn)
       titleText = l.findViewById(R.id.titleText)
       seekBar = l.findViewById(R.id.durationBar)
       nowPlayingTView = l.findViewById(R.id.durationText)

       return l
   }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookViewModel = ViewModelProvider(requireActivity()).get(BookForAll::class.java)
        play.setOnClickListener {
            val selectedBook = bookViewModel.getBook().value
            if(selectedBook != null){
                titleText.text = " Play Now -- " + selectedBook.title
                seekBar.max = selectedBook.duration
            }
            (activity as ControlInterface).playClick(initialTime)
        }
        stop.setOnClickListener {
            initialTime = 0
            seekBar.progress = 0
            (activity as ControlInterface).stopClick()
        }

        pause.setOnClickListener {
            val selectedBook = bookViewModel.getBook().value
            if (selectedBook != null){
                initialTime = seekBar.progress
            }
            (activity as ControlInterface).pauseClick()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(sBar: SeekBar, progress: Int, parent: Boolean) {
                val selectedBook = bookViewModel.getBook().value
                if(selectedBook != null){
                    nowPlayingTView.text = progress.toString()
                }
            }

            override fun onStartTrackingTouch(sBar: SeekBar?) {
                (activity as ControlInterface).pauseClick()
            }

            override fun onStopTrackingTouch(sBar: SeekBar?) {
                val selectedBook = bookViewModel.getBook().value
                if(selectedBook != null){
                    initialTime = seekBar.progress
                    nowPlayingTView.text = initialTime.toString()
                    (activity as ControlInterface).playClick(initialTime)
                }
            }
        })
    }

    interface ControlInterface{
        fun playClick(durationTime: Int)
        fun pauseClick()
        fun stopClick()
        fun changePosition(progress: Int)
    }

}





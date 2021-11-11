package temple.edu.audiobb

import java.io.Serializable

data class Book (
    val id: Int,
    val title: String,
    val author: String,
    val coverUrl: String):Serializable{
}
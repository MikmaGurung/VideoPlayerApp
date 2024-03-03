package com.example.khatravideoplayaer.Data

import android.net.Uri
import androidx.media3.common.MediaItem

data class VideoItem(
    val name : String,
    val absolutePath : String,
    val id : Long,
    val uri : Uri,
    val size : Long,
    val height : Int,
    val width : Int,
    val duration : Long,
    val dateModified : Long,
)
data class FolderItem(
    val name : String,
    val videoItems : List<VideoItem>
)

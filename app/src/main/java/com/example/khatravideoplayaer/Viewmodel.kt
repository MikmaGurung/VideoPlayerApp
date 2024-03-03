package com.example.khatravideoplayaer

import android.content.pm.ActivityInfo
import android.media.audiofx.LoudnessEnhancer
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.khatravideoplayaer.Data.MetadataReader
import com.example.khatravideoplayaer.Data.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.media3.session.MediaSession
import kotlinx.coroutines.flow.update

@HiltViewModel
@UnstableApi class Viewmodel @Inject constructor(
    private val metadataReader: MetadataReader,
    val player : ExoPlayer,
    private val mediaSession: MediaSession,
    private val listener : Player.Listener,
    private val loudnessEnhancer: LoudnessEnhancer
): ViewModel()  {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState:StateFlow<PlayerState> = _playerState

    init {
        player.prepare()
    }

    override fun onCleared(){
        player.release()
        mediaSession.release()
        player.removeListener(listener)
        loudnessEnhancer.release()
        super.onCleared()
    }

    private fun updateCurrentVideoItem(videoItem: VideoItem?){
        _playerState.update {
            it.copy(
                currentVideoItem = videoItem
            )
        }
        setMediaItem(_playerState.value.currentVideoItem!!.uri)
    }

    private fun setMediaItem(uri : Uri){
        player.apply {
            addMediaItem(MediaItem.fromUri(uri))
            playWhenReady = true
            if (isPlaying){
                _playerState.update {
                    it.copy(isPlaying = true)
                }
            }
        }
    }

    fun onPlayPauseClick(){
        if(player.isPlaying){
            player.pause().also {
                _playerState.update {
                    it.copy(isPlaying = false)
                }
            }
        }

        else{
            player.play().also {
                _playerState.update {
                    it.copy(isPlaying = true)
                }
            }
        }
    }

    fun playPauseOnActivityLifeCycleEvents(shouldPause : Boolean){
        if (player.isPlaying && shouldPause){
            player.pause().also {
                _playerState.update { it.copy(isPlaying = false) }
            }
        }else if(!player.isPlaying && !shouldPause){
            player.play().also {
                _playerState.update { it.copy(isPlaying = true) }
            }
        }
    }

    fun onIntent(uri: Uri){
        metadataReader.getMetadataFromUri(uri)?.let{
            updateCurrentVideoItem(it)
        }
    }

    fun onNewIntent(uri:Uri){
        player.clearMediaItems()
        metadataReader.getMetadataFromUri(uri)?.let{
            updateCurrentVideoItem(it)
        }
    }
}
@UnstableApi
data class PlayerState(
    val isPlaying: Boolean = false,
    val currentVideoItem: VideoItem? = null,
    val resizeMode: Int = AspectRatioFrameLayout.RESIZE_MODE_FIT,
    val orientation: Int = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
)
package com.example.khatravideoplayaer.di

import android.app.Application
import android.media.audiofx.LoudnessEnhancer
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.example.khatravideoplayaer.Data.MetadataReader
import com.example.khatravideoplayaer.Data.MetadataReaderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
object VideoPlayerModule  {

    @Provides
    @ViewModelScoped
    fun provideExoPlayer(app : Application, audioAttributes: AudioAttributes) :ExoPlayer{
        return ExoPlayer
            .Builder(app)
            .setAudioAttributes(audioAttributes,true)
            .build()
    }

    @Provides
    @ViewModelScoped
    fun  provideLoudnessEnhancer(player: ExoPlayer) :LoudnessEnhancer{
       val loudnessEnhancer = LoudnessEnhancer(player.audioSessionId)
        player.addListener(object : Player.Listener{
            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                loudnessEnhancer.release()

                try {
                    loudnessEnhancer.setTargetGain(0)
                    loudnessEnhancer.setEnabled(true)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        })
        return loudnessEnhancer
    }

    @Provides
    @ViewModelScoped
    fun provideMetaDataReader(app: Application): MetadataReader{
        return MetadataReaderImpl(app)
    }

    @Provides
    @ViewModelScoped
    fun provideListener(player: ExoPlayer,loudnessEnhancer: LoudnessEnhancer) : Player.Listener {
        return object :Player.Listener{
            override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
                super.onDeviceVolumeChanged(volume, muted)
                player.volume = volume.toFloat()
            }

            override fun onAudioSessionIdChanged(audioSessionId: Int) {
                super.onAudioSessionIdChanged(audioSessionId)
                loudnessEnhancer.release()
                try{
                    loudnessEnhancer.setTargetGain(0)
                    loudnessEnhancer.setEnabled(true)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

        }
    }

    @Provides
    @ViewModelScoped
    fun provideAudioAttributes() : AudioAttributes{
        return AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provideMediaSession(app:Application,player: ExoPlayer): MediaSession{
        return MediaSession.Builder(app,player)
            .build()
    }
}
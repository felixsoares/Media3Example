package com.felix.media3example

import android.content.ComponentName
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaController
import androidx.media3.session.MediaNotification
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerNotificationManager
import androidx.media3.ui.PlayerView
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class MainActivity : AppCompatActivity() {

    private var mediaControllerFuture: MediaController ?= null

    @UnstableApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val exoPlayer = ExoPlayer.Builder(this).build()
        val mediaItem =
            MediaItem.Builder()
                .setMediaId("media-1")
                .setUri("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtist("David Bowie")
                        .setTitle("Heroes")
                        .build()
                )
                .build()

        findViewById<PlayerView>(R.id.player_view).player = exoPlayer

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()

        val mediaSession = MediaSessionCompat(this, "MinhaSessaoDeMidia")

//        val mediaSession = MediaSession.Builder(this, exoPlayer).build()
        val not = PlayerNotificationManager.Builder(
            this,
            1,
            "channel_id",
        )
        not.build().apply {
            setPlayer(exoPlayer)
            setMediaSessionToken(mediaSession.sessionToken)
        }


//        if(mediaControllerFuture == null) {
//            val context = this
//            val sessionToken =
//                SessionToken(context, ComponentName(context, PlayerService::class.java))
//            val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
//            controllerFuture.addListener({
//                mediaControllerFuture = controllerFuture.get()
//                findViewById<PlayerView>(R.id.player_view).player = mediaControllerFuture
//
//                mediaControllerFuture?.addListener(object : Player.Listener {
//                    override fun onIsPlayingChanged(isPlaying: Boolean) {
//                        super.onIsPlayingChanged(isPlaying)
//                    }
//
//                    override fun onPlayerError(error: PlaybackException) {
//                        super.onPlayerError(error)
//                    }
//                })
//
//                setupPlayer()
//            }, MoreExecutors.directExecutor())
//        }

        findViewById<Button>(R.id.button_play).setOnClickListener {
            mediaControllerFuture?.play()
        }

        findViewById<Button>(R.id.button_pause).setOnClickListener {
            mediaControllerFuture?.pause()
        }
    }

    private fun setupPlayer(){
        val mediaItem =
            MediaItem.Builder()
                .setMediaId("media-1")
                .setUri("https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8")
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtist("David Bowie")
                        .setTitle("Heroes")
                        .build()
                )
                .build()

        mediaControllerFuture?.setMediaItem(mediaItem)
        mediaControllerFuture?.prepare()
        mediaControllerFuture?.play()
    }

    override fun onStop() {
        super.onStop()
        mediaControllerFuture?.release()
    }
}
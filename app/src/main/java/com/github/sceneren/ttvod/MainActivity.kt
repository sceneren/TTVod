package com.github.sceneren.ttvod

import android.annotation.SuppressLint
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.bytedance.playerkit.player.playback.PlaybackController
import com.github.sceneren.ttvod.ui.theme.TTVodTheme


class MainActivity : ComponentActivity() {

    private lateinit var playbackController: PlaybackController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playbackController = PlaybackController()
        enableEdgeToEdge()
        setContent {
            TTVodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppScreen(modifier = Modifier.padding(innerPadding), playbackController)
                    //TTVodPlayer(modifier = Modifier.padding(innerPadding))
//                    TTVideoView(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(innerPadding),
//                        playbackController = playbackController
//                    )
                }
            }

        }

    }
}
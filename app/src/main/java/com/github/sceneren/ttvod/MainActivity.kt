package com.github.sceneren.ttvod

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
                }
            }

        }

    }
}
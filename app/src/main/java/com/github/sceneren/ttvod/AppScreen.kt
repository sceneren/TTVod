package com.github.sceneren.ttvod

import android.annotation.SuppressLint
import android.graphics.SurfaceTexture
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.widget.FrameLayout
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.bytedance.playerkit.player.PlayerEvent
import com.bytedance.playerkit.player.event.InfoProgressUpdate
import com.bytedance.playerkit.player.event.InfoSubtitleTextUpdate
import com.bytedance.playerkit.player.playback.DisplayModeHelper
import com.bytedance.playerkit.player.playback.DisplayView
import com.bytedance.playerkit.player.playback.PlaybackController
import com.bytedance.playerkit.player.playback.VideoView
import com.bytedance.playerkit.player.source.MediaSource
import com.bytedance.playerkit.player.volcengine.VolcConfig
import com.bytedance.playerkit.player.volcengine.VolcDebugTools
import com.bytedance.playerkit.player.volcengine.VolcScene
import com.bytedance.playerkit.utils.event.Dispatcher
import com.ss.ttvideoengine.Resolution
import com.ss.ttvideoengine.SubInfoSimpleCallBack
import com.ss.ttvideoengine.TTVideoEngine
import com.ss.ttvideoengine.TTVideoEngineInterface.PLAYER_OPTION_ENABLE_OPEN_SUB
import com.ss.ttvideoengine.TTVideoEngineInterface.PLAYER_OPTION_ENABLE_OPEN_SUB_THREAD
import com.ss.ttvideoengine.TTVideoEngineInterface.PLAYER_OPTION_ENABLE_OPT_SUB_LOAD_TIME
import com.ss.ttvideoengine.TTVideoEngineInterface.PLAYER_OPTION_SUB_ENABLE_MDL
import com.ss.ttvideoengine.TTVideoEngineInterface.PLAYER_OPTION_SUB_IDS
import com.ss.ttvideoengine.TTVideoEngineInterface.PLAYER_OPTION_SWITCH_SUB_ID
import com.ss.ttvideoengine.TTVideoEngineInterface.PLAYER_OPTION_USE_VIDEOMODEL_CACHE
import com.ss.ttvideoengine.VideoEngineCallback
import com.ss.ttvideoengine.debugtool2.DebugTool
import com.ss.ttvideoengine.model.SubInfo
import com.ss.ttvideoengine.source.VidPlayAuthTokenSource
import com.ss.ttvideoengine.strategy.source.StrategySource
import com.ss.ttvideoengine.utils.Error
import org.json.JSONObject

const val VID = "v02d9bg10064d19n6kaljhtce8tgivj0"

const val PLAY_AUTH_TOKEN =
    "eyJHZXRQbGF5SW5mb1Rva2VuIjoiVmlkPXYwMmQ5YmcxMDA2NGQxOW42a2Fsamh0Y2U4dGdpdmowJlgtRXhwaXJlcz0zNjAwMCZBY3Rpb249R2V0UGxheUluZm8mVmVyc2lvbj0yMDIwLTA4LTAxJlgtRGF0ZT0yMDI1MDcxMVQwMjMwMTVaJlgtTm90U2lnbkJvZHk9JlgtQ3JlZGVudGlhbD1BS0xUT1dOaE5EWTVNalEwTWpGbE5HVmhNbUl6WkdZelpUYzFNRE5qWXpReU56SSUyRjIwMjUwNzExJTJGY24tbm9ydGgtMSUyRnZvZCUyRnJlcXVlc3QmWC1BbGdvcml0aG09SE1BQy1TSEEyNTYmWC1TaWduZWRIZWFkZXJzPSZYLVNpZ25lZFF1ZXJpZXM9QWN0aW9uJTNCVmVyc2lvbiUzQlZpZCUzQlgtQWxnb3JpdGhtJTNCWC1DcmVkZW50aWFsJTNCWC1EYXRlJTNCWC1FeHBpcmVzJTNCWC1Ob3RTaWduQm9keSUzQlgtU2lnbmVkSGVhZGVycyUzQlgtU2lnbmVkUXVlcmllcyZYLVNpZ25hdHVyZT0xZGFmOGVlZWFiMWU5MGMzNTY3ZTRhMDk1YjFhMzcxODRmYzJjNjIzNWY2YmJlOGQyY2Q0ZDRlNGE4ODRlZTc4IiwiVG9rZW5WZXJzaW9uIjoiVjIifQ=="

const val SUBTITLE_AUTH_TOKEN =
    "eyJHZXRTdWJ0aXRsZUF1dGhUb2tlbiI6IlZpZD12MDJkOWJnMTAwNjRkMTluNmthbGpodGNlOHRnaXZqMCZTdGF0dXM9UHVibGlzaGVkJlgtRXhwaXJlcz0zNjAwMCZBY3Rpb249R2V0U3VidGl0bGVJbmZvTGlzdCZWZXJzaW9uPTIwMjAtMDgtMDEmWC1EYXRlPTIwMjUwNzExVDAyMzAxNVomWC1Ob3RTaWduQm9keT0mWC1DcmVkZW50aWFsPUFLTFRPV05oTkRZNU1qUTBNakZsTkdWaE1tSXpaR1l6WlRjMU1ETmpZelF5TnpJJTJGMjAyNTA3MTElMkZjbi1ub3J0aC0xJTJGdm9kJTJGcmVxdWVzdCZYLUFsZ29yaXRobT1ITUFDLVNIQTI1NiZYLVNpZ25lZEhlYWRlcnM9JlgtU2lnbmVkUXVlcmllcz1BY3Rpb24lM0JTdGF0dXMlM0JWZXJzaW9uJTNCVmlkJTNCWC1BbGdvcml0aG0lM0JYLUNyZWRlbnRpYWwlM0JYLURhdGUlM0JYLUV4cGlyZXMlM0JYLU5vdFNpZ25Cb2R5JTNCWC1TaWduZWRIZWFkZXJzJTNCWC1TaWduZWRRdWVyaWVzJlgtU2lnbmF0dXJlPTdlOGNlODY1MWI2MzkzNjFkYjc5MDczYWZhZTA2Njg4MDM1Mzc4YTJlY2VjNTM0ZGRkNzc0ODlkNThkYThjZGMifQ=="

@Composable
fun AppScreen(modifier: Modifier, playbackController: PlaybackController) {
    // TTVideoView(modifier = modifier, playbackController = playbackController)
    Box(modifier = modifier) {

        VideoList(playbackController = playbackController)
    }
}


@SuppressLint("MutableCollectionMutableState")
@Composable
fun TTVodPlayer(modifier: Modifier) {
    val context = LocalContext.current
    var subtitleText by remember {
        mutableStateOf("")
    }
    var subInfoList: MutableList<SubInfo>? by remember {
        mutableStateOf(null)
    }

    var playState by remember {
        mutableIntStateOf(0)
    }


    val ttVideoEngine = remember {
        TTVideoEngine(context, TTVideoEngine.PLAYER_TYPE_OWN).apply {
            // 开启缓存 video model
            setIntOption(PLAYER_OPTION_USE_VIDEOMODEL_CACHE, 1)
            //外挂字幕功能总开关，play 前调用
            setIntOption(PLAYER_OPTION_ENABLE_OPEN_SUB_THREAD, 1)
            //外挂字幕加载优化开关
            setIntOption(PLAYER_OPTION_ENABLE_OPT_SUB_LOAD_TIME, 1)
            //使用数据加载模块 MDL 加载外挂字幕，提升加载成功率
            setIntOption(PLAYER_OPTION_SUB_ENABLE_MDL, 1)
            //字幕开关，起播时或者播放过程中控制打开或者关闭字幕
            setIntOption(PLAYER_OPTION_ENABLE_OPEN_SUB, 1)
            this@apply.isLooping = true
            setVideoInfoListener { videoModel ->
                if (videoModel == null) return@setVideoInfoListener false

                // 请求中、英字幕文件的 ID 数组
                val subtitleIds: MutableList<String> = ArrayList<String>()

                // 获取当前所有字幕语言的接口
                subInfoList = this.supportedSubInfoList()




                subInfoList?.let {
                    if (it.isNotEmpty()) {
                        val subIds: String? =
                            it.joinToString(",", transform = { it.getValueInt(0).toString() })
                        Log.e("subIds", "subIds==>$subIds")
                        // 传入字幕语言 ID 列表
                        val subId = it.first().getValueInt(0)
                        Log.e("subId", "subId==>${subId}")
                        //设置字幕源
                        this.setStringOption(PLAYER_OPTION_SUB_IDS, subIds)
                    }
                }


                return@setVideoInfoListener false
            }

            Log.e("setSubInfoCallBack", "setSubInfoCallBack")
            setSubInfoCallBack(object : SubInfoSimpleCallBack() {
                override fun onSubInfoCallback(code: Int, info: String?) {
                    super.onSubInfoCallback(code, info)
                    Log.e("onSubInfoCallback", "code==>${code},info==>${info}")
                    if (info != null) {
                        val jsonObject = JSONObject(info)
                        val subtitle = jsonObject.optString("info")
                        subtitleText = subtitle
                        Log.e("subtitle", subtitle)
                    }

                }

                override fun onSubPathInfo(subPathInfoJson: String?, error: Error?) {
                    super.onSubPathInfo(subPathInfoJson, error)
                    Log.e("subPathInfo", "subPathInfo=$subPathInfoJson,error=${error.toString()}")
                    //subPathInfoJson ?: return
//                    val subId = JSONObject(subPathInfoJson).getJSONArray("list").getJSONObject(0)
//                        .getInt("sub_id")
//                    //选择字幕
//                    this@apply.setIntOption(PLAYER_OPTION_SWITCH_SUB_ID, subId)
                }

                override fun onSubSwitchCompleted(success: Int, subId: Int) {
                    super.onSubSwitchCompleted(success, subId)
                    Log.e(
                        "onSubSwitchCompleted", "onSubSwitchCompleted success=$success,subId=$subId"
                    )
                }

                override fun onSubLoadFinished(success: Int) {
                    super.onSubLoadFinished(success)
                    Log.e("onSubLoadFinished", "onSubLoadFinished=$success")

                }
            })

            setVideoEngineCallback(object : VideoEngineCallback {
                override fun onPlaybackStateChanged(engine: TTVideoEngine?, playbackState: Int) {
                    super.onPlaybackStateChanged(engine, playbackState)
                    when (playbackState) {
                        TTVideoEngine.PLAYBACK_STATE_PLAYING -> {
                            playState = 1
                        }

                        TTVideoEngine.PLAYBACK_STATE_ERROR -> {
                            playState = -1
                        }

                        TTVideoEngine.PLAYBACK_STATE_STOPPED -> {
                            playState = 2
                        }

                        TTVideoEngine.PLAYBACK_STATE_PAUSED -> {
                            playState = 3
                        }
                    }
                }
            })
        }
    }
    val textureView = remember {
        TextureView(context).apply {
            keepScreenOn = true
            this.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    surfaceTexture: SurfaceTexture, width: Int, height: Int
                ) {
                    ttVideoEngine.surface = Surface(surfaceTexture)
                    val videoSource = createVideoSource(
                        vid = VID, playAuthToken = PLAY_AUTH_TOKEN
                    )

                    ttVideoEngine.setSubAuthToken(SUBTITLE_AUTH_TOKEN)

                    ttVideoEngine.strategySource = videoSource

                    //选择字幕
                    //1360716707-日语，1663121109-韩语 1897940745-英语
                    ttVideoEngine.setIntOption(PLAYER_OPTION_SWITCH_SUB_ID, 1897940745)
                    Log.e("ttVideoEngine", "ttVideoEngine===Play")
                    ttVideoEngine.play()
                }

                override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                    return true
                }

                override fun onSurfaceTextureSizeChanged(
                    surfaceTexture: SurfaceTexture, width: Int, height: Int
                ) {
                }

                override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
                }
            }

        }
    }


    val debugLayer = remember {
        FrameLayout(context).apply {
            DebugTool.setContainerView(this)
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    if (playState == 1) {
                        ttVideoEngine.pause()
                    } else {
                        ttVideoEngine.play()
                    }
                }, factory = { context ->
                textureView
            }, onRelease = {
                ttVideoEngine.releaseAsync()
                DebugTool.release()
            })

        AndroidView(modifier = Modifier.fillMaxSize(), factory = { context ->
            debugLayer
        })

        Text(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = subtitleText,
            color = Color(0xFFFF0000),
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            subInfoList?.let {
                items(it) { subInfo ->
                    Button(onClick = {
                        val subId = subInfo.getValueInt(SubInfo.VALUE_SUB_INFO_ID)
                        ttVideoEngine.setIntOption(PLAYER_OPTION_SWITCH_SUB_ID, subId)
                    }) {
                        Text(
                            text = subInfo.getValueInt(SubInfo.VALUE_SUB_INFO_LANGUAGE_ID)
                                .toLanguage()
                        )
                    }
                }
            }

        }

    }


}

@Preview
@Composable
fun TTVodPlayerPreview() {
    TTVodPlayer(modifier = Modifier.fillMaxSize())
}


internal fun createVideoSource(vid: String, playAuthToken: String): StrategySource {
    return VidPlayAuthTokenSource.Builder().setVid(vid).setPlayAuthToken(playAuthToken)
        .setEncodeType(TTVideoEngine.CODEC_TYPE_H264).setResolution(Resolution.High).build()
}

/**
 * 创建 Vid 播放源
 */

private fun createVidMediaSource(): MediaSource {
    val mediaId = VID
    val playAuthToken = PLAY_AUTH_TOKEN
    return MediaSource.createIdSource(mediaId, playAuthToken).apply {
        subtitleAuthToken = SUBTITLE_AUTH_TOKEN
        syncProgressId = this.mediaId
    }
}


fun Int.toLanguage(): String {
    return when (this) {
        2 -> "英语"
        3 -> "日语"
        4 -> "韩语"
        else -> ""
    }
}

@Composable
fun TTVideoView(modifier: Modifier, playbackController: PlaybackController, isSelected: Boolean) {


    val context = LocalContext.current
    val videoView = remember {
        VideoView(context).apply {
            selectDisplayView(DisplayView.DISPLAY_VIEW_TYPE_TEXTURE_VIEW)
            displayMode = DisplayModeHelper.DISPLAY_MODE_ASPECT_FIT
            playScene = VolcScene.SCENE_SHORT_VIDEO

            val mediaSource = createVidMediaSource()
            val volcConfig = VolcConfig().apply {
                this.enableSubtitle = true
                this.enableSubtitlePreloadStrategy = true
                subtitleLanguageIds = listOf(1, 2, 3)
            }
            VolcConfig.set(mediaSource, volcConfig)

            bindDataSource(mediaSource)
        }
    }

    var subtitleText by remember {
        mutableStateOf("")
    }

    LifecycleResumeEffect(Unit) {
        videoView.startPlayback()
        onPauseOrDispose {
            videoView.pausePlayback()
        }

    }

    DisposableEffect(videoView) {

        val eventListener = Dispatcher.EventListener { event ->
            when (event.code()) {
                PlayerEvent.State.PREPARING -> {
                    videoView.player()?.setSubtitleEnabled(true)
                }

                PlayerEvent.State.PREPARED -> {

                }

                PlayerEvent.Info.PROGRESS_UPDATE -> {
                    val progressInfo = event.cast(InfoProgressUpdate::class.java)
                }

                PlayerEvent.Info.SUBTITLE_TEXT_UPDATE -> {
                    //字幕变化回调
                    val e = event.cast(InfoSubtitleTextUpdate::class.java)
                    subtitleText = e.subtitleText.text ?: ""
                    Log.e("字幕变化回调","字幕变化回调==${e.subtitleText.text}")
                }

                PlayerEvent.Info.SUBTITLE_LIST_INFO_READY -> {
                    //字幕信息获取成功
                    videoView.player()?.let { player ->
                        val subtitles = player.subtitles
                        subtitles?.find { it.subtitleId == 1 }?.let { subtitle ->
                            player.selectSubtitle(subtitle)
                        }

                    }


                }


            }
        }
        playbackController.addPlaybackListener(eventListener)
        onDispose {
            videoView.stopPlayback()
            playbackController.removePlaybackListener(eventListener)
        }
    }

    LaunchedEffect(isSelected) {
        if(isSelected){
            playbackController.bind(videoView)
            playbackController.startPlayback()
        }else{
            playbackController.pausePlayback()
        }
    }

    Box {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier), factory = { _ ->
                videoView
            })
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp, start = 16.dp, end = 16.dp),
            text = subtitleText,
            textAlign = TextAlign.Center,
            color = Color.Red
        )

        TTVodDebugTool()
    }

}

@Composable
fun TTVodDebugTool(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val frameLayout = remember {
        FrameLayout(context)
    }

    AndroidView(modifier = modifier.fillMaxSize(), factory = {
        frameLayout
    })

    DisposableEffect(frameLayout) {

        VolcDebugTools.setContainerView(frameLayout)
        onDispose {
            VolcDebugTools.release()
        }
    }

}


@Composable
fun VideoList(playbackController: PlaybackController) {
    val state = rememberPagerState { 10 }

    Box(modifier = Modifier.fillMaxSize()) {

        VerticalPager(
            modifier = Modifier.fillMaxSize(),
            state = state,
            beyondViewportPageCount = 1
        ) { index ->
            Box {
                TTVideoView(
                    modifier = Modifier.fillMaxSize(),
                    playbackController = PlaybackController(),
                    isSelected = index==state.settledPage
                )
                TTVodDebugTool(modifier = Modifier.fillMaxSize())
            }

        }



    }
}

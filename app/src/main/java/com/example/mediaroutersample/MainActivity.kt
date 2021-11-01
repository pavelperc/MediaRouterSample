package com.example.mediaroutersample

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.mediarouter.media.MediaControlIntent
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val player = MediaPlayer.create(this, R.raw.sample)
        player.isLooping = true

        val router = MediaRouter.getInstance(this)

        val selector = MediaRouteSelector.Builder()
            .addControlCategory(MediaControlIntent.CATEGORY_LIVE_AUDIO)
            .build()

        router.addCallback(selector, callback, MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY)

        buttonSelectRoute.setOnClickListener {
            val routes = router.routes
            val routeNames = routes.map { it.name }.toTypedArray()
            AlertDialog.Builder(this)
                .setItems(routeNames) { dialog, which ->
                    val route = routes[which]
                    router.selectRoute(route)
//                    route.requestSetVolume(route.volumeMax / 2)
                }
                .show()
        }
        buttonPlay.setOnClickListener {
            if (player.isPlaying) player.pause() else player.start()
        }
    }

    private val callback = object : MediaRouter.Callback() {
        override fun onRouteSelected(router: MediaRouter, route: MediaRouter.RouteInfo, reason: Int) {
            textViewSelectedRoute.text = route.name
        }
    }
}
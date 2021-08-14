package com.ming.buildffmpegso

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var player: Int? = null
    val path = "/storage/emulated/0/DCIM/Camera/IMG_8011.MOV"

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Example of a call to a native method
        val tv = findViewById<TextView>(R.id.tv)
        tv.text = getFFmpegInfo()

        findViewById<Button>(R.id.start).setOnClickListener {
            player?.let {
                Log.d("play is id", it.toString())
                play(it)
            }
        }


        when {
            ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("MainActivity", "-----")

                initSurface()
            }
            else -> {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 100) {
            initSurface()
            Log.d("MainActivity", "获取到存储权限")
        }
    }


    private fun initSurface() {
        val surface = findViewById<SurfaceView>(R.id.sfv)

        surface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (player == null) {
                    player = createPlayer(path, holder.surface)
                }

                player?.let {
                    Log.d("play is id", it.toString())
                    play(it)
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }

        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("MainActivity--onKeyDown", keyCode.toString())
        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("MainActivity---onKeyUp", keyCode.toString())
        return true
    }


    external fun createPlayer(path: String, surface: Surface): Int
    external fun play(player: Int)
    external fun pause(player: Int)
    external fun getFFmpegInfo(): String
}
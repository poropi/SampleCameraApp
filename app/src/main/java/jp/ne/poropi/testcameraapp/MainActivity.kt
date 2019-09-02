package jp.ne.poropi.testcameraapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.CameraX
import androidx.camera.core.PreviewConfig
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        camera.post { startCameraWithPermissionCheck() }
    }

    override fun onDestroy() {
        CameraX.unbindAll()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @SuppressLint("RestrictedApi")
    @NeedsPermission(Manifest.permission.CAMERA)
    fun startCamera() {
        CameraX.unbindAll()
        val config = PreviewConfig.Builder().apply {
            setLensFacing(CameraX.LensFacing.FRONT)
        }.build()
        val preview = AutoFitPreviewBuilder.build(config, camera)
        // 作成したUseCaseをバインド
        CameraX.bindToLifecycle(this, preview)
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun close() {
        finish()
    }
}

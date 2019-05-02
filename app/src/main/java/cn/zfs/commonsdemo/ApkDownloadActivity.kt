package cn.zfs.commonsdemo

import android.app.DownloadManager
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import com.snail.commons.helper.ApkDownloadHelper
import kotlinx.android.synthetic.main.activity_apk_download.*
import java.io.File

/**
 * 调用系统下载APK
 *
 * date: 2019/5/2 19:16
 * author: zengfansheng
 */
class ApkDownloadActivity : BaseActivity() {
    private val url = "http://gdown.baidu.com/data/wisegame/55dc62995fe9ba82/jinritoutiao_448.apk"
    private var downloader: ApkDownloadHelper? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apk_download)
        val dir = externalCacheDir ?: File(Environment.getExternalStorageDirectory(), "Android/data/$packageName/files")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val savePath = File(dir, "toutiao.apk").absolutePath
        downloader = ApkDownloadHelper(this, url, "今日头条", savePath, object : ApkDownloadHelper.DownloadListener {
            override fun onProgress(downloaded: Int, total: Int) {
                progressBar.max = total
                progressBar.progress = downloaded
            }

            override fun onStateChange(status: Int) {
                when (status) {
                    DownloadManager.STATUS_FAILED -> tvStatus.text = "下载失败"
                    DownloadManager.STATUS_RUNNING -> tvStatus.text = "下载中..."
                    DownloadManager.STATUS_PAUSED -> tvStatus.text = "下载暂停"
                    DownloadManager.STATUS_PENDING -> tvStatus.text = "等待下载..."
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        tvStatus.text = "下载成功"
                        downloader!!.install(this@ApkDownloadActivity)
                    }
                }
            }

        })
        btnDownload.setOnClickListener { 
            downloader!!.start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        downloader?.onActivityResult(requestCode)
    }

    override fun onDestroy() {
        super.onDestroy()
        downloader?.cancel()
    }
}
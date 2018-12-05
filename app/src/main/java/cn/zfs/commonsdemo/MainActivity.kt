package cn.zfs.commonsdemo

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.zfs.commons.base.BaseHolder
import com.zfs.commons.base.BaseListAdapter
import com.zfs.commons.entity.PermissionsRequester
import com.zfs.commons.utils.Logger
import com.zfs.commons.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var requester: PermissionsRequester? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val data = arrayListOf("储存信息获取", "设置静态IP", "md5和sha1算法", "系统分享", "网络及位置服务状态")
        val clsArr = arrayListOf(StorageActivity::class.java, StaticIpActivity::class.java, MD5Activity::class.java, ShareActivity::class.java,
            NetStateActivity::class.java)
        lv.adapter = object : BaseListAdapter<String>(this, data) {
            override fun getHolder(position: Int): BaseHolder<String> {
                return object : BaseHolder<String>() {
                    private var tv: TextView? = null

                    override fun setData(data: String, position: Int) {
                        tv?.text = data
                    }

                    override fun createConvertView(): View {
                        val view = View.inflate(this@MainActivity, android.R.layout.simple_list_item_1, null)
                        tv = view.findViewById(android.R.id.text1)
                        return view
                    }
                }
            }
        }
        lv.setOnItemClickListener { _, _, position, _ ->
            startActivity(Intent(this, clsArr[position]))
        }
        Logger.setPrintLevel(Logger.ALL)
        requester = PermissionsRequester(this)
        val list = ArrayList<String>()
        list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        list.add(Manifest.permission.ACCESS_FINE_LOCATION)
        list.add(Manifest.permission.ACCESS_NETWORK_STATE)
        requester?.check(list)
        requester?.setOnRequestResultListener { 
            if (!it.isEmpty()) {
                ToastUtils.showShort("部分权限被拒绝，可能造成某些功能无法使用")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        requester?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requester?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

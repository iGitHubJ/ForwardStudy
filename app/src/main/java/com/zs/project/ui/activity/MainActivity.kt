package com.zs.project.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import com.zs.project.R
import com.zs.project.app.AppStatusManager
import com.zs.project.base.BaseActivity
import com.zs.project.event.RefreshEvent
import com.zs.project.ui.fragment.DouBanFragment
import com.zs.project.ui.fragment.MeFragment
import com.zs.project.ui.fragment.NewsFragment
import com.zs.project.util.ScreenUtil
import com.zs.project.util.SpUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode



/**
 * @author Administrator
 */
class MainActivity : BaseActivity() {

    private var mTransaction: FragmentTransaction? = null
    private var mCurrentFragment: Fragment? = null
    private var mNewsFragment: NewsFragment? = null
    private var mDouBanFragment: DouBanFragment? = null
    private var mMeFragment: MeFragment? = null
    private var mFragments : ArrayList<Fragment> = arrayListOf()

    var mIsShow = true  // true：语音播放框滑动到显示状态  false：语音播放框移除屏幕状态
    var SAVE_INDEX : String = "save_index"
    var mTags = arrayOf("news" , "movies" , "me")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
    }

    override fun init() {

        mTransaction = supportFragmentManager.beginTransaction()
        mNewsFragment = NewsFragment()
        mDouBanFragment = DouBanFragment()
        mMeFragment = MeFragment()
        mFragments?.add(mNewsFragment!!)
        mFragments?.add(mDouBanFragment!!)
        mFragments?.add(mMeFragment!!)

        changePage(0)

        fragment_news?.setOnClickListener(this)
        fragment_douban?.setOnClickListener(this)
        fragment_me?.setOnClickListener(this)

        colorfull_bg_view?.changeImg(SpUtil.getInt("color_view",0))
//        colorfull_bg_view?.switchAnim(false)
    }

    override fun initData() {

    }

    /**
     * 切换page
     */
    private fun changePage(index : Int){
        changeTab(index)
        changeFragment(index)
    }

    /**
     * fragment切换
     * @param nextFragment
     */
    private fun changeFragment(index: Int) {
        var nextFragment = mFragments[index]
        if (nextFragment != null) {
            if (mCurrentFragment != null) {
                var transaction = supportFragmentManager.beginTransaction()
                transaction.hide(mCurrentFragment).commitAllowingStateLoss()
            }
            mCurrentFragment = nextFragment
            var transaction = supportFragmentManager.beginTransaction()
            if (nextFragment.isAdded) {
                transaction.show(nextFragment).commitAllowingStateLoss()
            } else {
                transaction.add(R.id.fl_homepage, nextFragment,mTags[index]).commitAllowingStateLoss()
            }
        }
    }

    private fun changeTab(index : Int){
        when(index){
            0 ->{
//                tv_news.setTextColor(ContextCompat.getColor(this,R.color.app_main_color))
//                tv_product.setTextColor(ContextCompat.getColor(this,R.color.main_color_gray))
//                tv_me.setTextColor(ContextCompat.getColor(this,R.color.main_color_gray))

//                iv_home_news.setBackgroundResource(R.mipmap.home_bar_news_sel)
//                iv_home_product.setBackgroundResource(R.mipmap.home_bar_dou_nor)
//                iv_home_me.setBackgroundResource(R.mipmap.home_bar_user_nor)

                dynamicAddView(tv_news, "textColor", R.color.app_main_color)
                dynamicAddView(tv_product, "textColor", R.color.main_color_gray)
                dynamicAddView(tv_me, "textColor", R.color.main_color_gray)

                dynamicAddView(iv_home_news, "background", R.mipmap.home_bar_news_sel)
                dynamicAddView(iv_home_product, "background", R.mipmap.home_bar_dou_nor)
                dynamicAddView(iv_home_me, "background", R.mipmap.home_bar_user_nor)

            }
            1 ->{
//                tv_news.setTextColor(ContextCompat.getColor(this,R.color.main_color_gray))
//                tv_product.setTextColor(ContextCompat.getColor(this,R.color.app_main_color))
//                tv_me.setTextColor(ContextCompat.getColor(this,R.color.main_color_gray))

//                iv_home_news.setBackgroundResource(R.mipmap.home_bar_news_nor)
//                iv_home_product.setBackgroundResource(R.mipmap.home_bar_dou_sel)
//                iv_home_me.setBackgroundResource(R.mipmap.home_bar_user_nor)

                dynamicAddView(tv_news, "textColor", R.color.main_color_gray)
                dynamicAddView(tv_product, "textColor", R.color.app_main_color)
                dynamicAddView(tv_me, "textColor",R.color.main_color_gray)

                dynamicAddView(iv_home_news, "background", R.mipmap.home_bar_news_nor)
                dynamicAddView(iv_home_product, "background", R.mipmap.home_bar_dou_sel)
                dynamicAddView(iv_home_me, "background", R.mipmap.home_bar_user_nor)
            }
            2 ->{
//                tv_news.setTextColor(ContextCompat.getColor(this,R.color.main_color_gray))
//                tv_product.setTextColor(ContextCompat.getColor(this,R.color.main_color_gray))
//                tv_me.setTextColor(ContextCompat.getColor(this,R.color.app_main_color))

//                iv_home_news.setBackgroundResource(R.mipmap.home_bar_news_nor)
//                iv_home_product.setBackgroundResource(R.mipmap.home_bar_dou_nor)
//                iv_home_me.setBackgroundResource(R.mipmap.home_bar_user_sel)

                dynamicAddView(tv_news, "textColor", R.color.main_color_gray)
                dynamicAddView(tv_product, "textColor", R.color.main_color_gray)
                dynamicAddView(tv_me, "textColor",R.color.app_main_color)

                dynamicAddView(iv_home_news, "background", R.mipmap.home_bar_news_nor)
                dynamicAddView(iv_home_product, "background", R.mipmap.home_bar_dou_nor)
                dynamicAddView(iv_home_me, "background", R.mipmap.home_bar_user_sel)
            }
        }
    }

    private fun startAnimation(view: ImageView) {
        val scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0.6f, 1f, 0.75f, 1f)
        scaleXAnimator.repeatCount = 0
        //沿y轴放大
        val scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0.6f, 1f, 0.75f, 1f)
        scaleYAnimator.repeatCount = 0
        val set = AnimatorSet()
        //同时沿X,Y轴放大
        set.play(scaleXAnimator).with(scaleYAnimator)
        //都设置1s，也可以为每个单独设置
        set.duration = 300
        set.startDelay = 100
        set.start()
    }

    /**
     *
     * @param isShow
     */
    private fun setVoicePlayerAnim(isShow: Boolean) {
        val objectAnimator: ObjectAnimator
        if (home_bottom_tab?.visibility == View.GONE) {
            return
        }
        if (mIsShow == isShow) {
            return
        }
        if (isShow) {
            objectAnimator = ObjectAnimator.ofFloat<View>(home_bottom_tab, View.TRANSLATION_Y, ScreenUtil.dp2px(50f).toFloat(), 0f)
            mIsShow = true
        } else {
            objectAnimator = ObjectAnimator.ofFloat<View>(home_bottom_tab, View.TRANSLATION_Y, 0f, ScreenUtil.dp2px(50f).toFloat())
            mIsShow = false
        }
        objectAnimator.duration = 400
        objectAnimator.start()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleScroll(event: RefreshEvent?) {
        if (event == null){
            return
        }
        if ("scroll" == event.getmFlag()){
            setVoicePlayerAnim(event.isRefresh)
        }else if ("colorview" == event.getmFlag()){
            colorfull_bg_view?.changeImg(event.refresh_int)
        }

    }

    override fun onClick(view: View) {

        when(view.id){
            R.id.fragment_news ->{
                changePage(0)
                startAnimation(iv_home_news)
            }
            R.id.fragment_douban ->{
                changePage(1)
                startAnimation(iv_home_product)
            }
            
            R.id.fragment_me ->{
                changePage(2)
                startAnimation(iv_home_me)
            }
        }

    }

    override fun restartApp() {
        startActivity(Intent(this,GuideActivity::class.java))
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("My_Log_base", "main onNewIntent")
        var action = intent?.getIntExtra(AppStatusManager.KEY_HOME_ACTION,AppStatusManager.ACTION_BACK_TO_HOME)
        if (action == AppStatusManager.ACTION_RESTART_APP){
            restartApp()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0){
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            startActivity(intent)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}

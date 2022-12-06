package com.ayvytr.pic.main

import android.os.Bundle
import com.ayvytr.flow.BaseActivity
import com.ayvytr.pic.databinding.ActivityMainBinding


class MainActivity: BaseActivity<MainViewModel>() {

    lateinit var binding: ActivityMainBinding

//    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        val toolbar = binding.includeMain.toolbar
        setSupportActionBar(toolbar)

        //问题：切换fragment，会重建
//        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home,
//                R.id.nav_settings
//            ), binding.drawerLayout
//        )
//        toolbar.setupWithNavController(navController, appBarConfiguration)
//        binding.navView.setupWithNavController(navController)

//        L.e( navController.navigatorProvider.navigators )
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}
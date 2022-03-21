package com.alzu.android.newsroom.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.data.Article
import com.alzu.android.newsroom.data.ArticleDB
import com.alzu.android.newsroom.repository.NewsRepository
import com.alzu.android.newsroom.ui.fragments.ArticleFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class NewsRoomActivity : AppCompatActivity() {
    val TAG = "NewsRoomActivity"
    lateinit var viewModel: NewsViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var checkOut: MenuItem
    private var item: Article? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_room)
        //AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

        val repo = NewsRepository(ArticleDB(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(repo)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)
            .get(NewsViewModel::class.java)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //Setup the bottom navigation view with navController
        findViewById<BottomNavigationView>(R.id.bottomnavigation)
            .setupWithNavController(navController)

        //Setup actionBar
        setSupportActionBar(findViewById(R.id.toolbar))
        appBarConfiguration = AppBarConfiguration(setOf(R.id.newsFeedFragment,
            R.id.searchFragment,R.id.bookmarksFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        //Inflate menu to initialize checkOut menuItem
        val menu = findViewById<Toolbar>(R.id.toolbar).menu
        menuInflater.inflate(R.menu.menu_app_bar,menu)
        menu?.let {
            checkOut = it.findItem(R.id.action_add_bookmark)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app_bar,menu)
        menu?.let {
            checkOut = it.findItem(R.id.action_add_bookmark)
            checkOut.isVisible = false
        }
        return true
    }

    // set onClickListener bookmark icon on toolbar
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_add_bookmark -> {
                supportFragmentManager.setFragmentResultListener("article_item",this){
                    articleItem, bundle -> item = bundle.getSerializable("article") as Article?
                    Log.i(TAG, "reading result")
                }
                Log.i(TAG,"$item")
                item?.let{
                    viewModel.saveArticle(it)
                    val mView = findViewById<View>(android.R.id.content)
                    Snackbar.make(mView, "Bookmark added", Snackbar.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
    }

}
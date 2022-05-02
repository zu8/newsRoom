package com.alzu.android.newsroom.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.alzu.android.newsroom.R
import com.alzu.android.newsroom.domain.ArticleEntity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class NewsRoomActivity : AppCompatActivity() {
    val TAG = "NewsRoomActivity"

    lateinit var viewModel: NewsViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var checkOut: MenuItem
    private var item: ArticleEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_room)
        // setting theme
        setTheme(this)

        val viewModelProviderFactory = NewsViewModelProviderFactory(this.application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(NewsViewModel::class.java)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //Setup the bottom navigation view with navController
        findViewById<BottomNavigationView>(R.id.bottomnavigation)
            .setupWithNavController(navController)

        //Setup actionBar
        setSupportActionBar(findViewById(R.id.toolbar))
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.newsFeedFragment,
                R.id.searchFragment, R.id.bookmarksFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        //Inflate menu to initialize checkOut menuItem
        val menu = findViewById<Toolbar>(R.id.toolbar).menu
        menuInflater.inflate(R.menu.menu_app_bar, menu)
        menu?.let {
            checkOut = it.findItem(R.id.action_add_bookmark)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app_bar, menu)
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
                supportFragmentManager.setFragmentResultListener(
                    "article_item",
                    this
                ) { articleItem, bundle ->
                    item = bundle.getSerializable("article") as ArticleEntity?
                    Log.i(TAG, "reading result")
                }
                Log.i(TAG, "$item")
                item?.let {
                    viewModel.saveArticle(it)
                    val mView = findViewById<View>(android.R.id.content)
                    Snackbar.make(mView, "Bookmark added", Snackbar.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(menuItem)
        }
    }

    private fun setTheme(context: Context){
        val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val nightMode = sp.getBoolean("nightMode", false)
        val mode = if (nightMode as Boolean) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
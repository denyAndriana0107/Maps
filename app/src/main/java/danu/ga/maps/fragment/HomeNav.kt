package danu.ga.maps.fragment

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import danu.ga.maps.R
import danu.ga.maps.fragment.ui.home.HomeFragment
import danu.ga.maps.fragment.ui.jadwalwisata.SlideshowFragment
import danu.ga.maps.fragment.ui.jadwalwisata.SlideshowViewModel
import danu.ga.maps.fragment.ui.maps.MapsFragment2
import kotlinx.coroutines.*
import unpas.ac.id.maps.viewModel.WisataViewModel
import kotlin.math.cos


class HomeNav : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var idFragment : Int ?= null
    lateinit var fab :FloatingActionButton
    lateinit var wisataViewModel: WisataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_nav)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        wisataViewModel = ViewModelProvider(this).get(WisataViewModel::class.java)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//      val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
//        scope.launch {
//            getCurrentFragment()
//
//        }
        fab = findViewById(R.id.fab)
        val fm = supportFragmentManager
        val fragment: HomeFragment? = fm.findFragmentById(R.id.nav_home) as HomeFragment?
        var cost: Int = 0
        var jarak:Double = 0.0
        var button:Boolean = false
        fab.visibility = View.GONE
        wisataViewModel.readData.observe(this,{
            if (it.size!=0){
                cost = it[0].cost
                button = it[0].button
                jarak = it[0].jarak
                if (button==false){
                    fab.visibility = View.GONE
                }else{
                    fab.visibility = View.VISIBLE
                }
            }
        })

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Jarak : ${jarak}KM & Cost : ${cost} Rupiah", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_nav, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
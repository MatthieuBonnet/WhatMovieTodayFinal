package com.example.whatmovietodayfinal
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)

        // Chargez le fragment Home par défaut
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            HomeFragment()).commit()
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.navigation_home -> selectedFragment = HomeFragment()
            R.id.navigation_Liste -> selectedFragment = ListeFragment()
            R.id.navigation_historique -> selectedFragment = HistoriqueFragment()
        }
        // Chargez le fragment sélectionné
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,
            selectedFragment!!).commit()
        true
    }
}

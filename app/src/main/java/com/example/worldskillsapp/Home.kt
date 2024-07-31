package com.example.worldskillsapp

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager

class Home : AppCompatActivity() {
    private lateinit var carrossel: ViewPager
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        window.statusBarColor = Color.parseColor("#3b2a93")
        supportActionBar?.hide()

        val button = findViewById<Button>(R.id.my_button)
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://worldskills.org/what/competitions/"))
            startActivity(intent)
        }

        setupCarrosel()
        starAutoScroll()
    }

    private fun setupCarrosel() {
        carrossel = findViewById(R.id.carrossel)
        val images = listOf(R.drawable.ws1,R.drawable.ws2,R.drawable.ws3,R.drawable.ws4,R.drawable.ws5)
        carrossel.adapter = CarouselAdapter(images)
    }

    private fun starAutoScroll() {
        val update = Runnable {
            if (currentPage == (carrossel.adapter?.count ?: 1) - 1) {
                currentPage = 0
            } else {
                currentPage++
            }
            carrossel.setCurrentItem(currentPage, true)
        }
    }
}
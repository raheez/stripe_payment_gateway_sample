package com.example.stripepaymentgateway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.stripepaymentgateway.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding?.root)

        mainBinding?.payButton?.setOnClickListener {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
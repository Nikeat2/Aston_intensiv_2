package com.example.astonhomework2

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlin.random.Random

class MainActivity : AppCompatActivity(), OnColorStopListener {
    private lateinit var myView: CircularView
    private lateinit var ivRandom: ImageView
    private lateinit var btnReset: Button
    private lateinit var btnRotate: Button
    private lateinit var tvGenerator: TextGenerator
    private var picture: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ivRandom = findViewById(R.id.ivRandom)
        tvGenerator = findViewById(R.id.tvRandom)
        myView = findViewById(R.id.circularView)
        btnRotate = findViewById(R.id.btnRotate)
        myView.setOnColorStopListener(this)
        setListenerOnReset()
        btnRotate.setOnClickListener {
            startRotation()
        }
        restoreState()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveState()
    }

    override fun onColorStop(color: String) {
        when (color) {
            "red", "yellow", "lightblue", "purple" -> getText()
            "orange", "green", "blue" -> getPicture()
        }
    }

    private fun startRotation() {
        val numSectors = myView.colors.size
        val randomSector = Random.nextInt(numSectors)
        val baseAngle = (360 / numSectors) * randomSector
        val additionalRotations = 720
        val finalDegrees = (baseAngle + additionalRotations)
        myView.rotate(finalDegrees.toLong())
    }

    private fun getPicture() {
        ivRandom = findViewById(R.id.ivRandom)
        Glide.with(this)
            .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS010nWLutD0JsDnMldo_SNPA3B-3QUTZNbeQ&usqp=CAU")
            .centerCrop()
            .into(ivRandom)
    }

    private fun getText() {
        tvGenerator.setNewText("Hey, you've got a text!")
    }

    private fun setListenerOnReset() {
        btnReset = findViewById(R.id.btnReset)
        btnReset.setOnClickListener {
            val tvGenerator: TextGenerator = findViewById(R.id.tvRandom)
            tvGenerator.updateText()
            ivRandom = findViewById(R.id.ivRandom)
            ivRandom.setImageDrawable(null)
        }
    }

    private fun saveState() {
        val sharedPreferences = getSharedPreferences("CustomView", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("Text", tvGenerator.text)
            .putString(
                "Picture url",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS010nWLutD0JsDnMldo_SNPA3B-3QUTZNbeQ&usqp=CAU"
            ).apply()
    }

    private fun restoreState() {
        val sharedPreferences = getSharedPreferences("CustomView", Context.MODE_PRIVATE)
        picture = sharedPreferences.getString("Picture url", "")
        Glide.with(this).load(picture).centerCrop().into(ivRandom)
        tvGenerator.text = sharedPreferences.getString("Text", "").toString()
    }
}

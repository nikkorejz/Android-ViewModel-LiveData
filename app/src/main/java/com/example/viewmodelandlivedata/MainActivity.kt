package com.example.viewmodelandlivedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var textViewProduct: TextView
    private lateinit var textViewTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val model: MyViewModel by viewModels()

        listView = findViewById(R.id.ListView)
        editText = findViewById(R.id.EditText)
        button = findViewById(R.id.Button)
        textViewTime = findViewById(R.id.TimeTextView)
        textViewProduct = findViewById(R.id.ProductTextView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, model.numbers)
        listView.adapter = adapter


        button.setOnClickListener {
            val input = editText.text.toString()
            try {
                val number = input.toInt()
                model.add(number)
//                adapter.notifyDataSetChanged()
            } catch (e: java.lang.NumberFormatException) {
                Toast.makeText(this, "Введите число", Toast.LENGTH_SHORT).show()
            }
            editText.text.clear()
        }

        model.getProduct().observe(this) {
            textViewProduct.text = it.toString()
        }

        model.getNumbers().observe(this) {
            adapter.notifyDataSetChanged()
        }

        model.getLiveTime().observe(this) {
            Log.i(TAG, "TextView updated")
            textViewTime.text = it.toString()
        }

    }
}
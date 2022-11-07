package com.example.lks_jabar_testing_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MenuActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        recyclerView= findViewById(R.id.recycleView)
        refresh()

        val btn_Add = findViewById<FloatingActionButton>(R.id.btn_add)
        btn_Add.setOnClickListener {
            val intent = Intent(this,PostUpdateActivity::class.java)
            intent.putExtra("jenis", "POST")
            startActivity(intent)
        }

    }
    fun refresh(){
        val url=URL(" http://127.0.0.1:8000/api/produk")
        thread {
            with(url.openConnection() as HttpURLConnection){
                addRequestProperty("Authorization" , MainActivity.token)
                val result= inputStream.bufferedReader().readText()
                val a = JSONArray(result)
                if(responseCode==200){
                    runOnUiThread {
                        recyclerView.adapter=ItemAdapter(this@MenuActivity,a)
                        recyclerView.layoutManager=LinearLayoutManager(this@MenuActivity)
                    }
                }
                else {
                    runOnUiThread {
                        Toast.makeText(this@MenuActivity, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
package com.example.lks_jabar_testing_kotlin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    companion object{
        var token : String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_login = findViewById<Button>(R.id.btn_login)
        val edt_username = findViewById<EditText>(R.id.edt_username)
        val edt_password = findViewById<EditText>(R.id.edt_password)

        val status = intent.getStringExtra("status")
        if (status != null){
            Toast.makeText(this, status.toString(), Toast.LENGTH_SHORT).show()
        }

        val btn_register = findViewById<Button>(R.id.btn_register)
        btn_register.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            val url = URL(" http://127.0.0.1:8000/api/login")
            thread {
                with(url.openConnection() as HttpURLConnection){
                    val username = edt_username.text.toString()
                    val password = edt_password.text.toString()
                    requestMethod="POST"
                    addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                    with(outputStream.bufferedWriter()){
                        write("username=${Uri.encode((username))}&password=${Uri.encode(password)}")
                        flush()
                    }
                    if (responseCode==200){
                        val result= inputStream.bufferedReader().readText()
                        val data = JSONObject(result)
                        runOnUiThread {
                            token = "Bearer " + data["token"].toString()
//                            Toast.makeText(this@MainActivity, token, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity,MenuActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    else{
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "ERROR", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }
}
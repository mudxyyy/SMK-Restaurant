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

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btn_register = findViewById<Button>(R.id.btn_register)
        val edt_username = findViewById<EditText>(R.id.edt_username)
        val edt_password = findViewById<EditText>(R.id.edt_password)
        val edt_confirm_password = findViewById<EditText>(R.id.edt_confirm_password)
        btn_register.setOnClickListener {
            if (edt_password.text.toString() != edt_confirm_password.text.toString()) {
                Toast.makeText(this, "Password Tidak Sama", Toast.LENGTH_SHORT).show()
            } else {
                val url = URL(" http://127.0.0.1:8000/api/register")
                thread {
                    with(url.openConnection() as HttpURLConnection) {

                        val username = edt_username.text.toString()
                        val password = edt_password.text.toString()
                        requestMethod = "POST"
                        addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                        with(outputStream.bufferedWriter()) {
                            write("username=${Uri.encode((username))}&password=${Uri.encode(password)}")
                            flush()
                        }
                        if (responseCode == 200) {
                            runOnUiThread {
//                            Toast.makeText(this@MainActivity, token, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.putExtra("status", "Berhasil Register")
                                startActivity(intent)
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(this@RegisterActivity, "ERROR", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }
}
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

class PostUpdateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_update)

        val edt_menu_name = findViewById<EditText>(R.id.edt_menu_name)
        val edt_menu_description = findViewById<EditText>(R.id.edt_menu_dsc)
        val edt_menu_price = findViewById<EditText>(R.id.edt_menu_price)
        val btn_post_update = findViewById<Button>(R.id.btn_post_update)


        val jenis = intent.getStringExtra("jenis")
        if (jenis == "POST"){

            btn_post_update.setOnClickListener {
//                Toast.makeText(this, "ini tombol post", Toast.LENGTH_SHORT).show()
                val url = URL(" http://127.0.0.1:8000/api/produk")
                thread {
                    with(url.openConnection() as HttpURLConnection) {
                        val menu_name = edt_menu_name.text.toString()
                        val menu_description = edt_menu_description.text.toString()
                        val menu_price = edt_menu_price.text.toString()
                        requestMethod = "POST"
                        addRequestProperty("Authorization", MainActivity.token)
                        addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                        with(outputStream.bufferedWriter()) {
                            write(
                                "name=${Uri.encode((menu_name))}&deskripsi=${
                                    Uri.encode(
                                        menu_description
                                    )
                                }&harga=${Uri.encode(menu_price)}"
                            )
                            flush()
                        }
                        if (responseCode == 200) {

                            runOnUiThread {
//                            Toast.makeText(this@MainActivity, token, Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@PostUpdateActivity,MenuActivity::class.java)

                                startActivity(intent)
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(this@PostUpdateActivity, "ERROR", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
        else{
            btn_post_update.setOnClickListener {
                Toast.makeText(this, "Edit Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MenuActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
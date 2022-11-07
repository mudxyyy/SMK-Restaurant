package com.example.lks_jabar_testing_kotlin

import android.content.Intent
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ItemAdapter(val activity: MenuActivity, val Menu:JSONArray) : RecyclerView.Adapter<ItemAdapter.MyViewHolder>()  {

    class MyViewHolder(itemview : View): RecyclerView.ViewHolder(itemview){
        val txtMenuID:TextView= itemview.findViewById(R.id.txtId)
        val txt_menu_name:TextView= itemview.findViewById(R.id.txt_menu_name)
        val txt_menu_description:TextView= itemview.findViewById(R.id.txt_menu_desription)
        val txt_menu_price:TextView = itemview.findViewById(R.id.txt_menu_price)
        val btn_edit:ImageButton = itemview.findViewById(R.id.btn_edit)
        val btn_delete:ImageButton = itemview.findViewById(R.id.btn_delete)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.menu_item,parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemAdapter.MyViewHolder, position: Int) {
        val item= Menu.getJSONObject(position)
        holder.txtMenuID.text=item["ID"].toString()
        holder.txt_menu_name.text=item["name"].toString()
        holder.txt_menu_description.text=item["deskripsi"].toString()
        holder.txt_menu_price.text=item["harga"].toString()
        holder.btn_edit.setOnClickListener {
            val intent = Intent(activity, PostUpdateActivity::class.java)
            intent.putExtra("jenis", "update")
            intent.putExtra("ID", holder.txtMenuID.text.toString())
            intent.putExtra("name", holder.txt_menu_name.text.toString())
            intent.putExtra("deskripsi", holder.txt_menu_description.text.toString())
            intent.putExtra("harga", holder.txt_menu_price.text.toString())
            activity.startActivity(intent)

        }

        holder.btn_delete.setOnClickListener {
            val url= URL(" http://127.0.0.1:8000/api/produk/" + holder.txtMenuID.text.toString())
            thread {
                with(url.openConnection() as HttpURLConnection){
                    requestMethod= "DELETE"
                    doInput = true
                    doOutput = true
                    addRequestProperty("Authorization", MainActivity.token)
                    with(outputStream.bufferedWriter()){
                        flush()
                    }
                    if (responseCode==200){
                        activity.runOnUiThread {
                            Toast.makeText(activity, "Berhasil Hapus Data", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        activity.runOnUiThread {
                            Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show()
                        }
                    }
                    activity.refresh()
                }
            }
        }
    }

    override fun getItemCount(): Int = Menu.length()



}
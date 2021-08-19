package com.ruchanokal.mathmaster.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ruchanokal.mathmaster.R
import com.ruchanokal.mathmaster.fragments.SeviyeBirFragment
import kotlinx.android.synthetic.main.room_row.view.*
import org.w3c.dom.Text

class RoomAdapter(val roomList: ArrayList<String>, val zorlukList: ArrayList<Long>)
    : RecyclerView.Adapter<RoomAdapter.RoomHolder>() {



    class RoomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val playerNameText : TextView = itemView.playerNameText
        val zorlukSeviyesiText : TextView = itemView.zorlukSeviyesiText


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_row,parent,false)

        return RoomHolder(view)

    }

    override fun onBindViewHolder(holder: RoomHolder, position: Int) {


        if ( roomList != null && roomList.size > 0 ) {

            holder.playerNameText.text = roomList.get(position)

        }

        if ( zorlukList != null && zorlukList.size > 0) {

            val z = zorlukList.get(position).toString().toInt()

            if (z == 1){

                holder.zorlukSeviyesiText.text = "TEMEL"

            } else if ( z == 2) {

                holder.zorlukSeviyesiText.text = "KOLAY"

            } else if ( z == 3) {

                holder.zorlukSeviyesiText.text = "ORTA"

            } else if ( z == 4) {

                holder.zorlukSeviyesiText.text = "ZOR"

            } else if ( z == 5) {

                holder.zorlukSeviyesiText.text = "UZMAN"

            }

        }




    }

    override fun getItemCount(): Int {
          return roomList.size
    }
}
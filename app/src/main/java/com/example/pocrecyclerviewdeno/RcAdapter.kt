package com.example.pocrecyclerviewdeno

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pocrecyclerviewdeno.model.Cassettes
import java.util.logging.Handler

class RcAdapter(val context: Context, val list: MutableList<Cassettes>, var listener: Listener) :
    RecyclerView.Adapter<RcAdapter.ItemViewHolder>() {


//    var listTemp: MutableList<Cassettes> = list

    interface Listener {
        fun onNext(postion: Int, type: String, amount: String)
    }


    fun updateFocus(postion: Int, type: String) {

        list.forEach {
            it.dispense.isFocus = false
            it.reject.isFocus = false
        }

        if (postion == list.size) { // check last index
            if (type == "Dispense") {
                list[0].reject.isFocus = true
                notifyItemChanged(0)
            }
        } else {
            if (type == "Dispense") {
                list[postion].dispense.isFocus = true
            } else {
                list[postion].reject.isFocus = true
            }
//            notifyItemChanged(postion)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]

        holder.tvTitle.text = item.title
        holder.edtItem01.setText(item.dispense.dispenseAmount)
        holder.edtItem02.setText(item.reject.rejectAmount)



        ////dispense
        holder.edtItem01.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    listener.onNext(position, "Dispense", holder.edtItem01.text.toString())
                }
            }
            return@setOnKeyListener false
        }
        ////reject
        holder.edtItem02.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    listener.onNext(position, "Reject", holder.edtItem02.text.toString())
                }
            }
            return@setOnKeyListener false
        }


        //set focus change
        holder.edtItem01.setOnTouchListener { v, event ->
            holder.edtItem01.apply {
                setSelectAllOnFocus(true)
                selectAll()
            }
            false
        }
        holder.edtItem01.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    v?.requestFocus()
                    holder.edtItem01.apply {
                        this.selectAll()
                    }
                }
            }
        }


        holder.edtItem02.setOnTouchListener { v, event ->
            holder.edtItem02.apply {
                setSelectAllOnFocus(true)
                selectAll()
            }
            false
        }
        holder.edtItem02.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v?.requestFocus()
                    holder.edtItem02.apply {
                        this.selectAll()
                    }
                }
            }



        if (item.dispense.isFocus) {
            holder.edtItem01.requestFocus()
        } else {
            holder.edtItem01.clearFocus()
        }

        if (item.reject.isFocus) {
            holder.edtItem02.requestFocus()
        } else {
            holder.edtItem02.clearFocus()
        }


        ///watcher edt
        list.forEach { it.diffAmount = 0 }
        holder.edtItem01.afterTextChanged {
            Log.d("LOG_TAG", "$it: ")
            list[position].dispense.dispenseAmount = it

            updateDiffAmount(position,holder,it.toInt())

        }

        ///watcher edt
        holder.edtItem02.afterTextChanged {
            Log.d("LOG_TAG2", "$it: ")
            list[position].reject.rejectAmount = it

            updateDiffAmount(position,holder,it.toInt())
        }




    }
    fun updateDiffAmount(position: Int,holder: ItemViewHolder,str:Int){


        list[position].diffAmount += str

        holder.edtItem03.setText(list[position].diffAmount.toString())
    }


    inner class ItemViewHolder(itemView: View) : ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val edtItem01 = itemView.findViewById<EditText>(R.id.edtItem01)
        val edtItem02 = itemView.findViewById<EditText>(R.id.edtItem02)
        val edtItem03 = itemView.findViewById<EditText>(R.id.edtItem03)

    }

}


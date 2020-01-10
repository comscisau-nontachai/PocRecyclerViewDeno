package com.example.pocrecyclerviewdeno

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputFilter
import android.text.Spanned
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
import java.util.regex.Matcher
import java.util.regex.Pattern


class RcAdapter(val context: Context, val list: MutableList<Cassettes>, var listener: Listener) :
    RecyclerView.Adapter<RcAdapter.ItemViewHolder>() {


    interface Listener {
        fun onNext(postion: Int, type: String, amount: String)
        fun onUpdateTotal(dispenseTotal: String, type: String)
    }

    fun checkZero() {

        list.forEachIndexed { i, element ->
            if (element.dispense.dispenseAmount == "0") {
                Log.d("LOG_TAG", "pos : $i: ")
            }
        }

//       val index = list.forEachIndexed { index, cassettes -> if(cassettes.dispense.dispenseAmount == "0") index }
//       Log.d("LOG_TAG", "pos : $index: ")
    }

    fun updateFocusManual(postion: Int, type: String) {

        list.forEach {
            it.dispense.isFocus = false
            it.reject.isFocus = false
        }


        if (type == "Dispense") {
            list[postion].dispense.isFocus = true
        } else {
            list[postion].reject.isFocus = true
        }
        notifyItemChanged(postion)
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

        holder.tvTitle.text = "${item.title}  ${item.currency}"
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
            return@setOnTouchListener false
        }

        holder.edtItem01.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    v?.requestFocus()
                    holder.edtItem01.apply {
                        this.selectAll()
                    }
                } else {
                    if (item.dispense.dispenseAmount.toInt() == 0)
                        holder.edtItem01.setText("0")
                }
            }
        }


        holder.edtItem02.setOnTouchListener { v, event ->
            holder.edtItem02.apply {
                setSelectAllOnFocus(true)
                selectAll()
            }
            return@setOnTouchListener false
        }


        holder.edtItem02.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    v?.requestFocus()
                    holder.edtItem02.apply {
                        this.selectAll()
                    }
                } else {
                    if (item.reject.rejectAmount.toInt() == 0)
                        holder.edtItem02.setText("0")
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

        holder.edtItem01.afterTextChanged {

            list[position].dispense.dispenseAmount = it.safetyZero()

            val dispenseTotal =
                (list[position].dispense.dispenseAmount.safetyZero().toInt() * list[position].currency)
            list[position].dispense.dispenseTotal = dispenseTotal
            val sum = list.map { it.dispense.dispenseTotal }.sum()

            listener.onUpdateTotal(sum.toString(), "Dispense")


            //update amount
            updateAmount(position, holder)
        }

        ///watcher edt
        holder.edtItem02.afterTextChanged {

            list[position].reject.rejectAmount = it.safetyZero()
            val rejectTotal =
                list[position].reject.rejectAmount.safetyZero().toInt() * list[position].currency
            list[position].reject.rejectTotal = rejectTotal
            val sum = list.map { it.reject.rejectTotal }.sum()

            listener.onUpdateTotal(sum.toString(), "Reject")

            updateAmount(position, holder)
        }

    }


    fun updateAmount(position: Int, holder: ItemViewHolder) {
        val totalAmount: Int =
            (list[position].dispense.dispenseAmount.safetyZero().toInt() + list[position].reject.rejectAmount.safetyZero().toInt()) * list[position].currency
        holder.edtItem03.setText(totalAmount.toString())
    }


    inner class ItemViewHolder(itemView: View) : ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        val edtItem01 = itemView.findViewById<EditText>(R.id.edtItem01)
        val edtItem02 = itemView.findViewById<EditText>(R.id.edtItem02)
        val edtItem03 = itemView.findViewById<EditText>(R.id.edtItem03)

    }

}


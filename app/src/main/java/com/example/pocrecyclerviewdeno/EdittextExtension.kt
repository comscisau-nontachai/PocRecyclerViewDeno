package com.example.pocrecyclerviewdeno

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            afterTextChanged.invoke(p0.toString())
        }

        override fun afterTextChanged(editable: Editable?) {

        }
    })
}


fun String.safetyZero() : String{
    if(this == ""){
        return  "0"
    }
    return this
}

fun EditText.newFormatText(){

    val str:String = if(this.text.toString().equals("")) "0" else this.text.toString()
    val longValue = str.toLong()

    val formatter = DecimalFormat("##,###")
    val formatterStr = formatter.format(longValue)

    this.setText(formatterStr)

}
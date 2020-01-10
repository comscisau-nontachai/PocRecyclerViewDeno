package com.example.pocrecyclerviewdeno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocrecyclerviewdeno.model.Cassettes
import com.example.pocrecyclerviewdeno.model.Dispense
import com.example.pocrecyclerviewdeno.model.Reject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_row.*

class MainActivity : AppCompatActivity(), RcAdapter.Listener {

    var rcAdapter: RcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val cassettes: MutableList<Cassettes> = mutableListOf()

        val cassettes1 = Cassettes(currency = 100,title = "CAD", dispense = Dispense( "0",true))
        val cassettes2 = Cassettes(currency = 200,title = "CAD")
        val cassettes3 = Cassettes(currency = 300,title = "CAD")
        val cassettes4 = Cassettes(currency = 400,title = "CAD")
        val cassettes5 = Cassettes(currency = 500,title = "CAD")

        cassettes.add(cassettes1)
        cassettes.add(cassettes2)
        cassettes.add(cassettes3)
        cassettes.add(cassettes4)
        cassettes.add(cassettes5)

        recyclerView.init()
        rcAdapter = RcAdapter(this@MainActivity, cassettes, listener = this@MainActivity)
        recyclerView.setItemViewCacheSize(cassettes.size)
        recyclerView.adapter = rcAdapter


        btn_focus.setOnClickListener {
            rcAdapter?.updateFocusManual(1, "Reject")
        }
        btn_focus2.setOnClickListener {
            rcAdapter?.updateFocusManual(3, "Dispense")
        }


        btn_check_zero.setOnClickListener {
            rcAdapter?.checkZero()
        }



        edt_test.newFormatText()
    }

    override fun onNext(postion: Int, type: String, amount: String) {
        rcAdapter?.updateFocus(postion + 1, type)

    }

    override fun onUpdateTotal(amountTotal: String, type: String) {

        when (type) {
            "Dispense" -> {
                txtTotalDispense.text = amountTotal
            }
            "Reject" -> {
                txtTotalReject.text = amountTotal
            }
        }
        val total = (txtTotalDispense.text.toString().toInt() + txtTotalReject.text.toString().toInt())
        txtTotalATMAmount.text = total.toString()

    }

}

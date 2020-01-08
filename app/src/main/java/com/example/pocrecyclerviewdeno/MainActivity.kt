package com.example.pocrecyclerviewdeno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocrecyclerviewdeno.model.Cassettes
import com.example.pocrecyclerviewdeno.model.Dispense
import com.example.pocrecyclerviewdeno.model.Reject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),RcAdapter.Listener {

    var rcAdapter : RcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var cassettes : MutableList<Cassettes> = mutableListOf()

        var cassettes1 = Cassettes(title = "CAD 100",dispense = Dispense("0",true))
        var cassettes2 = Cassettes(title = "CAD 700")
        var cassettes3 = Cassettes(title = "CAD 200")
        var cassettes4 = Cassettes(title = "CAD 4200")
        var cassettes5 = Cassettes(title = "CAD 3200")

        cassettes.add(cassettes1)
        cassettes.add(cassettes2)
        cassettes.add(cassettes3)
        cassettes.add(cassettes4)
        cassettes.add(cassettes5)

        recyclerView.init()
        rcAdapter = RcAdapter(this@MainActivity,cassettes,listener = this@MainActivity)
        recyclerView.setItemViewCacheSize(cassettes.size)
        recyclerView.adapter = rcAdapter


        ///btn set focus
        btn_request_focus.setOnClickListener {
            rcAdapter?.list?.forEach { Log.d("LOG_TAG", "${it.dispense.dispenseAmount} , ${it.reject.rejectAmount} ") }
        }
    }

    override fun onNext(postion: Int, type: String, amount: String) {
        rcAdapter?.updateFocus(postion+1,type)

    }
}

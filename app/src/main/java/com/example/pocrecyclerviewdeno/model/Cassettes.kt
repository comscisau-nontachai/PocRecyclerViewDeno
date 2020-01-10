package com.example.pocrecyclerviewdeno.model

class Cassettes(
    var currency : Int = 0,
    var title : String = "",
    var dispense : Dispense = Dispense(),
    var reject  : Reject = Reject(),
    var remainAmount : Int = 0
)
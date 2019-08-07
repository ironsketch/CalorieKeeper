package com.hackercat.co.caloriekeeper

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_entry_activity.*
import kotlinx.android.synthetic.main.content_add.*
import java.text.SimpleDateFormat
import java.util.*

class AddEntryActivity() : AppCompatActivity() {
    var backPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_entry_activity)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        dateedittext.setText(currentDate.toString())
        fab2.setOnClickListener { view ->
            if(wrongDate()) {
                finish()
            } else {
                dateedittext.setBackgroundColor(resources.getColor(R.color.error))
            }
        }
    }
    override fun finish() {
        // Prepare data intent
        val data = Intent()
        data.putExtra("date", dateedittext.text.toString())
        data.putExtra("food", foodedittext.text.toString())
        data.putExtra("calories", caloriesedittext.text.toString())
        // Activity finished ok, return the data
        if(!backPressed) {
            setResult(0, data)
        } else {
            setResult(666, data)
        }
        super.finish()
    }
    override fun onBackPressed() {
        backPressed = true
        finish()
    }

    fun wrongDate(): Boolean{
        var notWrong = true
        val tmp = dateedittext.text.toString()
        if(tmp.length == 10) {
            notWrong = between0and9(tmp[0]) && between0and9(tmp[1])
                    && between0and9(tmp[3]) && between0and9(tmp[4])
                    && between0and9(tmp[6]) && between0and9(tmp[7])
                    && between0and9(tmp[8]) && between0and9(tmp[9])
        } else {
            return false
        }

        return notWrong
    }

    fun between0and9(c: Char): Boolean{
        var between = true
        return c.toInt() > 47 && c.toInt() < 58
    }
}
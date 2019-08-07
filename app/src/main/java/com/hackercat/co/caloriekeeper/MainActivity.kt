package com.hackercat.co.caloriekeeper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.widget.*
import kotlinx.android.synthetic.main.content_main.*
import android.view.Gravity
import android.view.View


class MainActivity : AppCompatActivity() {
    val dbh: DBH = DBH(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayStuff()
        fab.setOnClickListener { view ->
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivityForResult(intent, 0);
        }
    }

    fun displayStuff(){
        var outSideDate = ""
        var dateChanged: Boolean = true
        var firstDay: Boolean = true
        all.removeAllViewsInLayout()
        var arr: ArrayList< ArrayList<String>> = dbh.getall()
        if(arr != null){
            var currDate: String = ""
            var totalCount: Int = 0
            for(i in (arr.size - 1) downTo 0) {

                currDate = arr[i][1]
                if(currDate != outSideDate) {
                    dateChanged = true
                    outSideDate = currDate
                }

                if(dateChanged) {

                    // Calories Total
                    var tmpCalorieTotal = TextView(this)
                    tmpCalorieTotal.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    tmpCalorieTotal.textSize = 24f
                    tmpCalorieTotal.setPadding(10, 20, 10, 20)
                    tmpCalorieTotal.setTextColor(resources.getColor(R.color.colorPrimaryLight))
                    tmpCalorieTotal.setBackgroundColor(resources.getColor(R.color.other))
                    tmpCalorieTotal.gravity = Gravity.RIGHT
                    tmpCalorieTotal.text = totalCount.toString()

                    // Date of new day
                    var tmpDate = TextView(this)
                    tmpDate.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    tmpDate.textSize = 24f
                    tmpDate.setPadding(10, 20, 10, 20)
                    tmpDate.setTextColor(resources.getColor(R.color.colorPrimaryLight))
                    tmpDate.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                    tmpDate.text = arr[i][1]
                    if(totalCount > 0)
                        all.addView(tmpCalorieTotal)
                    all.addView(tmpDate)

                    dateChanged = false
                    totalCount = 0
                }

                // Entry LinearLayout
                var tmpEntry = RelativeLayout(this)
                tmpEntry.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                tmpEntry.setBackgroundColor(resources.getColor(R.color.other))
                tmpEntry.setPadding(30, 0, 30, 0)

                // Onclick
                tmpEntry.setOnClickListener {
                    var colorARGB = (tmpEntry.background as ColorDrawable).color
                    if(colorARGB == resources.getColor(R.color.colorAccent)){
                        dbh.deleteEntry(arr[i][0])
                        displayStuff()
                    }
                    tmpEntry.setBackgroundColor(resources.getColor(R.color.colorAccent))
                }

                tmpEntry.setOnLongClickListener {
                    tmpEntry.setBackgroundColor(resources.getColor(R.color.other))
                    return@setOnLongClickListener true
                }

                // Food TextView
                var tmpFood = TextView(this)
                tmpFood.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                tmpFood.textSize = 16f
                tmpFood.setPadding(10, 20, 10, 20)
                tmpFood.setTextColor(resources.getColor(R.color.colorPrimaryLight))
                tmpFood.text = arr[i][2]

                // Calories TextView
                var tmpCalories = TextView(this)
                tmpCalories.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { addRule(RelativeLayout.ALIGN_PARENT_RIGHT) }
                tmpCalories.textSize = 16f
                tmpCalories.setPadding(10, 20, 10, 20)
                tmpCalories.setRight(1)
                tmpCalories.setTextColor(resources.getColor(R.color.colorPrimaryLight))
                tmpCalories.text = arr[i][3]
                tmpEntry.addView(tmpFood); tmpEntry.addView(tmpCalories)
                all.addView(tmpEntry)
                totalCount += arr[i][3].toInt()

            }


            // Calories Total
            var tmpCalorieTotal = TextView(this)
            tmpCalorieTotal.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            tmpCalorieTotal.textSize = 24f
            tmpCalorieTotal.setPadding(10, 20, 10, 20)
            tmpCalorieTotal.setTextColor(resources.getColor(R.color.colorPrimaryLight))
            tmpCalorieTotal.setBackgroundColor(resources.getColor(R.color.other))
            tmpCalorieTotal.text = totalCount.toString()
            tmpCalorieTotal.gravity = Gravity.RIGHT
            all.addView(tmpCalorieTotal)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 0 && requestCode == 0) {
            val date = data!!.getStringExtra("date")
            val food = data.getStringExtra("food")
            val calories = data.getStringExtra("calories")
            dbh.addEntry(date, food, calories)
            displayStuff()

        }
    }
}

package com.hackercat.co.caloriekeeper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DBH(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null, DATABASE_VERSION) {
    var Lock = "dblock"
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "CalorieKeeper"
        private val TABLE_Entries = "EntriesTable"
        private val KEY_UID = "uid"
        private val KEY_DATE = "date"
        private val KEY_NAME = "name"
        private val KEY_CALORIES = "calories"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CATS_TABLE = ("CREATE TABLE " + TABLE_Entries + "("
                + KEY_UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT,"
                + KEY_NAME + " VARYING CHARACTER(100)," + KEY_CALORIES + " INTEGER" + ")")
        db?.execSQL(CREATE_CATS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_Entries)
        onCreate(db)
    }

    fun addEntry(date: String, name: String, calories: String):Long{
        synchronized(Lock) {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_DATE, date)
            contentValues.put(KEY_NAME, name)
            contentValues.put(KEY_CALORIES, calories.toInt())

            // Inserting Row
            val success = db.insert(TABLE_Entries, null, contentValues)
            //2nd argument is String containing nullColumnHack
            db.close() // Closing database connection
            return success
        }
    }

    fun updateEntry(uid: Int, date: String, name: String, calories: String):Int{
        synchronized(Lock) {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_UID, uid)
            contentValues.put(KEY_DATE, date)
            contentValues.put(KEY_NAME, name)
            contentValues.put(KEY_CALORIES, calories.toInt())

            // Updating Row
            val success = db.update(TABLE_Entries, contentValues, "uid=" + uid, null)
            //2nd argument is String containing nullColumnHack
            db.close() // Closing database connection
            return success
        }
    }

    fun deleteEntry(uid: String):Int{
        synchronized(Lock) {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_UID, uid.toInt()) // EmpModelClass UserId
            // Deleting Row
            val success = db.delete(TABLE_Entries, "uid=" + uid, null)
            //2nd argument is String containing nullColumnHack
            db.close() // Closing database connection
            return success
        }
    }
    fun getall():ArrayList< ArrayList<String>> {
        synchronized(Lock) {
            val entries: ArrayList< ArrayList<String>> = ArrayList< ArrayList<String>>()

            val selectQuery = "SELECT uid, date, name, calories FROM $TABLE_Entries"
            val db = this.readableDatabase
            var cursor: Cursor?

            try {
                cursor = db.rawQuery(selectQuery, null)
            } catch (e: SQLiteException) {
                db.execSQL(selectQuery)
                return entries
            }

            var uid: String
            var date: String
            var name: String
            var calories: String

            if (cursor.moveToFirst()) {
                do {
                    uid = (cursor.getString(cursor.getColumnIndex("uid")))
                    date = (cursor.getString(cursor.getColumnIndex("date")))
                    name = (cursor.getString(cursor.getColumnIndex("name")))
                    calories = (cursor.getString(cursor.getColumnIndex("calories")))
                    var tmp: ArrayList<String> = ArrayList<String>()
                    tmp.add(uid); tmp.add(date); tmp.add(name); tmp.add(calories)
                    entries.add(tmp)
                } while (cursor.moveToNext())
            }
            return entries
        }
    }
}
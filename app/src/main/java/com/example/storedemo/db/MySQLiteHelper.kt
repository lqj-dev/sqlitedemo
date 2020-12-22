package com.example.storedemo.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySQLiteHelper(val context: Context,name:String,version: Int)
    :SQLiteOpenHelper(context,name,null,version) {
    val createBook="create table tb_book("+
            "id integer primary key autoincrement,"+
            "bookname text,"+
            "author text,"+
            "price real,"+
            "pages integer)"
    val createCategory="create table tb_category("+
            "id integer primary key autoincrement,"+
            "categoryname text,"+
            "parentid integer)"
    val createContact="create table tb_contact("+
            "id integer primary key autoincrement,"+
            "displayname text,"+
            "number text)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createBook)
        db?.execSQL(createCategory)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion<=1){
            db?.execSQL(createContact)
        }
    }

}
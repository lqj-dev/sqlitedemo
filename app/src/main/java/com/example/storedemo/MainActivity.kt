package com.example.storedemo

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.storedemo.db.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var createDB = findViewById<Button>(R.id.button)
        createDB.setOnClickListener {
            var dbHelper = MySQLiteHelper(this,"bookstore.db",1)
            var db = dbHelper.writableDatabase
            val v1=ContentValues().apply {
                put("bookname","Android第一行代码")
                put("author","郭霖")
                put("price",99)
                put("pages",654)
            }
            db.insert("tb_book",null,v1)
            val v2=ContentValues().apply {
                put("bookname","Java EE Developing")
                put("author","black horse")
                put("price",54)
                put("pages",345)
            }
            db.insert("tb_book",null,v2)
            db.close()
        }
        button2.setOnClickListener {
            var dbHelper = MySQLiteHelper(this,"bookstore.db",1)
            var db = dbHelper.writableDatabase
            val v1=ContentValues().apply {
                put("price",77)
            }
            db.update("tb_book",v1,"bookname=?", arrayOf("Java EE Developing"))
            db.close()
        }
        button3.setOnClickListener {
            var dbHelper = MySQLiteHelper(this,"bookstore.db",1)
            var db = dbHelper.writableDatabase
            db.delete("tb_book","id=?", arrayOf("1"))
            db.close()
        }
        button4.setOnClickListener {
            val buffer=StringBuffer()
            val dbHelper = MySQLiteHelper(this,"bookstore.db",1)
            val db = dbHelper.writableDatabase
            val cursor = db.query(false, "tb_book", arrayOf("bookname,author,price"), null, null, null, null, null, null)
            if(cursor.moveToFirst()){
                do{
                    val bookname = cursor.getString(cursor.getColumnIndex("bookname"))
                    val author = cursor.getString(cursor.getColumnIndex("author"))
                    buffer.append(bookname)
                    buffer.append("-")
                    buffer.append(author)
                    buffer.append("\n")
                }while (cursor.moveToNext())
            }
            Toast.makeText(this,buffer.toString(),Toast.LENGTH_LONG).show()
            db.close()
        }
        button5.setOnClickListener {
            val buffer=StringBuffer()
            val db = MySQLiteHelper(this, "bookstore.db", 1).writableDatabase
            /**
             * 插入数据
             *
             */
            db.execSQL("insert into tb_book(bookname,author,price,pages) values(?,?,?,?)",
                    arrayOf("javascript","张三",56,654))
            /**
             * 更新数据，使用update
             */
            db.execSQL("update tb_book set author=? where bookname=?",arrayOf("李四","javascript"))
            /**
             * 删除数据
             */
            db.execSQL("delete from tb_book where id=?", arrayOf(2))
            /**
             * 查询数据
             */
            var cursor = db.rawQuery("select * from tb_book where id>?", arrayOf("3"))
            /**
             * 对cursor进行遍历
             */
            if(cursor.moveToFirst()){
                do{
                    val bookname = cursor.getString(cursor.getColumnIndex("bookname"))
                    val author = cursor.getString(cursor.getColumnIndex("author"))
                    buffer.append(bookname)
                    buffer.append("-")
                    buffer.append(author)
                    buffer.append("\n")
                }while (cursor.moveToNext())
            }
            Toast.makeText(this,buffer.toString(),Toast.LENGTH_LONG).show()
            db.close()
        }
    }

}
package com.example.contentproviderdemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.contentproviderdemo.db.MySQLiteHelper
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            //动态获取权限
            if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CALL_PHONE),1)
            }else{
                call("10086")
            }
        }
        button2.setOnClickListener {
            // 动态获取权限
            if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS),2)
            }else{
                readContext()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1->{
                if(grantResults.isNotEmpty()&&
                        grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    call("10086")
                }else{
                    Toast.makeText(this,"You denied thie permission", Toast.LENGTH_LONG).show()
                }
            }
            2->{
                if(grantResults.isNotEmpty()&&
                    grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    readContext()
                }else{
                    Toast.makeText(this,"You denied thie permission", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun call(number:String){
        try{
            val intent = Intent(Intent.ACTION_CALL)
            intent.data= Uri.parse("tel:${number}")
            startActivity(intent)
        }catch (e:SecurityException){
            e.printStackTrace()
        }
    }

    private fun readContext() {
        val context=this
        val db = MySQLiteHelper(context,"bookstore.db",1).writableDatabase
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,null,null,null)?.apply {
            //
            while (moveToNext()){
                val displayName =
                    getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number =
                    getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                db.execSQL("insert into tb_contact(displayname,number) values(?,?)",arrayOf(displayName,number))
            }
        }
        val cursor = db.rawQuery("select * from tb_contact", null)
        val buffer=StringBuffer()
        if(cursor.moveToFirst()){
            do{
                buffer.append(cursor.getString(cursor.getColumnIndex("displayname")))
                buffer.append(":")
                buffer.append(cursor.getString(cursor.getColumnIndex("number")))
                buffer.append("\n")
            }while (cursor.moveToNext())
        }
        Toast.makeText(this,buffer.toString(),Toast.LENGTH_LONG).show()
    }
}
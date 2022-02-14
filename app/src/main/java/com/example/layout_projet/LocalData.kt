package com.example.layout_projet

import android.app.Activity
import android.content.Context
import org.json.JSONObject
import java.io.*

//classe réalisée à l'aide de la page web:
//http://blog.qanbio.com/tech/2017/07/18/stocker-les-donnees-en-local-pour-une-application-android.html

class LocalData {
    fun saveData(test: Activity, jsonObject: JSONObject){

        val string = jsonObject.toString()

        val fileName = "Preferences"
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = test.openFileOutput(fileName, Context.MODE_PRIVATE)
            fileOutputStream.write(string.toByteArray())
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun readData(test: Activity): JSONObject {
        try {
            val fileInputStream: FileInputStream? = test.openFileInput("Preferences")
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String?
            while (run {
                    text = bufferedReader.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }

            return JSONObject(stringBuilder.toString())
            
        } catch (e: FileNotFoundException) {
            val jsonObject = JSONObject()
            jsonObject.put("Pseudo", "")
            return jsonObject
        }

    }
}
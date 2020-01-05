package br.com.mrocigno.usefulthings_lib_kotlin.utils

import android.util.Log
import java.lang.StringBuilder

object ToolBox {

    fun checkBrCpf(cpf : String) : Boolean{
        if(cpf.trim().isEmpty()) return false

        try {
            val splited = cpf.split("-")
            val body = StringBuilder(splited[0].replace(".", ""))
            val digit = splited[1]

            var digit1 = 0
            var multiplier = 10

            for(x in body.toString()){
                val value = x.toString().toInt()
                digit1 += value*multiplier--
            }
            digit1 = (11 - (digit1%11))
            digit1 = if(digit1 > 9) 0 else digit1
            body.append(digit1)

            var digit2 = 0
            multiplier = 11
            for(x in body.toString()){
                val value = x.toString().toInt()
                digit2 += value*multiplier--
            }
            digit2 = (11 - (digit2%11))
            digit2 = if(digit2 > 9) 0 else digit2

            return "$digit1$digit2" == digit

        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


}
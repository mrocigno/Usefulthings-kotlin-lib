package br.com.mrocigno.usefulthings_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.mrocigno.usefulthings_lib_kotlin.utils.ToolBox
import kotlinx.android.synthetic.main.activity_demo.*

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        button.setOnClickListener {
            Log.d("DEBUG.TEST", "${vedt1.validate()}")
            Log.d("DEBUG.TEST", "${vedt2.validate()}")
        }
    }
}

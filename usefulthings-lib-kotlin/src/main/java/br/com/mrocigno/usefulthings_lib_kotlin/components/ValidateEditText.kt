package br.com.mrocigno.usefulthings_lib_kotlin.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.Log
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import br.com.mrocigno.usefulthings_lib_kotlin.R
import br.com.mrocigno.usefulthings_lib_kotlin.utils.ToolBox

class ValidateEditText : AppCompatEditText {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    var mask = ""
    var cpf = false
    var compareWith = -1
    var errorMsg = ""
    var required = false
    var minLength = -1
    var showErrors = true

    @SuppressLint("CustomViewStyleable")
    fun init(attrs: AttributeSet? = null) {

        val ta = attrs?.run {
            context.obtainStyledAttributes(
                attrs,
                R.styleable.ValidateEditText
            )
        }

        this.mask = ta?.getString(R.styleable.ValidateEditText_mask) ?: ""
        this.cpf = ta?.getBoolean(R.styleable.ValidateEditText_validate_cpf, false) ?: false
        this.compareWith = ta?.getResourceId(R.styleable.ValidateEditText_compare_with, -1) ?: -1
        this.required = ta?.getBoolean(R.styleable.ValidateEditText_required, false) ?: false
        this.minLength = ta?.getInt(R.styleable.ValidateEditText_min_length, -1) ?: -1
        this.showErrors = ta?.getBoolean(R.styleable.ValidateEditText_show_errors, true) ?: true
        this.errorMsg = ta?.getString(R.styleable.ValidateEditText_error_msg) ?: ""

        if (cpf) {
            this.mask = "###.###.###-##"
            keyListener = DigitsKeyListener.getInstance("1234567890-.")
        }

        if(mask.isNotEmpty()){
            this.addTextChangedListener(textWatcher)
            val filters = arrayOf(
                InputFilter.LengthFilter(mask.length)
            )
            this.filters = filters
        }

        Log.d("DEBUG.TEST", "$mask, $cpf, $compareWith, $required, $minLength, $errorMsg")

    }


    fun validate(): Boolean {
        if (required && text.toString().trim().isEmpty()) {
            showError(context.getString(R.string.validateEditText_required_field_err_msg))
            return false
        } else {
            clearError()
        }

        if (cpf && !ToolBox.checkBrCpf(text.toString().trim())) {
            showError("CPF Inválido")
            return false
        } else {
            clearError()
        }

        if (minLength != -1 && text.toString().trim().length < minLength) {
            showError(context.getString(R.string.validateEditText_min_length_err_msg))
            return false
        } else {
            clearError()
        }

        if (compareWith != -1 && !compare()) {
            showError(context.getString(R.string.validateEditText_match_err_msg))
            return false
        } else {
            clearError()
        }

        return true
    }

    private fun compare(): Boolean {
        return if (compareWith != -1) {
            val edtCompare = (context as Activity).findViewById<EditText>(compareWith)
            val textCompare = edtCompare.text.toString().trim()
            val text = this.text.toString().trim()

            text == textCompare
        } else {
            false
        }
    }

    private fun showError(message: String, where: EditText = this) {
        if (showErrors) {
            where.error =
                if (errorMsg.isEmpty())
                    message
                else
                    errorMsg
        }
    }

    private fun clearError(where: EditText = this) {
        where.error = null
    }

    fun unmask(): String {
        return this.text.toString().trim().replace(
            "[.]|[-]|[/]|[(]|[ ]|[:]|[)]".toRegex(),
            ""
        )
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        var before = -1
        var selfChange = false

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(se: CharSequence, start: Int, before: Int, after: Int) {}

        override fun afterTextChanged(s: Editable) {
            if(selfChange) return
            selfChange = true
            edit(s)
            selfChange = false
        }

        private fun edit(s: Editable) {
            if(selfChange){
                val count = s.length - 1
                Log.d("DEBUG.TEST", "before: $before, after: $count")
                if(before < count){
                    var maskChar = if(count + 1 < mask.length){
                         mask[count + 1]
                    } else {
                        mask[count]
                    }

                    var x = 1
                    while(maskChar != '#'){
                        s.append(maskChar)
                        maskChar = mask[count + ++x]
                    }
                } else {
                    var maskChar = if(count < 0){
                        mask[count + 1]
                    } else {
                        mask[count]
                    }
                    var x = 0
                    while(maskChar != '#'){
                        s.delete(s.length - 1, s.length)
                        maskChar = mask[count - ++x]
                    }
                }
                before = count
            }
        }


    }

}

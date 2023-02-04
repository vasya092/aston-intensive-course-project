package com.example.astoncourseproject.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.annotation.ArrayRes
import androidx.appcompat.widget.AppCompatSpinner
import com.example.astoncourseproject.presentation.models.FilterItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URI

const val DEBOUNCE_DELAY_MS: Long = 300

fun EditText.onDebouncedChange(scope: CoroutineScope, textChanged: ((String) -> Unit)) {
    this.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            scope.launch {
                delay(DEBOUNCE_DELAY_MS)
                textChanged.invoke(s.toString())
            }
        }
    })
}

fun AppCompatSpinner.fillFromArray(context: Context, @ArrayRes arrayId: Int) {
    ArrayAdapter.createFromResource(
        context,
        arrayId,
        android.R.layout.simple_spinner_item
    ).also { arrayAdapter ->
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.adapter = arrayAdapter
    }
}

fun AppCompatSpinner.getFilterItem() : FilterItem {
    return FilterItem(
        this.selectedItemId,
        this.selectedItem.toString(),
        this.selectedItemPosition
    )
}

fun URI.findParameterValue(parameterName: String): String? {
    return rawQuery.split('&').map {
        val parts = it.split('=')
        val name = parts.firstOrNull() ?: ""
        val value = parts.drop(1).firstOrNull() ?: ""
        Pair(name, value)
    }.firstOrNull { it.first == parameterName }?.second
}

fun String.getIdByUrlString(parameterName: String): String? {
    return this.substringAfterLast("$parameterName/")
}

fun View.showSnackBar(text: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this,text, duration).show()
}
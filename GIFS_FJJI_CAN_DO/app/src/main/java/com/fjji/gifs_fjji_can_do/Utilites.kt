package com.fjji.gifs_fjji_can_do

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import com.fjji.gifs_fjji_can_do.API.model.Gif
import com.fjji.gifs_fjji_can_do.API.model.GifCategories
import java.io.Serializable

// Data class that represents the list of to-do items
data class CategoryItem(var categories: GifCategories): Serializable {}

data class subcategory(var GifSucategory: String) :Serializable {}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
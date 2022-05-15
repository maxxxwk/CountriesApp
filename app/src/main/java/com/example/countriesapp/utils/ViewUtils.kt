package com.example.countriesapp.utils

import android.view.View

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun hide(vararg views: View) {
    views.forEach {
        it.hide()
    }
}

fun show(vararg views: View) {
    views.forEach {
        it.show()
    }
}

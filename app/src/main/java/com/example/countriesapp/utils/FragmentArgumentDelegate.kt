package com.example.countriesapp.utils

import androidx.fragment.app.Fragment

fun Fragment.stringArgument(key: String, defaultValue: String? = null): Lazy<String> {
    return lazy {
        checkNotNull(arguments) { "Arguments is absent!" }.apply { }
        defaultValue?.let {
            arguments!!.getString(key, it)
        } ?: checkNotNull(arguments!!.getString(key)) { "Argument with key=$key is absent!" }
    }
}

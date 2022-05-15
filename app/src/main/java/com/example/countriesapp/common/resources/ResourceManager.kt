package com.example.countriesapp.common.resources

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceManager @Inject constructor(private val context: Context) {

    fun getString(@StringRes id: Int) = context.getString(id)

    fun getString(@StringRes id: Int, vararg formatArgs: Any) = context.getString(id, formatArgs)
}

package com.nicoapps.cooktime

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltAndroidApp
class CookTimeApplication : Application() {
    @Inject
    @ApplicationContext
    // TODO: delete when https://github.com/google/dagger/issues/3601 is resolved.
    lateinit var context: Context
}
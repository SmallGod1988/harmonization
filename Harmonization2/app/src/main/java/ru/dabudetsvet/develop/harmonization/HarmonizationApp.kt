package ru.dabudetsvet.develop.harmonization

import android.app.Application
import ru.dabudetsvet.develop.harmonization.data.HarmonizationDatabase
import ru.dabudetsvet.develop.harmonization.data.HarmonizationRepository

class HarmonizationApp : Application() {

    val repository: HarmonizationRepository by lazy {
        HarmonizationRepository(HarmonizationDatabase.getInstance(this))
    }
}

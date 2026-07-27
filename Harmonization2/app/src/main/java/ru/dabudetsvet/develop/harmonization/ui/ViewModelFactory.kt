package ru.dabudetsvet.develop.harmonization.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.dabudetsvet.develop.harmonization.HarmonizationApp
import ru.dabudetsvet.develop.harmonization.data.HarmonizationRepository

/** Builds a [ViewModel] wired to the app-wide [HarmonizationRepository] singleton. */
@Composable
inline fun <reified VM : ViewModel> harmonizationViewModel(
    crossinline creator: (HarmonizationRepository) -> VM
): VM {
    val repository = (LocalContext.current.applicationContext as HarmonizationApp).repository
    return viewModel(
        factory = viewModelFactory {
            initializer { creator(repository) }
        }
    )
}

package com.keremkulac.okeyscore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.keremkulac.okeyscore.ui.finishedGame.FinishedGameAdapter
import com.keremkulac.okeyscore.ui.finishedGame.FinishedGameFragment
import javax.inject.Inject

class DefaultFragmentFactory @Inject constructor(
    private val finishedGameAdapter: FinishedGameAdapter
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        when (loadFragmentClass(classLoader, className)) {
            FinishedGameFragment::class.java -> FinishedGameFragment(finishedGameAdapter)
            else -> super.instantiate(classLoader, className)
        }
}
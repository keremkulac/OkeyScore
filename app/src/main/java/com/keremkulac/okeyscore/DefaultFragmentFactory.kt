package com.keremkulac.okeyscore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.keremkulac.okeyscore.presentation.ui.finishedPartnerGame.FinishedPartnerGameFragment
import com.keremkulac.okeyscore.presentation.ui.finishedSingleGame.FinishedSingleGameFragment
import javax.inject.Inject

class DefaultFragmentFactory @Inject constructor(
    ) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        when (loadFragmentClass(classLoader, className)) {
            FinishedPartnerGameFragment::class.java -> FinishedPartnerGameFragment()
            FinishedSingleGameFragment::class.java -> FinishedSingleGameFragment()
            else -> super.instantiate(classLoader, className)
        }
}
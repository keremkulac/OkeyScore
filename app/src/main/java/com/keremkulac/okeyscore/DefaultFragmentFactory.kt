package com.keremkulac.okeyscore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.keremkulac.okeyscore.ui.finishedPartnerGame.FinishedPartnerGameAdapter
import com.keremkulac.okeyscore.ui.finishedPartnerGame.FinishedPartnerGameFragment
import com.keremkulac.okeyscore.ui.finishedPartnerGameDetail.FinishedPartnerGameDetailAdapter
import com.keremkulac.okeyscore.ui.finishedPartnerGameDetail.FinishedGameDetailFragment
import javax.inject.Inject

class DefaultFragmentFactory @Inject constructor(
    private val finishedPartnerGameAdapter: FinishedPartnerGameAdapter,
    private val finishedPartnerGameDetailAdapter: FinishedPartnerGameDetailAdapter
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        when (loadFragmentClass(classLoader, className)) {
            FinishedPartnerGameFragment::class.java -> FinishedPartnerGameFragment(finishedPartnerGameAdapter)
            FinishedGameDetailFragment::class.java -> FinishedGameDetailFragment(finishedPartnerGameDetailAdapter)
            else -> super.instantiate(classLoader, className)
        }
}
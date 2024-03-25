package com.keremkulac.okeyscore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.keremkulac.okeyscore.presentation.ui.finishedPartnerGame.FinishedPartnerGameFragment
import com.keremkulac.okeyscore.presentation.ui.finishedPartnerGameDetail.FinishedPartnerGameDetailAdapter
import com.keremkulac.okeyscore.presentation.ui.finishedPartnerGameDetail.FinishedPartnerGameDetailFragment
import com.keremkulac.okeyscore.presentation.ui.finishedSingleGame.FinishedSingleGameFragment
import com.keremkulac.okeyscore.presentation.ui.finishedSingleGameDetail.FinishedSingleGameDetailAdapter
import com.keremkulac.okeyscore.presentation.ui.finishedSingleGameDetail.FinishedSingleGameDetailFragment
import javax.inject.Inject

class DefaultFragmentFactory @Inject constructor(
    private val finishedPartnerGameDetailAdapter: FinishedPartnerGameDetailAdapter,
    private val finishedSingleGameDetailAdapter: FinishedSingleGameDetailAdapter
    ) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        when (loadFragmentClass(classLoader, className)) {
            FinishedPartnerGameFragment::class.java -> FinishedPartnerGameFragment()
            FinishedPartnerGameDetailFragment::class.java -> FinishedPartnerGameDetailFragment(finishedPartnerGameDetailAdapter)
            FinishedSingleGameFragment::class.java -> FinishedSingleGameFragment()
            FinishedSingleGameDetailFragment::class.java -> FinishedSingleGameDetailFragment(finishedSingleGameDetailAdapter)
            else -> super.instantiate(classLoader, className)
        }
}
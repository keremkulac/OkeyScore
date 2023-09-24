package com.keremkulac.okeyscore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.keremkulac.okeyscore.ui.finishedGame.FinishedGameAdapter
import com.keremkulac.okeyscore.ui.finishedGame.FinishedGameFragment
import com.keremkulac.okeyscore.ui.finishedGameDetail.FinishedDetailGameAdapter
import com.keremkulac.okeyscore.ui.finishedGameDetail.FinishedGameDetailFragment
import javax.inject.Inject

class DefaultFragmentFactory @Inject constructor(
    private val finishedGameAdapter: FinishedGameAdapter,
    private val finishedGameDetailAdapter: FinishedDetailGameAdapter
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
        when (loadFragmentClass(classLoader, className)) {
            FinishedGameFragment::class.java -> FinishedGameFragment(finishedGameAdapter)
            FinishedGameDetailFragment::class.java -> FinishedGameDetailFragment(finishedGameDetailAdapter)
            else -> super.instantiate(classLoader, className)
        }
}
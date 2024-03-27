package com.keremkulac.okeyscore.domain.use_case.get_finished_partner_game

import com.keremkulac.okeyscore.domain.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import javax.inject.Inject

class GetFinishedPartnerGameUseCase @Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) {
    suspend operator fun invoke(id : Int) : FinishedPartnerGame {
        return okeyScoreRepository.getFinishedPartnerGame(id)
    }
}
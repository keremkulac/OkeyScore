package com.keremkulac.okeyscore.domain.use_case.delete_finished_partner_game

import com.keremkulac.okeyscore.domain.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import javax.inject.Inject

class DeleteFinishedPartnerGameUseCase @Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) {
    suspend operator fun  invoke(finishedPartnerGame : FinishedPartnerGame) {
        okeyScoreRepository.deleteFinishedPartnerGame(finishedPartnerGame)
    }
}
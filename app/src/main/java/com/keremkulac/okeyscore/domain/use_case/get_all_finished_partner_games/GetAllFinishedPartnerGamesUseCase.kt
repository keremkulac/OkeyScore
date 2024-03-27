package com.keremkulac.okeyscore.domain.use_case.get_all_finished_partner_games

import com.keremkulac.okeyscore.domain.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import javax.inject.Inject

class GetAllFinishedPartnerGamesUseCase @Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) {
    suspend operator fun invoke() : List<FinishedPartnerGame>{
        return okeyScoreRepository.getAllFinishedPartnerGames()
    }
}
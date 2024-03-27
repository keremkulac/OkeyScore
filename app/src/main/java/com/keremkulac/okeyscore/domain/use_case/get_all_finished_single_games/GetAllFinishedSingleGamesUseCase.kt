package com.keremkulac.okeyscore.domain.use_case.get_all_finished_single_games

import com.keremkulac.okeyscore.domain.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedSingleGame
import javax.inject.Inject

class GetAllFinishedSingleGamesUseCase @Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) {
    suspend operator fun invoke() : List<FinishedSingleGame>{
        return okeyScoreRepository.getAllFinishedSingleGames()
    }
}
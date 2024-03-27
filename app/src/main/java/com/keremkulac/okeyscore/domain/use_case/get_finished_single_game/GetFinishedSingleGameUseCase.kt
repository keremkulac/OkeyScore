package com.keremkulac.okeyscore.domain.use_case.get_finished_single_game

import com.keremkulac.okeyscore.domain.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedSingleGame
import javax.inject.Inject

class GetFinishedSingleGameUseCase @Inject constructor(private val okeyScoreRepository: OkeyScoreRepository) {
    suspend operator fun invoke(id : Int) : FinishedSingleGame{
       return okeyScoreRepository.getFinishedSingleGame(id)
    }
}
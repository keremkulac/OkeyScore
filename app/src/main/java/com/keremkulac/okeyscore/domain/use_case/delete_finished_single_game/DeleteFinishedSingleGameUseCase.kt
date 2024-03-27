package com.keremkulac.okeyscore.domain.use_case.delete_finished_single_game

import com.keremkulac.okeyscore.domain.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedSingleGame
import javax.inject.Inject

class DeleteFinishedSingleGameUseCase @Inject constructor(private val okeyScoreRepository: OkeyScoreRepository){
    suspend operator fun  invoke(finishedSingleGame: FinishedSingleGame){
        okeyScoreRepository.deleteFinishedSingleGame(finishedSingleGame)
    }
}
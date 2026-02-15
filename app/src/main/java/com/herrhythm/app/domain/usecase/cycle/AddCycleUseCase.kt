package com.herrhythm.app.domain.usecase.cycle

import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.domain.repository.CycleRepository
import javax.inject.Inject

class AddCycleUseCase @Inject constructor(
    private val cycleRepository: CycleRepository
) {
    suspend operator fun invoke(cycle: Cycle): Long {
        return cycleRepository.insertCycle(cycle)
    }
}

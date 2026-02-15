package com.herrhythm.app.domain.usecase.cycle

import com.herrhythm.app.domain.model.Cycle
import com.herrhythm.app.domain.repository.CycleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCyclesUseCase @Inject constructor(
    private val cycleRepository: CycleRepository
) {
    operator fun invoke(): Flow<List<Cycle>> {
        return cycleRepository.getAllCycles()
    }
}

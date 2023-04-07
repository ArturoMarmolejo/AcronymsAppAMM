package com.arturomarmolejo.acronymsappam.usecases

import com.arturomarmolejo.acronymsappam.model.AcromineItem
import com.arturomarmolejo.acronymsappam.rest.AcromineRepository
import com.arturomarmolejo.acronymsappam.rest.AcromineServiceApi
import com.arturomarmolejo.acronymsappam.utils.FailureResponse
import com.arturomarmolejo.acronymsappam.utils.NullAcronymListResponse
import com.arturomarmolejo.acronymsappam.utils.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAcronymsUseCase @Inject constructor(
    private val acromineServiceApi: AcromineServiceApi
) {
    operator fun invoke(sf: String? = null, lf: String? = null): Flow<UIState<List<AcromineItem>>> = flow {
        emit(UIState.LOADING)
        try {
            val response = acromineServiceApi.getAcronymList(sf, lf)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(UIState.SUCCESS(it))
                } ?: throw NullAcronymListResponse()
            } else {
                throw FailureResponse(response.errorBody()?.string())
            }
        } catch (e: Exception) {
            emit(UIState.ERROR(e))
        }
    }
}
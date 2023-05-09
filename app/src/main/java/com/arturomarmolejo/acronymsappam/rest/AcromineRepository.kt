package com.arturomarmolejo.acronymsappam.rest

import com.arturomarmolejo.acronymsappam.model.AcromineItem
import com.arturomarmolejo.acronymsappam.usecases.GetAcronymsUseCase
import com.arturomarmolejo.acronymsappam.utils.FailureResponse
import com.arturomarmolejo.acronymsappam.utils.NullAcronymListResponse
import com.arturomarmolejo.acronymsappam.utils.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


import javax.inject.Inject

interface AcromineRepository {

    fun getAllAcronyms(sf: String? = null, lf: String? = null): Flow<UIState<MutableList<AcromineItem>>>

}

class AcromineRepositoryImpl @Inject constructor(
    private val acromineServiceApi: AcromineServiceApi,
): AcromineRepository {

    override fun getAllAcronyms(sf: String?, lf: String?): Flow<UIState<MutableList<AcromineItem>>> = flow {
        // getAcronymsUseCase(sf, lf)
        emit(UIState.LOADING)
        lateinit var response: Response<MutableList<AcromineItem>>
        try {
            if( sf != null){
                response = acromineServiceApi.getAcronymList(sf, lf = null)
            } else if (lf != null) {
                response = acromineServiceApi.getAcronymList(sf = null, lf)
            }
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
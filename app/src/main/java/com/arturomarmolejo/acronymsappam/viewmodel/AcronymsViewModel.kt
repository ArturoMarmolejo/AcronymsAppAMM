package com.arturomarmolejo.acronymsappam.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arturomarmolejo.acronymsappam.model.AcromineItem
import com.arturomarmolejo.acronymsappam.model.Lf
import com.arturomarmolejo.acronymsappam.rest.AcromineRepository
import com.arturomarmolejo.acronymsappam.usecases.GetAcronymsUseCase
import com.arturomarmolejo.acronymsappam.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AcronymsViewModel"
@HiltViewModel
class AcronymsViewModel @Inject constructor(
    private val acromineRepository: AcromineRepository,
    private val getAcronymsUseCase: GetAcronymsUseCase,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {
    private var isInitialized = false

    var selectedSf: String? = null
    var selectedLf: String? = null

    lateinit var selectedAcronymItem: Lf

    private val _longFormList: MutableLiveData<UIState<MutableList<AcromineItem>>> = MutableLiveData(UIState.LOADING)
    val longFormList: LiveData<UIState<MutableList<AcromineItem>>> get() = _longFormList

    fun getAllAcronyms(sf: String? = null, lf: String? = null) {
        viewModelScope.launch(ioDispatcher) {
            selectedSf = sf
            selectedLf = lf
            if(selectedSf != null) {
                acromineRepository.getAllAcronyms(sf = selectedSf).collect{ result ->
                    Log.d(TAG, "getAllAcronyms: $result, selectedSf: $selectedSf")
                    _longFormList.postValue(result)
                }
            } else if (selectedLf != null) {
                acromineRepository.getAllAcronyms(lf = selectedLf).collect{ result ->
                    Log.d(TAG, "getAllAcronyms: $result, selectedSf: $selectedSf")
                    _longFormList.postValue(result)
                }
            }

        }
    }


}
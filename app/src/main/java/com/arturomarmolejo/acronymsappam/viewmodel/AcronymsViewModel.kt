package com.arturomarmolejo.acronymsappam.viewmodel

import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.arturomarmolejo.acronymsappam.model.AcromineItem
import com.arturomarmolejo.acronymsappam.model.Lf
import com.arturomarmolejo.acronymsappam.rest.AcromineRepository
import com.arturomarmolejo.acronymsappam.usecases.GetAcronymsUseCase
import com.arturomarmolejo.acronymsappam.utils.UIState
import com.arturomarmolejo.acronymsappam.views.adapter.AcronymsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import javax.inject.Inject

private const val TAG = "AcronymsViewModel"
@HiltViewModel
class AcronymsViewModel @Inject constructor(
    private val acromineRepository: AcromineRepository,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {
    private var isInitialized = false

    var selectedSf: String? = null
    var selectedLf: String? = null

    lateinit var selectedAcronymItem: Lf

    private val _longFormList: MutableLiveData<UIState<MutableList<AcromineItem>>> = MutableLiveData(UIState.LOADING)
    val longFormList: LiveData<UIState<MutableList<AcromineItem>>> get() = _longFormList

    var searchViewContent: String = ""
    private val searchQueryChannel = Channel<String>(Channel.CONFLATED)

    fun getOnQueryTextListener(): SearchView.OnQueryTextListener{
        return object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it == searchViewContent)
                        return true
                    if (it.length > 1) {
//                        getAllAcronyms(query)
                        searchQueryChannel.trySend(it).isSuccess
                        return true
                    }
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        }
    }

    init {
        viewModelScope.launch(ioDispatcher) {
            searchQueryChannel.consumeAsFlow()
                .debounce(500)
                .distinctUntilChanged()
                .collect() { query ->
                    getAllAcronyms(query)
                }
        }
    }

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

@BindingAdapter("acronym_search_view")
fun setSearchViewAdapter(
    searchView: SearchView,
    listener: OnQueryTextListener
) {
    searchView.setOnQueryTextListener(listener)
}
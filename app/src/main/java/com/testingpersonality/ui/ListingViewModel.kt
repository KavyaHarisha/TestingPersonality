package com.testingpersonality.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.testingpersonality.data.local.PersonalityData
import com.testingpersonality.DataRepository
import com.testingpersonality.model.Question
import com.testingpersonality.utils.State
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
class ListingViewModel @ViewModelInject constructor(private val dataRepository: DataRepository) :
        ViewModel() {

    private val _questionData = MutableLiveData<State<List<Question>>>()
    val listQuestion: LiveData<State<List<Question>>>
        get() = _questionData

    private val _categoryData = MutableLiveData<List<String>>()
    val categoryList: LiveData<List<String>>
        get() = _categoryData

    init {
        fetchQuestionsData(Dispatchers.IO)
    }

    fun fetchQuestionsData(dispatcher: CoroutineDispatcher) {
        viewModelScope.launch(dispatcher) {
            dataRepository.getAllCategoriesAndQuestions().collect {
                _categoryData.postValue(dataRepository.getCategoryList())
                _questionData.postValue(it)
            }
        }
    }

    fun storeData(
        data: PersonalityData,
        dispatcher: CoroutineDispatcher
    ){
        viewModelScope.launch(dispatcher) {
            dataRepository.savePersonalityData(data)
        }
    }

    private var _personalityData = MutableLiveData<List<PersonalityData>>()
    var personalityInfo:LiveData<List<PersonalityData>> = _personalityData

    fun getData(dispatcher: CoroutineDispatcher){
        viewModelScope.launch(dispatcher) {
            val list: List<PersonalityData> = dataRepository.getPersonalityData()
            _personalityData.postValue(list)
        }
    }
}
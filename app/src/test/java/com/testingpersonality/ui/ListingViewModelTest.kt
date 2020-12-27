package com.testingpersonality.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.myapplication.model.Predicate
import com.testingpersonality.DataRepository
import com.testingpersonality.data.local.PersonalityData
import com.testingpersonality.model.*
import com.testingpersonality.utils.State
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListingViewModelTest{

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    lateinit var dataRepository: DataRepository

    @InjectMockKs
    lateinit var viewModel: ListingViewModel


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun fetch_question_data_when_state_loading()  {
        every { dataRepository.getAllCategoriesAndQuestions() } returns flowOf(State.loading())
        viewModel.fetchQuestionsData(Dispatchers.Unconfined)
        assert(viewModel.listQuestion.value!=null)
        assert(viewModel.listQuestion.value is State.Loading)
    }

    @Test
    fun fetch_question_data_when_state_success()  {
        every { dataRepository.getAllCategoriesAndQuestions() } returns flowOf(State.success(mockk()))
        viewModel.fetchQuestionsData(Dispatchers.Unconfined)
        assert(viewModel.listQuestion.value!=null)
        assert(viewModel.listQuestion.value is State.Success)
    }

    @Test
    fun fetch_question_data_when_state_error()  {
        every { dataRepository.getAllCategoriesAndQuestions() } returns flowOf(State.error(""))
        viewModel.fetchQuestionsData(Dispatchers.Unconfined)
        assert(viewModel.listQuestion.value!=null)
        assert(viewModel.listQuestion.value is State.Error)
    }

    @Test
    fun fetch_question_data_when_state_success_list_of_question()  {
        val list = listOf(Question(0,"first category","First Question?",
            QuestionType(null, listOf("first","second","third"),"single_choice")))
        every { dataRepository.getAllCategoriesAndQuestions() } returns flowOf(State.success(list))
        viewModel.fetchQuestionsData(Dispatchers.Unconfined)
        assert(viewModel.listQuestion.value!=null)
        assert(viewModel.listQuestion.value is State.Success)
        val state = viewModel.listQuestion.value as State.Success
        assert(state.data[0].category == "first category")
        assert(state.data[0].question_type.condition == null)
    }

    @Test
    fun fetch_question_data_when_state_success_list_of_question_with_condition()  {
        val list = listOf(Question(0,"first category","First Question?",
            QuestionType(null, listOf("first","second","third"),"single_choice")),
            Question(1,"first category","second Question?",
            QuestionType(Condition(IfPositive("first category","second extend question?",
                QuestionTypeX(Range(10,20),"number")), Predicate(listOf("very important"))
            ), listOf("first","second","third"),"single_choice")))
        every { dataRepository.getAllCategoriesAndQuestions() } returns flowOf(State.success(list))
        viewModel.fetchQuestionsData(Dispatchers.Unconfined)
        assert(viewModel.listQuestion.value!=null)
        assert(viewModel.listQuestion.value is State.Success)
        val state = viewModel.listQuestion.value as State.Success
        assert(state.data[0].category == "first category")
        assert(state.data[0].question_type.condition == null)
        assert(state.data[1].question_type.condition != null)
        val condition = state.data[1].question_type.condition
        assert(condition!!.if_positive.question == "second extend question?")
        assert(condition.predicate.exactEquals.contains("very important"))
    }

    @Test
    fun fetch_question_data_when_state_success_of_category_items()  {
        val categories = listOf("first category", "second category","third category")
        every { dataRepository.getAllCategoriesAndQuestions() } returns flowOf(State.success(mockk()))
        every { dataRepository.getCategoryList() } returns categories
        viewModel.fetchQuestionsData(Dispatchers.Unconfined)
        viewModel.categoryList
        assert(viewModel.categoryList.value!=null)
        assert(viewModel.categoryList.value!![0] == "first category")
    }

    @Test
    fun store_selected_question_and_answer()  {
        val pd = PersonalityData("first question","important")
        val pd1 = PersonalityData("second question","not important")
        every { dataRepository.savePersonalityData(pd) }
        //every { dataRepository.savePersonalityData(pd1) }
        viewModel.storeData(pd,Dispatchers.Unconfined)
        //viewModel.storeData(pd1,Dispatchers.Unconfined)
        every { dataRepository.getPersonalityData() } returns listOf(pd/*,pd1*/)
        viewModel.getData(Dispatchers.Unconfined)
        assert(viewModel.personalityInfo.value!=null)

    }
}
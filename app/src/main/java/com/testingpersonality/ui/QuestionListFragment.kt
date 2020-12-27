package com.testingpersonality.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.testingpersonality.R
import com.testingpersonality.data.local.PersonalityData
import com.testingpersonality.model.Question
import com.testingpersonality.utils.Config.CATEGORY_KEY
import com.testingpersonality.utils.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_question_list.*
import kotlinx.android.synthetic.main.fragment_question_list.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class QuestionListFragment : Fragment() {

    private val viewModel by viewModels<ListingViewModel>()
    private var questionList = listOf<Question>()
    lateinit var currentQuestion: Question
    private var answer = mutableListOf<String>()
    private var questionIndex = 0
    private var numQuestions: Int = 0
    lateinit var personalityData: PersonalityData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_list, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listOfQuestionObserver()

        nextClickListener(view)

        radioGroupCheckListener(view)

        inputEditTextChangeListener()
    }

    private fun inputEditTextChangeListener() {
        condition_edit.condition_edit_text.doAfterTextChanged { charSequence ->
            val data = if (charSequence.toString().isEmpty()) 0 else charSequence.toString().toInt()
            val range = currentQuestion.question_type.condition!!.if_positive.question_type.range
            if (data > range.from && data <= range.to) {
                personalityData = PersonalityData(
                    currentQuestion.question_type.condition!!.if_positive.question,
                    charSequence.toString()
                )
                viewModel.storeData(personalityData)
                nextButton.isEnabled = true
                condition_edit.error = null
            } else {
                condition_edit.error = getString(R.string.input_edit_error, range.from, range.to)
            }
        }
    }

    private fun radioGroupCheckListener(view: View) {
        radiogroup_option.setOnCheckedChangeListener { radioGroup, i ->
            val mRadiobtnId: Int = radioGroup.checkedRadioButtonId
            val selectedOptions = view.findViewById<View>(mRadiobtnId) as RadioButton
            nextButton.isEnabled = true
            checkConditionEnableOrNOT(selectedOptions)
            personalityData =
                PersonalityData(currentQuestion.question, selectedOptions.text.toString())
            viewModel.storeData(personalityData)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun nextClickListener(view: View) {
        nextButton.setOnClickListener {
            conditionVisibilityView(false)
            val checkedId = radiogroup_option.checkedRadioButtonId
            if (-1 != checkedId) {
                questionIndex++
                if (questionIndex < numQuestions) {
                    setQuestion()
                    view.invalidate()
                } else {
                    view.findNavController()
                        .navigate(R.id.action_questionListFragment_to_reviewQuestionAndAnswersFragment)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun listOfQuestionObserver() {
        viewModel.listQuestion.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    questionList =
                        state.data.filter { it.category == arguments?.getString(CATEGORY_KEY) }
                    if(questionList.isNotEmpty()) setQuestion()
                }
                is State.Error -> {
                    Toast.makeText(activity, state.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun checkConditionEnableOrNOT(selectedOptions: RadioButton) {
        if (currentQuestion.question_type.condition != null &&
            currentQuestion.question_type.condition!!.predicate.exactEquals.contains(selectedOptions.text.toString())
        ) {
            conditionVisibilityView(true)
            nextButton.isEnabled = false
            question_condition_text.text =
                currentQuestion.question_type.condition!!.if_positive.question
        } else conditionVisibilityView(false)
    }

    private fun conditionVisibilityView(isVisible:Boolean) {
        question_condition_text.visibility = if(isVisible)View.VISIBLE else View.GONE
        condition_edit.visibility = if(isVisible)View.VISIBLE else View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setQuestion() {
        numQuestions = questionList.size
        currentQuestion = questionList[questionIndex]
        answer.clear()
        answer = currentQuestion.question_type.options.toMutableList()
        question_text.text = currentQuestion.question
        nextButton.isEnabled = false
        addRadioButtonOptions(answer)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun addRadioButtonOptions(
        optionsList: List<String>
    ) {
            radiogroup_option.orientation = LinearLayout.VERTICAL
            radiogroup_option.removeAllViews()
            for (i in optionsList.indices) {
                val rdbtn = RadioButton(context)
                rdbtn.id = View.generateViewId()
                rdbtn.text = optionsList[i]
                radiogroup_option.addView(rdbtn)
            }
    }

}
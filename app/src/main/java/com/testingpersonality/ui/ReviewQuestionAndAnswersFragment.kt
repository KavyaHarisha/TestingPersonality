package com.testingpersonality.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.testingpersonality.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_review_question_and_answers.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class ReviewQuestionAndAnswersFragment : Fragment() {
    @ExperimentalCoroutinesApi
    private val viewModel by viewModels<ListingViewModel>()
    private val savedAnswerAdapter = SavedAnswerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_question_and_answers, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewRecyclerView.adapter = savedAnswerAdapter
        viewModel.getData(Dispatchers.IO)
        viewModel.personalityInfo.observe(viewLifecycleOwner, Observer {
            savedAnswerAdapter.updateCategory(it)
            savedAnswerAdapter.notifyDataSetChanged()
        })
        submit_button.setOnClickListener {
            view.findNavController().navigate(R.id.action_reviewQuestionAndAnswersFragment_to_categoryListFragment)
        }
    }
}
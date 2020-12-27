package com.testingpersonality.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.testingpersonality.R
import com.testingpersonality.utils.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CategoryListFragment : Fragment() {
    private val viewModel by viewModels<ListingViewModel>()
    private var adapter = SelectCategoryAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_list, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryRecyclerView.adapter =  adapter
        handleNetworkChanges()
        viewModel.categoryList.observe(viewLifecycleOwner, Observer {
            loader.visibility = View.GONE
            adapter.updateCategory(it)
            adapter.notifyDataSetChanged()
        })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun handleNetworkChanges() {
        context.let {
            NetworkUtils.getNetworkLiveData(it!!)
                .observe(viewLifecycleOwner, Observer { isConnected ->
                    if (!isConnected) {
                        loader.visibility = View.GONE
                        textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                        networkStatusLayout.apply {
                            visibility = View.VISIBLE
                            setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.colorStatusNotConnected
                                )
                            )
                        }
                    } else {
                        loader.visibility = View.VISIBLE
                        viewModel.fetchQuestionsData()
                        textViewNetworkStatus.text = getString(R.string.text_connectivity)
                        networkStatusLayout.apply {
                            setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.colorStatusNotConnected
                                )
                            )
                            animate()
                                .alpha(1f)
                                .setStartDelay(ANIMATION_DURATION)
                                .setDuration(ANIMATION_DURATION)
                                .setListener(object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        visibility = View.GONE
                                    }
                                })
                        }
                    }
                })
        }
    }

    companion object {
        const val ANIMATION_DURATION = 1000.toLong()
    }
}
package com.arturomarmolejo.acronymsappam.views

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.arturomarmolejo.acronymsappam.R
import com.arturomarmolejo.acronymsappam.databinding.FragmentAcronymListBinding
import com.arturomarmolejo.acronymsappam.utils.BaseFragment
import com.arturomarmolejo.acronymsappam.utils.UIState
import com.arturomarmolejo.acronymsappam.views.adapter.AcronymsAdapter



/**
 * A simple [Fragment] subclass.
 * Use the [AcronymList.newInstance] factory method to
 * create an instance of this fragment.
 */
private const val TAG = "AcronymList"
class AcronymList : BaseFragment() {
//   private val binding by lazy {
//       FragmentAcronymListBinding.inflate(layoutInflater)
//   }

    private lateinit var binding: FragmentAcronymListBinding

    private val acronymsAdapter: AcronymsAdapter by lazy {
        AcronymsAdapter {
            acronymsViewModel.selectedAcronymItem = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
//        binding = FragmentAcronymListBinding.inflate(inflater, container, false)
        binding = DataBindingUtil.inflate<FragmentAcronymListBinding>(
            inflater,
            R.layout.fragment_acronym_list,
            container,
            false
        ).apply {
            this.viewModel = acronymsViewModel
        }

        binding.acronymListRv.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
            adapter = acronymsAdapter
        }

        isInternetAvailable()

        if(!isInternetAvailable()) {
            binding.noInternetTextView.visibility = View.VISIBLE
        } else {
            // check if the recycler view is empty and show/hide the TextView accordingly
            if(acronymsAdapter.itemCount == 0) {
                binding.noAcronymSearchedTv.visibility = View.VISIBLE
            } else {
                binding.noAcronymSearchedTv.visibility = View.GONE
            }

            binding.noInternetTextView.visibility = View.GONE
            acronymsViewModel.longFormList.observe(viewLifecycleOwner) { state ->
                when(state) {
                    is UIState.LOADING -> {}
                    is UIState.SUCCESS -> {
                        Log.d(TAG, "onCreateView: ${state.response}")
                        acronymsAdapter.updateItems(state.response)
                        // check again if the recycler view is empty and show/hide the TextView accordingly
                        if(acronymsAdapter.itemCount == 0) {
                            binding.noAcronymSearchedTv.visibility = View.VISIBLE
                        } else {
                            binding.noAcronymSearchedTv.visibility = View.GONE
                        }
                    }
                    is UIState.ERROR -> {
                        showError(state.error.localizedMessage) {
                            Log.d(TAG, "onCreateView: UIState Error: $state ")
                        }
                    }
                }
            }
        }


        return binding.root
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }
}


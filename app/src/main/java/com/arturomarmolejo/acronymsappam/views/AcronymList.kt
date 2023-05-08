package com.arturomarmolejo.acronymsappam.views

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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        acronymsViewModel.longFormList.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UIState.LOADING -> {}
                is UIState.SUCCESS -> {
                    Log.d(TAG, "onCreateView: ${state.response}")
                    acronymsAdapter.updateItems(state.response)
                }
                is UIState.ERROR -> {
                    showError(state.error.localizedMessage) {
                        Log.d(TAG, "onCreateView: UIState Error: $state ")
                    }
                }
            }
        }


        return binding.root
    }
}
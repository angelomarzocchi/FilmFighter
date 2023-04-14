package com.example.filmfighter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.filmfighter.databinding.FragmentWinnerBinding
import com.example.filmfighter.model.ViewModel


class WinnerFragment : Fragment() {

    private lateinit var binding: FragmentWinnerBinding
    private val movieViewModel: ViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentWinnerBinding.inflate(inflater,container,false)
        binding = fragmentBinding
        binding.lifecycleOwner = viewLifecycleOwner
        movieViewModel.computeWinner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val first = movieViewModel.winners.value?.get(0)
        if (first != null) {
            binding.firstNameTextView.text = first.name
            binding.firstMovieTitleTextView.text = first.movie
            binding.firstPointTextView.text = first.points.toString()
        }
        val second = movieViewModel.winners.value?.get(1)
        if(second != null) {
            binding.secondNameTextView.text = second.name
            binding.secondMovieTitleTextView.text = second.movie
            binding.secondPointTextView.text = second.points.toString()
        }

        if(movieViewModel.fighters.value?.size!! >2) {
            val third = movieViewModel.winners.value?.get(3)
            if(third != null){
                binding.thirdNameTextView.text = third.name
                binding.thirdMovieTitleTextView.text = third.movie
                binding.thirdPointTextView.text = third.points.toString()
            }
        } else {binding.thirdCardView.isVisible = false}


    }

    




}
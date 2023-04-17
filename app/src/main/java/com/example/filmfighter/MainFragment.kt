package com.example.filmfighter.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.filmfighter.R
import com.example.filmfighter.databinding.FragmentMainBinding
import com.example.filmfighter.model.ViewModel

class MainFragment : Fragment() {

    private val viewModel: ViewModel by activityViewModels()
    private lateinit var binding: FragmentMainBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentMainBinding = FragmentMainBinding
            .inflate(inflater,container,false)
        binding = fragmentMainBinding

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            hostGameButton.setOnClickListener { hostGame() }
            joinGameButton.setOnClickListener { joinGame() }
        }
    }




    private fun chooseFilm() {
        findNavController()
            .navigate(
                R.id.action_mainFragment_to_chooseFilm
            )
    }

    private fun hostGame() {
        viewModel.host()
        chooseFilm()

    }

    private fun joinGame() {
        viewModel.join()
        chooseFilm()
    }


}
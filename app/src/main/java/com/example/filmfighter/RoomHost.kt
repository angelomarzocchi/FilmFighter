package com.example.filmfighter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.filmfighter.databinding.FragmentRoomHostBinding
import com.example.filmfighter.model.ViewModel


class RoomHost : Fragment() {

    private lateinit var binding: FragmentRoomHostBinding

    private val movieViewModel: ViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentRoomHostBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding.lifecycleOwner = viewLifecycleOwner
        binding.nameTextView.setText(movieViewModel.myDevice)
        binding.confirmName.setOnClickListener { confirmUsername() }


        binding.recyclerView.adapter = FighterListAdapter()



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            roomHostFragment = this@RoomHost
            viewModel = movieViewModel
            movie = movieViewModel.movie.value
            startGameButton.isEnabled = false
        }
        if(!movieViewModel.hosting)
            binding.startGameButton.isVisible = false


        movieViewModel.fighters.observe(viewLifecycleOwner) {
            if (it.size > 1)
                binding.startGameButton.isEnabled = true
        }



        movieViewModel.isGameStarted.observe(viewLifecycleOwner) {
            if(it){
                findNavController().
                navigate(R.id.action_roomHost_to_questionFragment)
            }
        }


        Log.d("isGameStarted",movieViewModel.isGameStarted.hasObservers().toString())


    }

    private fun confirmUsername() {
        hideKeyboard()
        binding.nameTextView.clearFocus()
        if(binding.nameTextView.text.isNotEmpty()) {
            movieViewModel.onConfirmedName(binding.nameTextView.text.toString())

            Log.d("FighterList", (movieViewModel.fighters.value?.get(0)?.name ?: "no fighters"))
            //binding.startGameButton.isEnabled = true
            //inizia advertising ecc
            if(movieViewModel.hosting){
                (activity as MainActivity).startAdvertising()
            }else {(activity as MainActivity).startDiscovery()}
        } else {
            Toast.makeText(context,"Input a name to start the game",Toast.LENGTH_SHORT).show()
            binding.startGameButton.isEnabled = false
        }


    }
    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

     fun startGame() {
        (activity as MainActivity).startGame()
         movieViewModel.startGame()

    }







}
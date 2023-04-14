package com.example.filmfighter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.filmfighter.databinding.FragmentChooseFilmBinding
import com.example.filmfighter.model.ViewModel


class ChooseFilm : Fragment() {

    private lateinit var binding: FragmentChooseFilmBinding

    private val movieViewModel: ViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentChooseFilmBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding.lifecycleOwner = viewLifecycleOwner

        binding.recyclerView.adapter = TrendingMovieListAdapter(
            MovieListener { movie ->
                movieViewModel.onMovieClicked(movie)
                    findNavController()
                        .navigate(R.id.action_chooseFilm_to_roomHost)
            })

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    movieViewModel.searchMovies(query)
                    hideKeyboard()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText == null || newText.isEmpty()){
                  movieViewModel.restoreMovies()
                    hideKeyboard()
                return true
                }else {return false}
            }

        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            chooseFilmFragment = this@ChooseFilm
            viewModel = movieViewModel
        }
    }

    private fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }



}
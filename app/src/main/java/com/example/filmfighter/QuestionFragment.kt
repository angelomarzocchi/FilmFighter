package com.example.filmfighter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.filmfighter.databinding.FragmentQuestionBinding
import com.example.filmfighter.model.ViewModel


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class QuestionFragment : Fragment() {

    private lateinit var binding: FragmentQuestionBinding
    private val movieViewModel: ViewModel by activityViewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentQuestionBinding.inflate(inflater,container,false)
        binding = fragmentBinding
        binding.lifecycleOwner = viewLifecycleOwner







        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            questionFragment = this@QuestionFragment
            viewModel = movieViewModel
        }


        movieViewModel.currentQuestionIndex.observe(viewLifecycleOwner) {
            if(it == 5)
                findNavController()
                    .navigate(R.id.action_questionFragment_to_winnerFragment)
        }




        movieViewModel.currentQuestion.observe(viewLifecycleOwner) {question ->

            binding.questionTextView.text = question?.question ?: "No question"
            binding.firstAnswerButton.text = question?.answers?.get(0)
            binding.secondAnswerButton.text = question?.answers?.get(1)
            binding.thirdAnswerButton.text = question?.answers?.get(2)
            binding.fourthAnswerButton.text = question?.answers?.get(3)


            binding.firstAnswerButton.isEnabled = true
            binding.secondAnswerButton.isEnabled = true
            binding.thirdAnswerButton.isEnabled = true
            binding.fourthAnswerButton.isEnabled = true

        }


    }

    fun sendAnswer(answer: Int){
        binding.firstAnswerButton.isEnabled = false
        binding.secondAnswerButton.isEnabled = false
        binding.thirdAnswerButton.isEnabled = false
        binding.fourthAnswerButton.isEnabled = false
        movieViewModel.vote(movieViewModel.myName,answer)
        (activity as MainActivity).sendQuizAnswer(movieViewModel.myName,answer.toString())









    }

    


}
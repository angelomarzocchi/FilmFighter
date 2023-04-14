package com.example.filmfighter.model



import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmfighter.network.*

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class TrendingMovieApiStatus { NOT_STARTED, LOADING, FAILED, DONE }
enum class SearchMovieApiStatus { NOT_STARTED, LOADING, FAILED, DONE }


class ViewModel : ViewModel() {

    //dati e funzioni riguardanti il gioco
    var hosting: Boolean = false
    fun host() {
        hosting = true
    }

    private val _currentQuestionIndex = MutableLiveData<Int>(-1)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private fun incrementCurrentQuestionCounter() {
        _currentQuestionIndex.value = _currentQuestionIndex.value!! + 1
    }

    private val _isGameStarted = MutableLiveData<Boolean>(false)
    val isGameStarted: LiveData<Boolean> = _isGameStarted

    fun startGame() {
        _isGameStarted.value = true
    }

    private var numberOfVotes = 0


    fun vote(fighter: String, vote: Int) {
        _fighters.value?.find { it.name == fighter }?.answers?.set(
            _currentQuestionIndex.value!!,
            vote
        )
        numberOfVotes++
        Log.d("numberOfVotes", numberOfVotes.toString())
        if (numberOfVotes == _fighters.value!!.size)
            nextQuestion()
    }


    private val _fighters = MutableLiveData<List<Fighter>>()
    val fighters: LiveData<List<Fighter>> = _fighters
    var myName: String = ""

    fun onConfirmedName(name: String) {
        myName = name
        _fighters.value = listOf(Fighter(name, movie.value!!.title))

    }

    fun addFighter(name: String, film: String) {
        val newFighter = Fighter(name, film)
        val newList = mutableListOf<Fighter>()
        _fighters.value?.let { newList.addAll(it) }
        newList.add(newFighter)
        _fighters.value = newList.toList()


    }

    private val _questions = MutableLiveData<List<Question>>(QuestionsList.value)
    val questions: LiveData<List<Question>> = _questions

    private val _currentQuestion = MutableLiveData<Question>()
    val currentQuestion: LiveData<Question> = _currentQuestion

    fun nextQuestion() {
        numberOfVotes = 0
        incrementCurrentQuestionCounter()
        _currentQuestion.value = _questions.value?.get(_currentQuestionIndex.value!!)
    }


    private val _winners = MutableLiveData<List<Fighter>>()
    val winners: LiveData<List<Fighter>> = _winners


    fun computeWinner() {


        for (f in _fighters.value!!)
            evaluateAnswer(f)
        _winners.value = fighters.value?.sortedByDescending { fighter -> fighter.points }?.take(3)

    }


    private fun evaluateAnswer(fighter: Fighter) {
        for (i in 0 until questions.value!!.size) {
            if (questions.value!![i].rightAnswer == fighter.answers[i])
                fighter.points += 10
        }

    }


    // dati e funzioni riguardanti le API dei film
    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie> = _movie


    val myDevice = DefaultNames.namesList.random()

    private val _trendingMoviesResponse = MutableLiveData<MovieResponse>()
    val trendingMoviesResponse: LiveData<MovieResponse> = _trendingMoviesResponse

    private val _trendingMovies = MutableLiveData<List<Movie>>()
    val trendingMovies: LiveData<List<Movie>> = _trendingMovies

    private val _searchMovieResponse = MutableLiveData<MovieResponse>()
    val searchMovieResponse: LiveData<MovieResponse> = _searchMovieResponse

    private val _searchedMovies = MutableLiveData<List<Movie>>()
    val searchedMovies: LiveData<List<Movie>> = _searchedMovies

    private val _winnersMovies = MutableLiveData<List<Movie>>()
    val winnersMovies: LiveData<List<Movie>> = _winnersMovies


    private val _trendingMovieStatus =
        MutableStateFlow<TrendingMovieApiStatus>(TrendingMovieApiStatus.NOT_STARTED)
    val trendingMovieApiStatus: StateFlow<TrendingMovieApiStatus> = _trendingMovieStatus

    private val _searchedMovieStatus =
        MutableStateFlow<SearchMovieApiStatus>(SearchMovieApiStatus.NOT_STARTED)
    val searchedMovieStatus: StateFlow<SearchMovieApiStatus> = _searchedMovieStatus

    private fun getTrendingMovies() {
        viewModelScope.launch {
            _trendingMovieStatus.value = TrendingMovieApiStatus.LOADING
            try {

                _trendingMoviesResponse.value = TheMovieDB.retrofitService.getTrendingMovies()

                _trendingMovies.value = _trendingMoviesResponse.value!!.results
                Log.d("MovieDB Error", _trendingMovies.value!![0].absulutePosterPath)
                restoreMovies()
                _trendingMovieStatus.value = TrendingMovieApiStatus.DONE
            } catch (e: Exception) {
                Log.d("MovieDB Error", e.toString())
                _trendingMovieStatus.value = TrendingMovieApiStatus.FAILED
            }

        }

    }


    fun searchMovies(title: String) {
        viewModelScope.launch {
            _searchedMovieStatus.value = SearchMovieApiStatus.LOADING
            try {
                _searchMovieResponse.value = TheMovieDB.retrofitService.getSearchResult(title)
                _searchedMovies.value = _searchMovieResponse.value!!.results
                Log.d("Search Good", searchedMovies.value!![0].title)
                _searchedMovieStatus.value = SearchMovieApiStatus.DONE
            } catch (e: Exception) {
                Log.d("Search Error", e.toString())
                _searchedMovieStatus.value = SearchMovieApiStatus.FAILED
            }
        }

    }

    fun searchWinnersMovies(winners: List<Fighter>) {
        viewModelScope.launch {
            val list = mutableListOf<Movie>()
            _searchedMovieStatus.value = SearchMovieApiStatus.LOADING
            try {
                for (w in winners) {
                    _searchMovieResponse.value = TheMovieDB.retrofitService.getSearchResult(w.movie)
                    list.add(_searchMovieResponse.value!!.results[0])
                }
                _searchedMovieStatus.value = SearchMovieApiStatus.DONE

            } catch (e: Exception) {
                _searchedMovieStatus.value = SearchMovieApiStatus.FAILED
            }

        }

    }


    fun restoreMovies() {
        _searchedMovies.value = _trendingMovies.value
    }

    fun resetGame() {
        _fighters.value = mutableListOf()
        _isGameStarted.value = false
    }


    init {
        getTrendingMovies()
        resetGame()
        nextQuestion()
        _questions.value = QuestionsList.value
    }

    fun onMovieClicked(movie: Movie) {
        _movie.value = movie
    }
}
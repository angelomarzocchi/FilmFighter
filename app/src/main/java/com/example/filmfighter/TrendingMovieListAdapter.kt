package com.example.filmfighter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filmfighter.databinding.MovieListItemBinding
import com.example.filmfighter.network.Movie

class TrendingMovieListAdapter(private val clickListener: MovieListener): ListAdapter<Movie,
        TrendingMovieListAdapter.MovieViewHolder>(DiffCallback) {

    class MovieViewHolder(
        private var binding:
        MovieListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(clickListner: MovieListener, movie: Movie) {
            binding.movie = movie
            binding.clickListener = clickListner
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.title == newItem.title
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MovieViewHolder(
            MovieListItemBinding.inflate(layoutInflater,parent,false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(clickListener,movie)
    }


}

class MovieListener(val clickListner: (movie: Movie) -> Unit) {

    fun onClick(movie: Movie) = clickListner(movie)
}

package com.example.filmfighter

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.filmfighter.model.Fighter
import com.example.filmfighter.network.Movie

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView,data: List<Movie>?) {
    val adapter = recyclerView.adapter as TrendingMovieListAdapter
    adapter.submitList(data)
}

@BindingAdapter("fighterData")
fun bindFighterRecyclerView(recyclerView: RecyclerView,data: List<Fighter>?) {
    val adapter = recyclerView.adapter as FighterListAdapter
    adapter.submitList(data)
}



@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}
package com.example.pokemongeo.pokedex;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemongeo.R;
import com.example.pokemongeo.databinding.PokemonItemBinding;
import com.example.pokemongeo.model.Pokemon;

import java.util.List;

public class PokemonListAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<Pokemon> pokemonList;
    private OnClickOnNoteListener listener;

    public PokemonListAdapter(List<Pokemon> pokemonList, OnClickOnNoteListener listener){
        this(pokemonList);
        this.listener = listener;
    }

    public PokemonListAdapter(List<Pokemon> pokemonList) {
        assert pokemonList != null;
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PokemonItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.pokemon_item, parent, false);

        return new ViewHolder(binding, listener);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.viewModel.setPokemon(pokemon);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

}

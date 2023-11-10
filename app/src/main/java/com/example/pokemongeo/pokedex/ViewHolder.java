package com.example.pokemongeo.pokedex;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemongeo.PokemonViewModel;
import com.example.pokemongeo.databinding.PokemonItemBinding;

class ViewHolder extends RecyclerView.ViewHolder {
    private PokemonItemBinding binding;
    public PokemonViewModel viewModel = new PokemonViewModel();
    private OnClickOnNoteListener listener;

    ViewHolder(PokemonItemBinding binding, OnClickOnNoteListener listener){
        this(binding);
        this.listener = listener;

        this.binding.getRoot().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClickOnNote(viewModel);
                    }
                }
        );
    }

    ViewHolder(PokemonItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.setPokemonViewModel(viewModel);

    }
}

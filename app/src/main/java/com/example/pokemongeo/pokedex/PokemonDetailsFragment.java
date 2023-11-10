package com.example.pokemongeo.pokedex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pokemongeo.PokemonViewModel;
import com.example.pokemongeo.R;
import com.example.pokemongeo.databinding.PokemonDetailsBinding;

public class PokemonDetailsFragment extends Fragment  {
    private PokemonViewModel viewModel;
    public PokemonDetailsFragment(PokemonViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        PokemonDetailsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.pokemon_details,container,false);

        binding.setPokemonViewModel(viewModel);
        return binding.getRoot();
    }



}
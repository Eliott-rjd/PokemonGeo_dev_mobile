package com.example.pokemongeo.pokedex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pokemongeo.database.Database;
import com.example.pokemongeo.R;
import com.example.pokemongeo.databinding.PokedexFragmentBinding;
import com.example.pokemongeo.model.Pokemon;

import java.util.List;

public class PokedexFragment extends Fragment {

    private OnClickOnNoteListener listener;
    Database mydb;
    public PokedexFragment(OnClickOnNoteListener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        PokedexFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.pokedex_fragment,container,false);

        mydb = Database.getInstance(binding.getRoot().getContext());
        List<Pokemon> pokemonList = mydb.getAllPokemon();

        PokemonListAdapter adapter = new PokemonListAdapter(pokemonList, listener);
        binding.pokemonList.setAdapter(adapter);
        binding.pokemonList.setLayoutManager(new LinearLayoutManager(
                binding.getRoot().getContext()));

        return binding.getRoot();
    }
}
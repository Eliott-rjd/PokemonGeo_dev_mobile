package com.example.pokemongeo.starter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pokemongeo.R;
import com.example.pokemongeo.database.Database;
import com.example.pokemongeo.databinding.StartersFragmentBinding;
import com.example.pokemongeo.model.Pokemon;

import java.util.List;

public class StarterSelectionFragment extends Fragment {

    StarterSelectionListener listener;

    Database mydb;
    public StarterSelectionFragment(StarterSelectionListener starterSelectionListener) {
        listener = starterSelectionListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        StartersFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.starters_fragment,container,false);

        mydb = Database.getInstance(binding.getRoot().getContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Pokemon> starterList = mydb.getStarter();

        // SETUP listener for the starters :
        ImageButton button = getView().findViewById(R.id.starter1);
        button.setClickable(true);
        button.setOnClickListener(v -> chooseStarter(starterList.get(0)));

        button = getView().findViewById(R.id.starter2);
        button.setClickable(true);
        button.setOnClickListener(v -> chooseStarter(starterList.get(1)));

        button = getView().findViewById(R.id.starter3);
        button.setClickable(true);
        button.setOnClickListener(v -> chooseStarter(starterList.get(2)));
    }

    /**
     * Add the starter to the database. This pokemon is the starter
     * Then leave the fragment using the listener
     */
    public void chooseStarter(Pokemon chosenStarter){
        // Write the selected started in the DB :

        chosenStarter.setStats(5 , 30 ,30);
        mydb.capturePokemon( chosenStarter );

        // Leave the fragments :
        listener.onSarterSelected();
    }

}
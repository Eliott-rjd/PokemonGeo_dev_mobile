package com.example.pokemongeo.fight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pokemongeo.PokemonViewModel;
import com.example.pokemongeo.R;
import com.example.pokemongeo.database.Database;
import com.example.pokemongeo.databinding.FightFragementBinding;

public class FightFragment extends Fragment {

    private PokemonViewModel opponentPokemonViewModel;
    private PokemonViewModel playerPokemonViewModel;

    private MapToFightListener listener;

    private Button fightButton;
    private Database mydb;

    public FightFragment(PokemonViewModel opponentPokemonViewModel, PokemonViewModel playerPokemonViewModel) {
        this.opponentPokemonViewModel = opponentPokemonViewModel;
        this.playerPokemonViewModel = playerPokemonViewModel;
    }

    public void setListener(MapToFightListener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FightFragementBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fight_fragement, container, false);

        binding.setOpponentPokemonViewModel(opponentPokemonViewModel);
        binding.setPlayerPokemonViewModel(playerPokemonViewModel);

        mydb = Database.getInstance(binding.getRoot().getContext());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fightButton = getView().findViewById(R.id.fightButton);
        fightButton.setOnClickListener(v -> launchFight());
    }

    /**
     * Run the fight between the player and opponent
     */
    private void launchFight(){
        // TODO : generate stat for opponent
        opponentPokemonViewModel.getPokemonModel().setStats(5, 5,5);

        // TODO : define fight rules

        // if player win :
        if(true){
            if(mydb.getPokeballQuantity() > 0) {
                fightButton.setText("Catch !");
                fightButton.setOnClickListener(v -> catchOppenent());
            } else {
                // TODO set a message for new pokeball
                // Si plus de pokeball, on peut seulement le combattre en gagner une pokeball
                mydb.addOnePokeball();
                listener.removeMarker();
                listener.goBackToMap();
            }
        } else {
            listener.goBackToMap();
        }
    }

    /**
     * Catch the oppenent pokemon
     */
    private void catchOppenent(){

        if(mydb.getPokeballQuantity() > 0){
            mydb.deleteOnePokeball();
            // TODO : calcutale capture rate
            if (true){
                mydb.capturePokemon(opponentPokemonViewModel.getPokemonModel());

                // TODO set a message for the capture
                listener.removeMarker();
                listener.goBackToMap();
            }
        }
    }
}

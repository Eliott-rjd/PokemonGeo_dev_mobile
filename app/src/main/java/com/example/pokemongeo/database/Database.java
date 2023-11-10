package com.example.pokemongeo.database;

import android.content.Context;

import com.example.pokemongeo.model.Item;
import com.example.pokemongeo.model.Pokemon;
import java.util.List;

public class Database {
    private static Database instance = null;
    private DBHelper helper;

    // database version
    private final int DB_VERSION = 1;
    private Database(Context context){
        helper = new DBHelper(context,"MyDatabase.db",null, DB_VERSION);
        List<Pokemon> allPokemon = getAllPokemon();  // if json is not converted in the DB
        if (allPokemon.isEmpty()) {
            createAllPokemon(context);
            initInventory();
        }
    }

    public static Database getInstance(Context context){
        if(instance == null)
            instance = new Database(context);
        return instance;
    }

    public List<Pokemon> getAllPokemon() {
        List<Pokemon> pokemonList = helper.getAllPokemon();

        return pokemonList;
    }

    public void createAllPokemon(Context context) {
        helper.createAllPokemon(context);
    }
    public void initInventory() {
        helper.initInventory();
    }

    public List<Pokemon> getStarter() {
        List<Pokemon> listStarter = helper.getStarter();
        return listStarter;
    }

    public List<Item> getInventory() {
        return helper.getInventory();
    }
    public void capturePokemon(Pokemon pokemon) {
        helper.capturePokemon(pokemon);
    }
    public List<Pokemon> getMyPokemon() {
        return helper.getMyPokemon();
    }
    public Pokemon getOnePokemonByDexNo(int dexNo) {
        return helper.getOnePokemonByDexNo(dexNo);
    }
    public Pokemon getOnePokemonByName(String name) {
        return helper.getOnePokemonByName(name);
    }
    public int getPokeballQuantity() {
        return helper.getPokeballQuantity();
    }
    public void deleteOnePokeball() {
        helper.deleteOnePokeball();
    }
    public void addOnePokeball() {
        helper.addOnePokeball();
    }
}
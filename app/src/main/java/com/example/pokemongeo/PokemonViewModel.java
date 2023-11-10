package com.example.pokemongeo;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.pokemongeo.model.Pokemon;

public class PokemonViewModel extends BaseObservable {
    private Pokemon pokemon = new Pokemon();
    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
        notifyChange();
    }
    @Bindable
    public int getFront() {
        return pokemon.getFrontResource();
    }

    @Bindable
    public int getImageType1() {
        return pokemon.getImageType1();
    }

    @Bindable
    public int getImageType2() {
        return pokemon.getImageType2();
    }
    @Bindable
    public String getName() {
        return pokemon.getName();
    }
    @Bindable
    public String getType1() {
        return pokemon.getType1String();
    }
    @Bindable
    public String getType2() {
        if (pokemon.getType2() != null)
            return pokemon.getType2String();
        return "";
    }
    @Bindable
    public String getHeight() {
        return pokemon.getHeight() + " m";
    }
    @Bindable
    public String getWeight() {
        return pokemon.getWeight() + " kg";
    }

    @Bindable
    public String getNumber() {
        return "#"+pokemon.getOrder();
    }
    @Bindable
    public int getImageCapture() {
        return pokemon.getImageCapture();
    }
    public long getDexNo(){return (long)pokemon.getOrder(); }
    public Drawable getImage(Context context, int res) {
        if(res != -1)
            return ResourcesCompat.getDrawable(context.getResources(),
                    res, context.getTheme());
        else
            return null;
    }

    public Pokemon getPokemonModel(){
        return pokemon;
    }

}
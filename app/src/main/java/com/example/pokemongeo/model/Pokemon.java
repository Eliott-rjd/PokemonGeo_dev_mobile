package com.example.pokemongeo.model;

import com.example.pokemongeo.R;

public class Pokemon {
    private int order;
    private String name;
    private String height;
    private String weight;
    private int frontResource;
    private POKEMON_TYPE type1;
    private POKEMON_TYPE type2;
    private int imageType1;
    private int imageType2;
    private int imageCapture;

    // Stats:

    private int stat_HP;
    private int stat_ATK;
    private int stat_LEVEL;

    public Pokemon() {
        order = 1;
        name = "Unknown";
        frontResource = R.drawable.p1;
        type1 = POKEMON_TYPE.Plante;
        weight = "0";
        height = "0";
        imageType1 = -1;
        imageType2 = -1;
        imageCapture = -1;
    }
    public Pokemon(int order, String name, int frontResource,
                   POKEMON_TYPE type1, int imageType1,
                   POKEMON_TYPE type2, int imageType2,
                   String weight, String height,
                   int imageCapture) {
        this.order = order;
        this.name = name;
        this.frontResource = frontResource;
        this.type1 = type1;
        this.type2 = type2;
        this.weight = weight;
        this.height = height;
        this.imageType1  = imageType1;
        this.imageType2  = imageType2;
        this.imageCapture = imageCapture;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getOrder() {
        return order;
    }
    public void setOrder(int order) {
        this.order = order;
    }
    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
    public int getFrontResource() {
        return frontResource;
    }
    public void setFrontResource(int frontResource) {
        this.frontResource = frontResource;
    }
    public POKEMON_TYPE getType1() {
        return type1;
    }
    public void setType1(POKEMON_TYPE type1) {
        this.type1 = type1;
    }
    public POKEMON_TYPE getType2() {
        return type2;
    }
    public void setType2(POKEMON_TYPE type2) {
        this.type2 = type2;
    }
    public String getType1String() {
        return type1.name();
    }
    public String getType2String() {
        return type2.name();
    }

    public int getImageType1() {
        return imageType1;
    }
    public int getImageType2() {
        return imageType2;
    }
    public int getImageCapture() {
        return imageCapture;
    }
    public void setCapture(int capture) {
        this.imageCapture = imageCapture;
    }

    // STATS :
    public void setStats(int level,int hp, int atk){
        stat_LEVEL = level;
        stat_HP = hp;
        stat_ATK = atk;
    }
    public int getStateHP(){
        return stat_HP;
    }
    public int getStateATK(){
        return stat_ATK;
    }
    public int getStateLEVEL(){ return stat_LEVEL; }
}


package com.example.pokemongeo.fight;

public interface MapToFightListener {

    /**
     * Leave the fragment toç go back to map :
     * remove the lase fragment added to transaction
     */
    public void goBackToMap();

    /**
     * delete a marker marcker from the map
     */
    public void removeMarker();
}

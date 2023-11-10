package com.example.pokemongeo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.pokemongeo.model.Item;

public class ItemViewModel extends BaseObservable {
    private Item item = new Item();
    public void setItem(Item item) {
        this.item = item;
        notifyChange();
    }
    @Bindable
    public String getItemName() {
        return item.getItemName();
    }
    @Bindable
    public String getPrice() {
        return "Price:" + item.getPrice();
    }
    @Bindable
    public String getQuantity() {
        return "Quantity: " + item.getQuantity();
    }

    public Item getItemModel(){
        return item;
    }

}
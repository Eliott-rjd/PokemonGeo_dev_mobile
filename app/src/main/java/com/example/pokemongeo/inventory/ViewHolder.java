package com.example.pokemongeo.inventory;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemongeo.ItemViewModel;
import com.example.pokemongeo.databinding.InventoryItemBinding;

class ViewHolder extends RecyclerView.ViewHolder {
    private InventoryItemBinding binding;
    public ItemViewModel viewModel = new ItemViewModel();

    ViewHolder(InventoryItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.binding.setItemViewModel(viewModel);

    }
}

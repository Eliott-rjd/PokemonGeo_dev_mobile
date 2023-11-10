package com.example.pokemongeo.inventory;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokemongeo.R;
import com.example.pokemongeo.databinding.InventoryItemBinding;
import com.example.pokemongeo.model.Item;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ViewHolder> {
        List<Item> itemList;
        public ItemListAdapter(List<Item> itemList) {
            assert itemList != null;
            this.itemList =itemList;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            InventoryItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.inventory_item, parent, false);
            return new ViewHolder(binding);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Item item = itemList.get(position);
            holder.viewModel.setItem(item);
        }
        @Override
        public int getItemCount() {
            return itemList.size();
        }

}

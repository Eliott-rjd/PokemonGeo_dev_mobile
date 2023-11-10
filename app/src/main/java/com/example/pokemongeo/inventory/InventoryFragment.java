package com.example.pokemongeo.inventory;

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
import com.example.pokemongeo.databinding.InventoryFragmentBinding;
import com.example.pokemongeo.model.Item;

import java.util.List;

public class InventoryFragment extends Fragment {

    Database mydb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        InventoryFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.inventory_fragment,container,false);

        mydb = Database.getInstance(binding.getRoot().getContext());
        List<Item> itemList = mydb.getInventory();
        System.out.println(itemList);

        ItemListAdapter adapter = new ItemListAdapter(itemList);
        binding.inventoryList.setAdapter(adapter);
        binding.inventoryList.setLayoutManager(new LinearLayoutManager(
                binding.getRoot().getContext()));

        return binding.getRoot();
    }
}

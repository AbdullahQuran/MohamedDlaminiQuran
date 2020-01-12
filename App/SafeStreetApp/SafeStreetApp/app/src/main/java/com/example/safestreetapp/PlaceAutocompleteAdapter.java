package com.example.safestreetapp;

import android.content.Context;

import android.widget.ArrayAdapter;

import android.widget.Filter;
import android.widget.Filterable;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class PlaceAutocompleteAdapter extends ArrayAdapter implements Filterable{

    ArrayList<String> results;

    int resources;
    Context context;

    PlaceApi placeApi=new PlaceApi();


    public PlaceAutocompleteAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context=context;
        this.resources=resource;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
       Filter filter = new Filter(){
           @Override
           protected FilterResults performFiltering(CharSequence constraint) {
               FilterResults filterResults = new FilterResults();
               if(constraint != null){
                   results=placeApi.autocomplete(constraint.toString());

                   filterResults.values=results;
                   filterResults.count=results.size();
               }
               return  filterResults;
           }

           @Override
           protected void publishResults(CharSequence constraint, FilterResults results) {
               if(results!=null && results.count>0){
                    notifyDataSetChanged();
               }
               else{
                   notifyDataSetInvalidated();
               }
           }
       };

       return filter;
    }
}
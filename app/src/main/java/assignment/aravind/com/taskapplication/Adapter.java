package assignment.aravind.com.taskapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aravindhan Software on 07/17/18
 */

public class Adapter extends RecyclerView.Adapter<Adapter.Myholder> {

    List<Gettersetter> list;

    public Adapter(List<Gettersetter> list) {
        this.list = list;
    }

    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        Myholder myHolder = new Myholder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {

        Gettersetter countryModel = list.get(position);
        holder.rank_txtView.setText("RANK - " + String.valueOf(countryModel.getRank()));
        holder.country_txtView.setText("COUNTRY - " + countryModel.getCountry());
        holder.population_txtView.setText("POPULATION - " + String.valueOf(countryModel.getPopulation()));
        String image1 = countryModel.getImage();
        Picasso.get().load(image1).into(holder.imageView);

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    public static class Myholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView rank_txtView;
        TextView country_txtView;
        TextView population_txtView;

        public Myholder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageCountry);
            rank_txtView = (TextView) itemView.findViewById(R.id.rank);
            country_txtView = (TextView) itemView.findViewById(R.id.country);
            population_txtView = (TextView) itemView.findViewById(R.id.population);


        }
    }
}
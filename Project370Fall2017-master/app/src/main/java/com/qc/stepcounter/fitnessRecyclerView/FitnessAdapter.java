package com.qc.stepcounter.fitnessRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.share.widget.ShareButton;
import com.qc.stepcounter.R;

import java.util.List;

/**
 * Created by mohammadnaz on 12/7/17.
 */

public class FitnessAdapter extends RecyclerView.Adapter<FitnessAdapter.FitViewHolder>{
public static List<FitnessData> f;
FBcallback fBcallback;

public FitnessAdapter(List<FitnessData> f, FBcallback fBcallback){
    this.f=f;
    this.fBcallback=fBcallback;

}

    @Override
    public FitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_card,parent,false);
        return new FitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FitViewHolder holder, int position) {
    FitnessData fd=f.get(position);
    holder.steper1.setText(String.valueOf(fd.getSteps())+" steps");
    holder.cal1.setText(String.valueOf(fd.getCalories())+" cals");
    holder.distance1.setText(String.valueOf(fd.getDistance())+" km");


        Glide.with(holder.imageView1.getContext())
                .load("https://lh3.googleusercontent.com/mz5lTfssMMqEM0t6zwzQs96JtH3JgvEpgOXq6BD_TxZc9QgttE8Qdm-apH7Zr__DdyW5=w300")
                .into(holder.imageView1);

    }

    @Override
    public int getItemCount() {
        return f.size();
    }

    class FitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView1;
        TextView steper1;
        TextView cal1;
        TextView distance1;
        Button button1;


        public FitViewHolder(View itemView) {
            super(itemView);

            steper1 = itemView.findViewById(R.id.steperitem);
            cal1 = itemView.findViewById(R.id.calitem);
            distance1 = itemView.findViewById(R.id.dfordistanceitem);
            imageView1 = itemView.findViewById(R.id.image22item);
            //fbshare1 = itemView.findViewById(R.id.fb_share_button2);
            //button=itemView.findViewById(R.id.)
            ///fbshare1.setOnClickListener(this);
            button1 = itemView.findViewById(R.id.coolbutton);
            button1.setOnClickListener(this);
            ///itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == button1.getId()) {
                int p = getAdapterPosition();

                if (p != RecyclerView.NO_POSITION) {
                    fBcallback.fbcall(v, p);
                }
            }
        }
    }


    public interface FBcallback {
        void fbcall(View v, int position);
    }
}

package com.qc.stepcounter.fitnessRecyclerView;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.qc.stepcounter.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mohammadnaz on 12/17/17.
 */

public class FitnessFragment extends Fragment implements FitnessAdapter.FBcallback{
    RecyclerView recyclerView;
    List<FitnessData> fitnessData= new ArrayList<>();
    ShareDialog FB;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ///View view = inflater.inflate(R.layout., container, false);
        View view=inflater.inflate(R.layout.recyclefragment,container,false);
        recyclerView=view.findViewById(R.id.my_recycler_view);
        //FB=view.findViewById(R.id.fb_share_button2);
        FB = new ShareDialog(this);

        createdata();
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
        FitnessAdapter adapter= new FitnessAdapter(fitnessData,this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);



        return view;
    }

    public void createdata(){

        fitnessData.add(new FitnessData(1,1,12));
        fitnessData.add(new FitnessData(2,2,11));
        fitnessData.add(new FitnessData(3,3,10));
        fitnessData.add(new FitnessData(14,4,9));
        fitnessData.add(new FitnessData(15,5,8));
        fitnessData.add(new FitnessData(16,6,7));
        fitnessData.add(new FitnessData(17,7,6));
        fitnessData.add(new FitnessData(18,8,5));
        fitnessData.add(new FitnessData(19,9,4));
        fitnessData.add(new FitnessData(10,10,3));
        fitnessData.add(new FitnessData(11,11,2));
        fitnessData.add(new FitnessData(12,12,1));
    }

    @Override
    public void fbcall(View v, int position) {

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Just wanna share my walking experience ")
                    .setContentDescription(
                            "i have taken today!!!")
                    .setContentUrl(Uri.parse("https://pixelmantras.com/wp-content/uploads/2017/01/How-to-Setup-Pedometer-in-Google-Pixel-Pixel-XL.png"))
                    .setImageUrl(Uri.parse("https://pixelmantras.com/wp-content/uploads/2017/01/How-to-Setup-Pedometer-in-Google-Pixel-Pixel-XL.png"))
                    .setQuote("i walked so many steps today about : "+fitnessData.get(position).getSteps()+ (" and burned " +
                            fitnessData.get(position).getCalories()+ " calories and walked "+ fitnessData.get(position).getDistance()+ " km distance"))

                    .build();

            FB.show(linkContent);
        }

    }
}

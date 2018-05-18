package com.qc.stepcounter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.qc.stepcounter.RestAPI.WeatherFragment;
import com.qc.stepcounter.fitnessRecyclerView.FitnessFragment;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView txtName, txtURL, txtGender,txtBd;
    Button btn2;
    Button logout;
    private ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start();
        start2();
        btn2 = findViewById(R.id.b);
        logout = findViewById(R.id.logout);
        shareDialog = new ShareDialog(this);
        imageView =findViewById(R.id.imgPhoto);
        txtName =findViewById(R.id.txtName);
        txtURL = findViewById(R.id.txtURL);
        txtGender =findViewById(R.id.txtGender);
        txtBd =findViewById(R.id.txtBd);

        getinfo();
        ShareButton fbShareButton = (ShareButton) findViewById(R.id.fb_share_button);
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("The Number of Steps i took today!")
                .setContentDescription(
                        "i've burn some calories today by having taking steps")
                .setContentUrl(Uri.parse("https://pixelmantras.com/wp-content/uploads/2017/01/How-to-Setup-Pedometer-in-Google-Pixel-Pixel-XL.png"))
                .setImageUrl(Uri.parse("https://pixelmantras.com/wp-content/uploads/2017/01/How-to-Setup-Pedometer-in-Google-Pixel-Pixel-XL.png"))

                .build();
        fbShareButton.setShareContent(content);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StepActivity.class);
                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);


            }
        });
    }

    public void start() {
        WeatherFragment weatherFragment = new WeatherFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.weather_card,weatherFragment).commit();
    }
    public void start2() {
        FitnessFragment weatherFragment = new FitnessFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.recyclecontainer,weatherFragment).commit();
    }

    @Override
    public void onBackPressed() {
    }

    private void getinfo(){
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                       try{
                           String gender = object.getString("gender");
                           String birthday = object.getString("birthday");
                           String name = object.getString("name");
                           String id = object.getString("id");

                            txtName.setText(name);
                            txtURL.setText(id);
                            txtGender.setText(gender);
                           txtBd.setText(birthday);
                          if (object.has("picture")) {
                              String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                               Picasso.with(MainActivity.this).load(profilePicUrl).into(imageView);
                           }


                        } catch (JSONException e) {
                           e.printStackTrace();
                     }
                  }
                });
        Bundle bundle1 = new Bundle();
        bundle1.putString("fields", "id,gender,name,birthday,picture.type(large)");
        request.setParameters(bundle1);
        request.executeAsync();

    }



}
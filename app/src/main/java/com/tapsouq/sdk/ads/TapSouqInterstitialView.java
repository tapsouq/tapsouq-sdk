package com.tapsouq.sdk.ads;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tapsouq.sdk.R;

/**
 * Created by dell on 11/1/2016.
 */
public class TapSouqInterstitialView extends Activity {

    private TapSouqInterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_interstitial);

        LinearLayout interBase = (LinearLayout) findViewById(R.id.inter_base);
        interstitialAd = TapSouqInterstitialAd.getInterstitialAd();

        if(interstitialAd.getAdCreative().getType() == AdCreative.AD_TYPE_IMAGE) {

            View view = LayoutInflater.from(this).inflate(R.layout.inter_image, null);
            interBase.addView(view);


            ImageView imageView = (ImageView) view.findViewById(R.id.inter_view);

            Glide.with(this).load(interstitialAd.getAdCreative().getImageFileUrl()).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interstitialAd.adClicked();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(interstitialAd.getAdCreative().getClickUrl()));
                    startActivity(intent);
                }
            });

            view.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interstitialAd.adClosed();
                    finish();
                }
            });
        } else if (interstitialAd.getAdCreative().getType() == AdCreative.AD_TYPE_TEXT){
            View view = LayoutInflater.from(this).inflate(R.layout.inter_text, interBase, false);
            interBase.addView(view);

            ImageView imageView = (ImageView) view.findViewById(R.id.app_icon);

            AdCreative adCreative = interstitialAd.getAdCreative();

            Glide.with(this).load(interstitialAd.getAdCreative().getImageFileUrl()).into(imageView);

            TextView titleView = (TextView) view.findViewById(R.id.inter_title);
            titleView.setText(adCreative.getTitle());

            TextView descriptionView = (TextView) view.findViewById(R.id.inter_description);
            descriptionView.setText(adCreative.getDescription());

            Button button = (Button) view.findViewById(R.id.install_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interstitialAd.adClicked();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(interstitialAd.getAdCreative().getClickUrl()));
                    startActivity(intent);
                }
            });

            view.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interstitialAd.adClosed();
                    finish();
                }
            });

            view.invalidate();

        }

    }

    @Override
    public void onBackPressed() {
        if(interstitialAd!=null)
            interstitialAd.adClosed();
        super.onBackPressed();
    }
}

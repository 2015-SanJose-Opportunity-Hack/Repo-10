package com.kivalocalteam10.kivalocal;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by corey on 10/3/15.
 */
public class KivaApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "gejDCEuSkcVc2TdHblxDKIIRhuK1xneb57kylIMr", "Cm0DWvJhi27AbOQW8Hm0sSCEN3n13wmfh2TUE6qX");
    }
}

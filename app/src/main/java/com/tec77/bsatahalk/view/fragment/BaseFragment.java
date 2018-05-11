package com.tec77.bsatahalk.view.fragment;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tec77.bsatahalk.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Other Fragments can extend BaseFragment. If you define common elements in BaseFragment and all other fragments extend BaseFragments,
 * then all fragments will have these common elements.
 */

public class BaseFragment extends Fragment {

    private Typeface typeface;

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }




    // This event fires 2nd, before views are created for the fragment.
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Cocon® Next Arabic-Light.otf")
                .setFontAttrId(R.attr.fontPath).build());

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


    // This method is called when the fragment is no longer connected to the Activity
    // Any references saved in onAttach should be nulled out here to prevent memory leaks.
    @Override
    public void onDetach() {
        super.onDetach();
    }




    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * to set a font of typeface family
     */
    public void initFonts(View... textViews) {
        for (View view : textViews) {
            if (view instanceof Button)
                ((Button) view).setTypeface(typeface);
            else if (view instanceof TextView)
                ((TextView) view).setTypeface(typeface);
            else if (view instanceof EditText)
                ((EditText) view).setTypeface(typeface);
        }
    }

}

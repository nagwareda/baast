package com.tec77.bsatahalk.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.api.FastNetworkManger;
import com.tec77.bsatahalk.api.request.PushFireBaseTokenRequest;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.utils.CustomTypefaceSpan;
import com.tec77.bsatahalk.utils.FontProvider;
import com.tec77.bsatahalk.view.fragment.AboutUsFragment;
import com.tec77.bsatahalk.view.fragment.AllQuestionsFragment;
import com.tec77.bsatahalk.view.fragment.CategoryFragment;
import com.tec77.bsatahalk.view.fragment.ContactUsFragment;
import com.tec77.bsatahalk.view.fragment.Emla2SerialFragment;
import com.tec77.bsatahalk.view.fragment.HomeFragment;
import com.tec77.bsatahalk.view.fragment.ProfileFragment;
import com.tec77.bsatahalk.view.fragment.SerialListFragment;

import java.util.Locale;

import static com.tec77.bsatahalk.database.SharedPref.editor;

public class HomeActivity extends BaseActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private boolean doubleBackToExitPressedOnce = false;
    private View headerLayout;
    private int itemId;
    private SharedPref pref;
    private TextView languageTxt;
    private ImageView notificationImg;
    boolean langBoolean=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = SharedPref.getInstance(this);
//        if(savedInstanceState == null || langBoolean==false)
           setLocale(getApplicationContext(),"ar");

       // saveLanguage("ar");
        setContentView(R.layout.activity_home);

        initView();
        actionView();

        if (savedInstanceState != null) {
            navigationView.getMenu().getItem(itemId).setChecked(true);
            navigationView.getMenu().performIdentifierAction(itemId, 0);
        }

        if (getIntent().getStringExtra("from") != null && getIntent().getStringExtra("from").equals("contactUs"))
            replaceContentMainFragment(new ContactUsFragment());
        else if (getIntent().getStringExtra("from") != null && getIntent().getStringExtra("from").equals("profile")) {
            replaceContentMainFragment(new ProfileFragment());
        } else
            replaceContentMainFragment(new HomeFragment());

        if (getIntent().getStringExtra("from") != null && getIntent().getStringExtra("from").equals("notification")) {
            if (getIntent().getStringExtra("ka3da") != null)
                displayGrammerAlert(getIntent().getStringExtra("ka3da"));
        }


        Log.d("fbToken", FirebaseInstanceId.getInstance().getToken()+"");
        if (pref.getString("old_token") == null || pref.getString("old_token").isEmpty())
            pushFBToken();


        //apply font on navigation view
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
    }


    private void displayGrammerAlert(final String ka3da) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView textView = new TextView(this);
        textView.setText("");
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setPadding(33, 50, 10, 10);
        builder.setCustomTitle(textView);
        builder.setMessage(ka3da)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                }).show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("selectedNavItem", itemId);
        outState.putBoolean("langBoolean",langBoolean);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        langBoolean = savedInstanceState.getBoolean("langBoolean");
        itemId = savedInstanceState.getInt("selectedNavItem");
    }

    private void initView() {
//        languageTxt = findViewById(R.id.HomeActivity_languageTxt);
//        languageTxt.setText(pref.getString("lan"));
        drawerLayout = findViewById(R.id.HomeActivity_DrawerLayout_drawer);
        navigationView = findViewById(R.id.HomeActivity_navigation_view);
        headerLayout = navigationView.getHeaderView(0); // 0-index header
        toolbar = findViewById(R.id.HomeActivity_Toolbar_toolbar);

    }

    private void actionView() {
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setNavigationOnClickListener(this);
        headerLayout.setOnClickListener(this);
//        languageTxt.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawers();  // CLOSE DRAWER
        itemId = item.getItemId();
        switch (item.getItemId()) {
            case R.id.nav_home: {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");
                navigationView.getMenu().getItem(0).setChecked(true);
                if (fragment != null && fragment.isVisible())
                    return true;
                else {
                    navigationView.getMenu().getItem(0).setChecked(true);
                    replaceContentMainFragment(new HomeFragment());
                }
                break;
            }
            case R.id.nav_profile: {
                replaceContentMainFragment(new ProfileFragment());
                navigationView.getMenu().getItem(1).setChecked(true);
                break;
            }

            case R.id.nav_questions: {
                replaceContentMainFragment(new AllQuestionsFragment());
                navigationView.getMenu().getItem(2).setChecked(true);
                break;
            }
            case R.id.nav_serila: {
                replaceContentMainFragment(new SerialListFragment());
                navigationView.getMenu().getItem(3).setChecked(true);
                break;
            }
            case R.id.nav_emla2Serila: {
                replaceContentMainFragment(new Emla2SerialFragment());
                navigationView.getMenu().getItem(4).setChecked(true);
                break;
            }
            case R.id.nav_class: {
                replaceContentMainFragment(new CategoryFragment());
                navigationView.getMenu().getItem(5).setChecked(true);
                break;
            }
            case R.id.nav_contactUs: {
                replaceContentMainFragment(new ContactUsFragment());
                navigationView.getMenu().getItem(6).setChecked(true);
                break;
            }
            case R.id.nav_aboutUs: {
                replaceContentMainFragment(new AboutUsFragment());
                navigationView.getMenu().getItem(7).setChecked(true);
                break;
            }
            case R.id.nav_logout: {
                alertDialogLogout();
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == headerLayout.getId() && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            replaceContentMainFragment(new ProfileFragment());
        }
//        else if(view.getId() == languageTxt.getId()){
//            if(pref.getString("lan")!=null && pref.getString("lan").equals("en"))
//                setLocale(getApplicationContext(),"ar");
//            else
//                setLocale(getApplicationContext(),"en");
//        }
        else {
            drawerLayout.openDrawer(Gravity.START);
        }

    }

    public void replaceContentMainFragment(Fragment fragment) {
        FragmentManager ft = getSupportFragmentManager();
        if (fragment instanceof HomeFragment)
            ft.beginTransaction()
                    .replace(R.id.HomeActivity_FrameLayout_container, fragment, "HomeFragment")
                    .addToBackStack("HomeFragment")
                    .commit();
        else if (fragment instanceof ProfileFragment)
            ft.beginTransaction()
                    .replace(R.id.HomeActivity_FrameLayout_container, fragment, "ProfileFragment")
                    .addToBackStack("ProfileFragment")
                    .commit();
        else if (fragment instanceof SerialListFragment)
            ft.beginTransaction()
                    .replace(R.id.HomeActivity_FrameLayout_container, fragment, "SerialListFragment")
                    .addToBackStack("SerialListFragment")
                    .commit();
        else if (fragment instanceof Emla2SerialFragment)
            ft.beginTransaction()
                    .replace(R.id.HomeActivity_FrameLayout_container, fragment, "Emla2SerialFragment")
                    .addToBackStack("Emla2SerialFragment")
                    .commit();
        else if (fragment instanceof CategoryFragment)
            ft.beginTransaction()
                    .replace(R.id.HomeActivity_FrameLayout_container, fragment, "CategoryFragment")
                    .addToBackStack("CategoryFragment")
                    .commit();
        else if (fragment instanceof ContactUsFragment)
            ft.beginTransaction()
                    .replace(R.id.HomeActivity_FrameLayout_container, fragment, "ContactUsFragment")
                    .addToBackStack("ContactUsFragment")
                    .commit();
        else if (fragment instanceof AboutUsFragment)
            ft.beginTransaction()
                    .replace(R.id.HomeActivity_FrameLayout_container, fragment, "AboutUsFragment")
                    .addToBackStack("AboutUsFragment")
                    .commit();
        else
            ft.beginTransaction().replace(R.id.HomeActivity_FrameLayout_container, fragment).addToBackStack(null)
                    .commit();

        ft.executePendingTransactions();
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = FontProvider.getFont(HomeActivity.this);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void alertDialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.logout));
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setPadding(33, 50, 10, 10);
        builder.setCustomTitle(textView);
        builder.setMessage(getString(R.string.sureLogout))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteFBToken();
                        LoginManager.getInstance().logOut();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(getApplicationContext(), splashSliderActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void pushFBToken() {
        PushFireBaseTokenRequest body = new PushFireBaseTokenRequest();
        body.setToken(FirebaseInstanceId.getInstance().getToken());
        Log.d("fbId", FirebaseInstanceId.getInstance().getToken());
        body.setUser_id(pref.getInt("id"));
        new FastNetworkManger(HomeActivity.this).PushFireBaseToken(body);

    }

    private void deleteFBToken() {
        new FastNetworkManger(HomeActivity.this).deleteFireBaseToken(SharedPref.getInstance(this).getInt("id"));
    }

    @Override
    public void onBackPressed() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        int counterFraghment = fm.getBackStackEntryCount();
        if (counterFraghment > 1 && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
            fm.popBackStack();
            android.support.v4.app.FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(counterFraghment - 2);
            String tag = backEntry.getName();
            if (!tag.isEmpty())
                if (tag.contains("HomeFragment"))
                    navigationView.getMenu().getItem(0).setChecked(true);
                else if (tag.contains("ProfileFragment"))
                    navigationView.getMenu().getItem(1).setChecked(true);
                else if (tag.contains("SerialListFragment"))
                    navigationView.getMenu().getItem(2).setChecked(true);
                else if (tag.contains("Emla2SerialFragment"))
                    navigationView.getMenu().getItem(3).setChecked(true);
                else if (tag.contains("CategoryFragment"))
                    navigationView.getMenu().getItem(4).setChecked(true);
                else if (tag.contains("AboutUsFragment"))
                    navigationView.getMenu().getItem(5).setChecked(true);

        } else if (counterFraghment == 1 && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();  // CLOSE DRAWER
        } else {
            //if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            super.onBackPressed();
               // return;
           // }
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, getString(R.string.confirm_exit_message), Toast.LENGTH_SHORT).show();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    doubleBackToExitPressedOnce = false;
//                }
//            }, 2000);
        }
    }

    //to start arabic language in application
    private void saveLanguage(String lang){
        SharedPref preferences =SharedPref.getInstance(this);
        editor.putString("language",lang);
        editor.apply();
    }

    private void setLocale(final Context ctx, String lang) {
        pref.putString("lan",lang);
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(myLocale);
            ctx.createConfigurationContext(config);
            ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());

        }else{
            config.locale=myLocale;
            getApplicationContext().getResources().updateConfiguration(config,null);
        }
       // restartActivity();

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        SharedPref preferences =SharedPref.getInstance(this);

    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
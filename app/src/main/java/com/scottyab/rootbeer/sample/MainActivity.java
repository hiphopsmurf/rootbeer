package com.scottyab.rootbeer.sample;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.scottyab.rootbeer.RootBeer;
import com.scottyab.rootbeer.util.Utils;


public class MainActivity extends ActionBarActivity {

    private static String GITHUB_LINK = "https://github.com/scottyab/rootbeer";

    private View resultsContainer;
    private TextView results;
    private AlertDialog infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.rootCheckButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRootCheck();
            }
        });

        resultsContainer = findViewById(R.id.resultsContainer);
        results = (TextView)findViewById(R.id.rootResults);

    }

    private void doRootCheck() {
        RootBeer check = new RootBeer(this);

        if(check.isRooted()){
            //we found indication of root
            doPropertyAnimatorReveal(getResources().getColor(R.color.fail));
        }else{
            //we didn't find indication of root
            doPropertyAnimatorReveal(getResources().getColor(R.color.pass));
        }

        StringBuilder b = new StringBuilder();
        b.append("If any of the below are true the root check \'might\' indicate device is rooted\n");

        b.append("\ndetectRootManagementApps: ");
        b.append(check.detectRootManagementApps());

        b.append("\ndetectPotentiallyDangerousApps: ");
        b.append(check.detectPotentiallyDangerousApps());

        b.append("\ndetectTestKeys: ");
        b.append(check.detectTestKeys());

        b.append("\ncheckForBusyBoxBinary: ");
        b.append(check.checkForBusyBoxBinary());

        b.append("\ncheckForSuBinary: ");
        b.append(check.checkForSuBinary());

        b.append("\ncheckSuExists: ");
        b.append(check.checkSuExists());

        b.append("\ncheckForRWPaths: ");
        b.append(check.checkForRWPaths());

        b.append("\ncheckForDangerousProps: ");
        b.append(check.checkForDangerousProps());

        b.append("\ncheckForRootNative: ");
        b.append(check.checkForRootNative());

        b.append("\ndetectRootCloakingApps: ");
        b.append(check.detectRootCloakingApps());

        b.append("\nisSelinuxFlagInEnabled? (experimental) ");
        b.append(Utils.isSelinuxFlagInEnabled());

        results.setText(b.toString());
    }


    public void checkForRoot(Context context){
        RootBeer rootBeer = new RootBeer(context);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_github) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(GITHUB_LINK));
            startActivity(i);
            return true;
        }else  if (id == R.id.action_info) {
            showInfoDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        if(infoDialog!=null && infoDialog.isShowing()){
            //do nothing if already showing
        }else {
            infoDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.info_details)
                    .setCancelable(true)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("More info", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(GITHUB_LINK)));
                        }
                    })
                    .create();
            infoDialog.show();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void doPropertyAnimatorReveal(Integer colorTo) {
        Integer colorFrom = Color.TRANSPARENT;
        Drawable background = resultsContainer.getBackground();
        if (background instanceof ColorDrawable){
            colorFrom = ((ColorDrawable) background).getColor();
        }

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(500);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                resultsContainer.setBackgroundColor((Integer) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

}

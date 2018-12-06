/*
 *   Copyright (c) 2018 BigStep Technologies Private Limited.
 *
 *   The distribution of this source code is prohibited.
 */

package com.channelize.android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.channelize.android.pushnotification.MyFcmListenerService;
import com.socialengineaddons.messenger.Constants;
import com.socialengineaddons.messenger.MQTTClient;
import com.socialengineaddons.messenger.MessengerActivity;
import com.socialengineaddons.messenger.interfaces.OnConversationClickListener;
import com.socialengineaddons.messenger.interfaces.OnResponseListener;
import com.socialengineaddons.messenger.utils.GlobalFunctionsUtil;
import com.socialengineaddons.messenger.utils.Logcat;
import com.socialengineaddons.messenger.utils.MessengerDatabaseUtils;
import com.socialengineaddons.messenger.utils.PrimePreferencesUtils;
import com.socialengineaddons.messenger.utils.SnackbarUtils;

import org.json.JSONObject;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnConversationClickListener {

    // Member variables.
    private Context mContext;
    private TextInputLayout ilEmail, ilPassword;
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin, btnLogout;
    private ProgressDialog progressDialog;
    private MessengerDatabaseUtils messengerDatabaseUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        mContext = this;
        messengerDatabaseUtils = MessengerDatabaseUtils.getInstance();
        String currentUserId = PrimePreferencesUtils.getCurrentUserId(mContext);
        if (currentUserId != null && !currentUserId.isEmpty()
                && !currentUserId.equals("null")) {
            messengerDatabaseUtils.setCurrentUserId(currentUserId);
            messengerDatabaseUtils.enableFirstTimeAppLaunch();
        }
        messengerDatabaseUtils.setOnConversationClickListener(this);
        initViews();
        invalidateOptionsMenu();

        Logcat.d(MainActivity.class, "Inside Login Activity");
        if (getIntent().getExtras() != null) {
            Logcat.d(MainActivity.class.getSimpleName(), "GetIntent not null");
            Bundle chatInfo = getIntent().getExtras();
            Intent intent = new Intent(mContext, MessengerActivity.class);
            intent.putExtra("package_name", mContext.getPackageName());
            if (chatInfo != null && chatInfo.containsKey(Constants.CHAT_ID)) {
                chatInfo = messengerDatabaseUtils.getPushNotificationData(mContext, chatInfo);
                intent.putExtras(chatInfo);
            }
            startMainActivity(chatInfo);
        } else {
            startMainActivity(null);
        }

        if (!(MessengerDatabaseUtils.getApiDefaultUrl() != null
                && !MessengerDatabaseUtils.getApiDefaultUrl().isEmpty()
                && MessengerDatabaseUtils.getMQTTServerUrl() != null
                && !MessengerDatabaseUtils.getMQTTServerUrl().isEmpty()
                && MessengerDatabaseUtils.getApiKey() != null
                && !MessengerDatabaseUtils.getApiKey().isEmpty())) {
            Toast.makeText(mContext, "Please fill all the messenger information", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        ilEmail = findViewById(R.id.emailWrapper);
        ilPassword = findViewById(R.id.passwordWrapper);
        etEmail = findViewById(R.id.email_field);
        etPassword = findViewById(R.id.password_field);
        btnLogin = findViewById(R.id.login_button);
        btnLogout = findViewById(R.id.logout_button);

        btnLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        setViewsVisibility(messengerDatabaseUtils.getCurrentUserId() != null
                && !messengerDatabaseUtils.getCurrentUserId().isEmpty());

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    private void setViewsVisibility(boolean isLoggedInUser) {
        ilEmail.setVisibility(isLoggedInUser ? View.GONE : View.VISIBLE);
        ilPassword.setVisibility(isLoggedInUser ? View.GONE : View.VISIBLE);
        btnLogin.setVisibility(isLoggedInUser ? View.GONE : View.VISIBLE);
        btnLogout.setVisibility(isLoggedInUser ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mContext != null) {
            setViewsVisibility(messengerDatabaseUtils.getCurrentUserId() != null
                    && !messengerDatabaseUtils.getCurrentUserId().isEmpty());
            invalidateOptionsMenu();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu_item, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_messenger).setVisible(messengerDatabaseUtils.getCurrentUserId() != null
                && !messengerDatabaseUtils.getCurrentUserId().isEmpty());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_messenger) {
            startMainActivity(null);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                if (checkFields()) {
                    GlobalFunctionsUtil.hideKeyboard(mContext);
                    progressDialog.setMessage("Logging-In ...");
                    progressDialog.show();
                    messengerDatabaseUtils.loginInWithEmailAndPassword(mContext,
                            etEmail.getText().toString(), true, 0, new OnResponseListener() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    progressDialog.dismiss();
                                    MQTTClient.getInstance(mContext);
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    errorMessage = (errorMessage == null || errorMessage.isEmpty())
                                            ? mContext.getResources().getString(R.string.pm_something_went_wrong) : errorMessage;
                                    SnackbarUtils.displaySnackbar(btnLogin, errorMessage);
                                    progressDialog.dismiss();
                                }
                            });
                }
                break;

            case R.id.logout_button:
                new Logout().execute();
                break;
        }

    }

    private void startMainActivity(Bundle chatInfo) {
        Logcat.d(MainActivity.class.getSimpleName(), "startMainActivity");
        if (messengerDatabaseUtils.getCurrentUserId() != null
                && !messengerDatabaseUtils.getCurrentUserId().isEmpty()) {
            Intent intent = new Intent(mContext, MessengerActivity.class);
            intent.putExtra("package_name", mContext.getPackageName());
            if (chatInfo != null) {
                intent.putExtras(chatInfo);
            }
            startActivity(intent);
        }
    }

    protected boolean checkFields() {
        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onChatOpened(String chatId) {

    }

    /* Executing background task for sending post request for sing out */
    public class Logout extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Logging out ...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            messengerDatabaseUtils.logoutUser(mContext);
            messengerDatabaseUtils.clearCache();
            MyFcmListenerService.clearMessengerPushNotification();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                setViewsVisibility(false);
                messengerDatabaseUtils.setCurrentUserId("");
                invalidateOptionsMenu();
            }
        }
    }
}

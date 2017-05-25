package com.eventx.moviex;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eventx.moviex.LoginAccount.Account;
import com.eventx.moviex.MovieActivities.MoviesActivity;
import com.eventx.moviex.MovieFragments.NavigationDrawerFragment;
import com.eventx.moviex.MovieModels.RequestToken;
import com.eventx.moviex.MovieModels.SessionId;
import com.eventx.moviex.MovieModels.ValidRequest;
import com.eventx.moviex.Network.ApiClient;
import com.eventx.moviex.Network.ApiInterface;
import com.eventx.moviex.PeopleActivities.PopularPeopleActivity;
import com.eventx.moviex.TvActivities.TvActivity;
import com.eventx.moviex.Wishlist.WishlistAcitvity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Html.fromHtml;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailET;
    private EditText mPasswordET;
    private Button mLoginBtn;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ProgressDialog mProgressBar;

    TextView signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp=getSharedPreferences("MovieX",MODE_PRIVATE);
        editor=sp.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");

        mProgressBar=new ProgressDialog(this);
        signUp=(TextView)findViewById(R.id.register);
        signUp.setPaintFlags(signUp.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Uri uri = Uri.parse("https://www.themoviedb.org/account/signup");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                overridePendingTransition(R.anim.slide_right,R.anim.no_change);
            }
        });
        mEmailET = (EditText) findViewById(R.id.email_field);
        mPasswordET = (EditText) findViewById(R.id.password_field);
        mLoginBtn = (Button) findViewById(R.id.login_button);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                generateRequestToken();
            }
        });

        NavigationDrawerFragment drawerFragment=(NavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_nav);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout),toolbar);

    }

    private void generateRequestToken() {
        final String email = mEmailET.getText().toString().trim();
        final String password = mPasswordET.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mProgressBar.setMessage("Logging In");
            mProgressBar.show();
            ApiInterface apiInterface = ApiClient.getApiInterface();
            Call<RequestToken> tokenCall = apiInterface.getRequestToken();
            tokenCall.enqueue(new Callback<RequestToken>() {
                @Override
                public void onResponse(Call<RequestToken> call, Response<RequestToken> response) {
                    if (response.isSuccessful()) {
                        String requestToken = response.body().getRequest_token();
                        validateRequestToken(email, password, requestToken);
                    }
                }

                @Override
                public void onFailure(Call<RequestToken> call, Throwable t) {
                    if(t.getMessage().contains("Unable to resolve host")){
                        mProgressBar.dismiss();
                        AlertDialog.Builder dialog=new AlertDialog.Builder(LoginActivity.this);
                        dialog.setTitle("No Internet Detected");
                        dialog.setMessage("Turn On mobile data/ wifi ");
                        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });

                        dialog.create().show();
                    }
                }
            });
        }else{
            AlertDialog.Builder dialog=new AlertDialog.Builder(LoginActivity.this);
            dialog.setMessage("Enter username and password ");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            dialog.create().show();
        }
    }

    private void validateRequestToken(String email, String password, final String requestToken) {
        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<ValidRequest> validate = apiInterface.validateRequest(email, password, requestToken);
        validate.enqueue(new Callback<ValidRequest>() {
            @Override
            public void onResponse(Call<ValidRequest> call, Response<ValidRequest> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        createSession(requestToken);
                    }
                }else{
                    mProgressBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ValidRequest> call, Throwable t) {

            }
        });
    }

    private void createSession(final String requestToken) {
        final ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<SessionId> sessionIdCall = apiInterface.getSessionId(requestToken);
        sessionIdCall.enqueue(new Callback<SessionId>() {
            @Override
            public void onResponse(Call<SessionId> call, Response<SessionId> response) {
                if (response.isSuccessful()) {

                    Call<Account> accountCall=apiInterface.getAccountId(response.body().getSession_id());
                    accountCall.enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            if(response.isSuccessful()){
                                editor.putLong("account",response.body().getId());
                                editor.commit();
                            }
                        }

                        @Override
                        public void onFailure(Call<Account> call, Throwable t) {

                        }
                    });
                    editor.putString("session",response.body().getSession_id());
                    editor.commit();
                    mProgressBar.dismiss();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SessionId> call, Throwable t) {

            }
        });
    }



}

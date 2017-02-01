package np.com.sanjeebojha.sanjeeb;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

public class RegistrationActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int REQUEST_READ_CONTACTS = 0;
    private final String TAG= Constants.TAG_ACTIVITY_REGISTRATION;

    private UserRegistrationTask mRegistrationTask = null;

    private View mProgressView;
    private View mRegistrationView;
    private AutoCompleteTextView mFirstNameView;
    private AutoCompleteTextView mLastNameView;
    private AutoCompleteTextView mDisplayNameView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private String ApplicationName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mFirstNameView = (AutoCompleteTextView) findViewById(R.id.a2_firstname);
        mLastNameView = (AutoCompleteTextView) findViewById(R.id.a2_lastname);
        mDisplayNameView = (AutoCompleteTextView) findViewById(R.id.a2_displayname);
        ApplicationName  = getPackageName();
        mEmailView = (AutoCompleteTextView) findViewById(R.id.a2_email);
        mPasswordView = (EditText) findViewById(R.id.a2_password);

        Button mRegisterationButton = (Button)findViewById(R.id.a2_registration_button);
        mRegisterationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptRegistration();
            }
        });

        mRegistrationView = findViewById(R.id.a2_registration_form);
        mProgressView = findViewById(R.id.a2_registration_progress);
    }

    private void AttemptRegistration() {

        //Reset Errors
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mDisplayNameView.setError(null);


        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String firstname = mFirstNameView.getText().toString();
        String lastname = mLastNameView.getText().toString();
        String displayname = mDisplayNameView.getText().toString();
        String username = email;
        //On error select this view
        View focusView = null;
        boolean cancel = false;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if(cancel){
            focusView.requestFocus();
        }else{
            showProgress(true);
            mRegistrationTask = new UserRegistrationTask(firstname,lastname,displayname,email,password,ApplicationName);
            mRegistrationTask.execute((Void) null);
        }

    }

    //region Form Validation
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if(email.length()<4) return  false;
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        if(password.length()>32) return false;
        return password.length() > 4;
    }
    //endregion

    //region EMAIL Auto complete methods
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
    //endregion

    //region  LoaderManager OverRideMethods
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
    //endregion

    //region ProgressBar
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegistrationView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegistrationView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegistrationView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegistrationView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    //endregion

    //region RegistrationTask WebService
    public class UserRegistrationTask extends AsyncTask<Void,Void,Boolean>{
        public ProgressDialog progressDialog;
        //private final String mFirstName;
         final String FirstName;
         final String LastName;
         final String DisplayName;
         final String Email;
         final String Password;
         final String ApplicationName;
        UserRegistrationTask(String firstName, String lastName, String displayName, String email, String password, String applicationName){

            FirstName = firstName;
            LastName = lastName;
            DisplayName = displayName;
            Email = email;
            Password = password;
            ApplicationName = applicationName;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressDialog.dismiss();
                        try{
                            JSONObject obj = new JSONObject(response);
                            Common.SetToast(getApplicationContext(),obj.getString("message"),true);
                        }catch (JSONException e){
                            Log.e(TAG,e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();

                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                Log.e(TAG,e1.getMessage());
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                Log.e(TAG,e2.getMessage());
                                e2.printStackTrace();
                            }
                        }
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("UserName",Email);
                        params.put("FirstName",FirstName);
                        params.put("LastName",LastName);
                        params.put("DisplayName",DisplayName);
                        params.put("Email",Email);
                        params.put("Password",Password);
                        params.put("ApplicationName",ApplicationName);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }catch (Exception e) {
                //Common.SetToast(getApplicationContext(),"Error:" + e.getMessage(),true);
                Log.e(TAG,e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mRegistrationTask=null;
            showProgress(false);
            if(success){

            }else{
                mEmailView.setError("Cannot register the User");
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mRegistrationTask=null;
            showProgress(false);
            super.onCancelled();
        }
    }

}

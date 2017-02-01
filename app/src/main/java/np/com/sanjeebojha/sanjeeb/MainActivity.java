package np.com.sanjeebojha.sanjeeb;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        startFireBaseService();
    }

    private void startFireBaseService() {
        Intent service1 = new Intent(this,SVCFireBaseIdService.class);
        startService(service1);
        Intent service2 = new Intent(this,SVCFirebaseMessagingService.class);
        startService(service2);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Common.SetToast(this,MODSharedPrefManager.getInstance(this).getSession(),true);

            try {
                Common.SetToast(this,Common.ConvertDate(MODSharedPrefManager.getInstance(this).getLoginValidTillDate()),true);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if (id==R.id.nav_registeration){

            Intent intent = new Intent(this,RegistrationActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_login){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_send) {
            String Token = MODSharedPrefManager.getInstance(this).getDeviceToken();
            if(Token==null)
                Token= Constants.STRING_TOKEN_NOT_GENERATED;
            TextView message = (TextView) findViewById(R.id.a1_message_text);
            message.setText(Token);
        }else if(id==R.id.a1_get_message){
            GetMessage();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private  RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;



    public void GetMessage(){
        String[] params = {"UserID","1"};
        List<ContactMessage> messageList =null;
        try {
            TextView tv = (TextView) findViewById(R.id.textView);
            messageList=  MODWebServiceManager.Post("http://sanjeebojha.azurewebsites.net/Home/GetMessages",params);
        } catch (ExecutionException e) {
            String error = e.getMessage();
            e.printStackTrace();
            Common.SetToast(this,error,true);
        } catch (InterruptedException e) {
            String error =e.getMessage();
            e.printStackTrace();
            Common.SetToast(this,error,true);
        }

        if(messageList!=null){
            //Load RecyclerView with Card View
            recyclerView= (RecyclerView) findViewById(R.id.a1_recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(recyclerLayoutManager);

            recyclerAdapter = new MainActivityRecyclerViewAdapter(messageList);
            recyclerView.setAdapter(recyclerAdapter);

            //((RecyclerView.Adapter)recyclerAdapter).setOn
           /* int index=0;
            for (index=0;index<messageList.size();index++) {
                ((MainActivityRecyclerViewAdapter) recyclerAdapter).addItem(messageList.get(index),index);
            }*/


        }
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();

    }


}

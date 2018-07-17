package assignment.aravind.com.taskapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ir.mahdi.mzip.zip.ZipArchive;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by aravindhan Software on 07/17/18
 */
public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Gettersetter> list;
    String responseString;
    JSONObject jsonObject;
    FloatingActionButton btn_saveContacts;
    Boolean permissionsBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btn_saveContacts = (FloatingActionButton) findViewById(R.id.saveContacts);

        getCountryData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                Intent intent = new Intent(MainActivity.this, ImageClass.class);
                ImageView imageView = (ImageView) view.findViewById(R.id.imageCountry);
                Drawable drawable = imageView.getDrawable();
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
                Bitmap bitmap = bitmapDrawable.getBitmap();
                intent.putExtra("DATA", bitmap);
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        btn_saveContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        });

    }

    public void getCountryData() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.androidbegin.com/tutorial/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        JSON apiService = retrofit.create(JSON.class);
        apiService.getFile("http://www.androidbegin.com/tutorial/jsonparsetutorial.txt")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Log.d("FINISHED", "COMPLETED TRANSFER");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("FINISH ONERROR", e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            responseString = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Log.d("FINISH", responseString);

                        try {
                            jsonObject = new JSONObject(responseString);
                            Log.d("FINISH", jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        list = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.optJSONArray("worldpopulation");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gettersetter countryModel = new Gettersetter();
                            JSONObject jsonobject = null;
                            try {
                                jsonobject = jsonArray.getJSONObject(i);
                                String rank = jsonobject.getString("rank");
                                countryModel.setRank(Integer.valueOf(rank));
                                String country = jsonobject.getString("country");
                                countryModel.setCountry(country);
                                String population = jsonobject.getString("population");
                                Long l = Long.parseLong(population.replaceAll(",", ""));
                                countryModel.setPopulation(l);
                                String image = jsonobject.getString("flag");
                                countryModel.setImage(image);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            list.add(countryModel);
                            Log.d("Country", list.toString());

                        }

                        Adapter adapter = new Adapter(list);
                        RecyclerView.LayoutManager recyce = new LinearLayoutManager(MainActivity.this);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setLayoutManager(recyce);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    permissionsBool = true;
                    doBackgroundTasks();

                } else {

                    permissionsBool = false;

                    Toast.makeText(MainActivity.this, "Permissions are Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    public void doBackgroundTasks() {

        Observable<String> myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {

                        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                        Log.d("Contact count", String.valueOf(cursor.getCount()));

                        List<String[]> data = new ArrayList<String[]>();

                        String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath().concat("/contactt.csv");
                        CSVWriter writer = null;
                        try {
                            writer = new CSVWriter(new FileWriter(csv));
                        } catch (IOException e) {
                            Log.d("Error", e.getMessage());
                            e.printStackTrace();
                        }

                        while (cursor.moveToNext()) {

                            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            data.add(new String[]{id, name, phoneNumber});

                        }

                        if (writer != null) {
                            writer.writeAll(data);
                        } else {
                        }
                        cursor.close();
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ZipArchive zipArchive = new ZipArchive();
                        zipArchive.zip(android.os.Environment.getExternalStorageDirectory().getAbsolutePath().concat("/contactt.csv")
                                , android.os.Environment.getExternalStorageDirectory().getAbsolutePath().concat("/contactt.zip"), "");

                        sub.onCompleted();
                    }
                }
        );

        myObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onNext(String s) {
                    }

                    @Override
                    public void onCompleted() {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), ".Zip has been saved to the Root Directory of Your Phone", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("FINISH", e.getMessage());
                        e.printStackTrace();
                    }
                });

    }

}

package com.example.barril.ui.googlelens;

import static com.android.volley.Response.*;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.barril.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageScreen;
    private Button buscarInfo;
    private RecyclerView resultRV;
    private Button scan;
    private Bitmap imageBitmap;
    SearchResultsRVAdapter searchResultsRVAdapter;
    ArrayList<SearchModel> dataModalArrayList;
    String title, link, displayed_link, snippet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gl_mainscreen);
        imageScreen = findViewById(R.id.imagenCamara);
        buscarInfo = findViewById(R.id.idBuscar);
        resultRV = findViewById(R.id.listRecyclerView);
        scan = findViewById(R.id.idBotonSscan);


        dataModalArrayList = new ArrayList<>();

        searchResultsRVAdapter = new SearchResultsRVAdapter(dataModalArrayList, MainScreen.this);

        LinearLayoutManager manager = new LinearLayoutManager(MainScreen.this, LinearLayoutManager.HORIZONTAL, false);

        resultRV.setLayoutManager(manager);
        resultRV.setAdapter(searchResultsRVAdapter);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to capture an image.
                dispatchTakePictureIntent();
            }

        });

        // adding on click listener for our button to search data.
        buscarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to get search results.
                getResults();
            }
        });
    }

    private void getResults() {
        //En este método estamos llamando al firebase image
        // y pasamos nuestra imagenBitMap a Firebase
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);

        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
        labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                String searchQuery = firebaseVisionImageLabels.get(0).getText();
                searchData(searchQuery);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // displaying error message.
                Toast.makeText(MainScreen.this, "Fail to detect image..", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //ESTO ESTA PENDIENTE DE MEJORA
    private void searchData(String searchQuery) {
        String apiKey = "dd0646cdbbdcbdf4eee229a553c1877f47dcdb05f01b37cb6e6058d3be2eb5ef";
        String url = "https://serpapi.com/search.json?q=" + searchQuery.trim() + "&q=Mahou&location=Madrid,Spain&hl=es&gl=es&google_domain=google.com&api_key=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(MainScreen.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // on below line we are extracting data from our json.
                    JSONArray organicResultsArray = response.getJSONArray("organic_results");
                    for (int i = 0; i < organicResultsArray.length(); i++) {
                        JSONObject organicObj = organicResultsArray.getJSONObject(i);
                        if (organicObj.has("title")) {
                            title = organicObj.getString("title");
                        }
                        if (organicObj.has("link")) {
                            link = organicObj.getString("link");
                        }
                        if (organicObj.has("displayed_link")) {
                            displayed_link = organicObj.getString("displayed_link");
                        }
                        if (organicObj.has("snippet")) {
                            snippet = organicObj.getString("snippet");
                        }
                        // on below line we are adding data to our array list.
                        dataModalArrayList.add(new SearchModel(title, link, displayed_link, snippet));
                    }
                    // notifying our adapter class
                    // on data change in array list.
                    searchResultsRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // displaying error message.
                Toast.makeText(MainScreen.this, "No Result found for the search query..", Toast.LENGTH_SHORT).show();
            }
        });
        // adding json object request to our queue.
        queue.add(jsonObjectRequest);
    }

    //método para capturar la imagen
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // calling a start activity for result when image is captured.
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // inside on activity result method we are
        // setting our image to our image view from bitmap.
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            // on below line we are setting our
            // bitmap to our image view.
            imageScreen.setImageBitmap(imageBitmap);
        }
    }

}
    




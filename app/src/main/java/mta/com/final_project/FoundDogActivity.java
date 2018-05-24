package mta.com.final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FoundDogActivity extends AppCompatActivity {

    private Spinner animalTypeSpinner;
    private ImageView mapImageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_dog);

        initViews();
        chooseLocationHandler();
    }

    private void initViews(){
        addItemsToSpinner();
        mapImageView = findViewById(R.id.mapImageView_FoundDogActivity);
        progressBar = findViewById(R.id.progressBar_FoundDogActivity);
    }

    private void addItemsToSpinner() {
        animalTypeSpinner = findViewById(R.id.animalTypeSpinner_FoundDogActivity);
        List<String> spinnerItemsList = new ArrayList<>();
        spinnerItemsList.add("Type of Animal");
        spinnerItemsList.add("Dog");
        spinnerItemsList.add("Cat");
        spinnerItemsList.add("Other");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerItemsList);
        animalTypeSpinner.setAdapter(dataAdapter);

    }

    private void chooseLocationHandler() {
        mapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoundDogActivity.this, MapsActivity.class));
            }
        });
    }
}

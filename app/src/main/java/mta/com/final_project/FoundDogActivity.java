package mta.com.final_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FoundDogActivity extends AppCompatActivity {

    private Spinner animalTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_dog);

        initViews();
    }

    private void initViews(){
        addItemsToSpinner();
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
}

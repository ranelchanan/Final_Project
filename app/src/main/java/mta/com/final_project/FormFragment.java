package mta.com.final_project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import mta.com.final_project.model.AnimalCardDetails;

import static android.app.Activity.RESULT_OK;

public class FormFragment extends Fragment {

    private View view;
    //    private Spinner animalTypeSpinner;
//    private ImageView mapImageView;
    private TextView titleTextView;
    private Button mapButton;
    private CheckBox iHaveItCheckBox;
    private ImageButton cameraIconImageButton;
    private ImageView addPhotoImageView;
    private ImageView userPhotoImageView;
    private ProgressBar progressBar;
    private Spinner foundOrLostSpinner;
    private Button submitButton;
    private EditText descriptionEditText;
    private DatabaseReference animalDetailsDB;
    private StorageReference mStorageRef;
    private Uri photoUri;
    private DatabaseReference userDB;
    private RadioButton currentLocationRadioButton;
    private RadioButton enterLocationRadioButton;
    private EditText enterLocationEditText;
    private RadioGroup radioGroup;

    //    private String animalType;
    private Geocoder geocoder;
    private List<Address> addresses;
    private String location;
    private String foundOrLostStr;
    private String descriptionStr;
    private String titleStr;
    private String animalPhotoUrlStr;
    private String userIdStr;
    private String timeAndDateStr;
    private String locationStr;


    private boolean iHaveItBool;

    private final String FOUND;
    private final String LOST;
    private final String FOUND_LOST;


    public FormFragment() {
        FOUND_LOST = "Found/Lost Animal";
        FOUND = "Found";
        LOST = "Lost";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_form, container, false);
        initViews();
        onChooseLocation();
        onClickSubmitHandler();
        onClickAddPhotoHandler();
        onClickCameraIconHandler();
        onChooseItemFoundOrLostSpinnerHandler();
        return view;
    }


    private void initViews() {

        addItemsToFoundOrLostSpinner();
//        mapImageView = view.findViewById(R.id.mapImageView_FoundAnimalForm);
        titleTextView = view.findViewById(R.id.titleEditText_formFragment);
//        mapButton = view.findViewById(R.id.mapButton_formFragment);
        currentLocationRadioButton = view.findViewById(R.id.currentLocationRadioButton_formFragment);
        enterLocationRadioButton = view.findViewById(R.id.enterLocationRadioButton_formFragment);
        enterLocationEditText = view.findViewById(R.id.enterLocationEditText_formFragment);
        radioGroup = view.findViewById(R.id.radioGroup_formFragment);
        progressBar = view.findViewById(R.id.progressBar_formFragment);
        submitButton = view.findViewById(R.id.submitButton_formFragment);
        descriptionEditText = view.findViewById(R.id.descriptionEditText_formFragment);
        iHaveItCheckBox = view.findViewById(R.id.IHaveItCheckBox_formFragment);
        cameraIconImageButton = view.findViewById(R.id.cameraIcon_formFragment);
        addPhotoImageView = view.findViewById(R.id.addPhotoImageView_formFragment);
        animalDetailsDB = FirebaseDatabase.getInstance().getReference().child("animalDetails");
        userIdStr = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        userDB = FirebaseDatabase.getInstance().getReference().child("users");
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

    }

    private void openCameraAndGallery() {
        progressBar.setVisibility(View.VISIBLE);
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(4, 3)
                .start(getContext(), FormFragment.this);
    }

    private void onClickAddPhotoHandler() {
        addPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraAndGallery();
            }
        });
    }

    private void onClickCameraIconHandler() {
        cameraIconImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraAndGallery();
            }
        });
    }

    private void addItemsToFoundOrLostSpinner() {
        foundOrLostSpinner = view.findViewById(R.id.foundOrLost_formFragment);
        List<String> spinnerItemsList = new ArrayList<>();
        spinnerItemsList.add(FOUND_LOST);
        spinnerItemsList.add(FOUND);
        spinnerItemsList.add(LOST);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerItemsList);
        foundOrLostSpinner.setAdapter(dataAdapter);
    }

    private void onChooseLocation() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == currentLocationRadioButton.getId()) {
                    enterLocationEditText.setEnabled(false);
                } else if (checkedId == enterLocationRadioButton.getId()) {
                    enterLocationEditText.setEnabled(true);
                }
            }
        });

    }

    private void onChooseItemFoundOrLostSpinnerHandler() {
        foundOrLostSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                boolean isEnabled = false;
                if (foundOrLostSpinner.getSelectedItem().toString().equals(FOUND)) {
                    isEnabled = true;
                }

                iHaveItCheckBox.setEnabled(isEnabled);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        String city = getAddress(location.getLatitude(), location.getLongitude());
                        Log.i("City location", city);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Not found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private String getCurrentLocation() {
        String address = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        } else {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                address = getAddress(location.getLatitude(), location.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Not found!", Toast.LENGTH_SHORT).show();
            }
        }

        return address;
//
    }

    private String getAddress(double lat, double lon) {
        String address = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 10);
            address = addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    private void onClickSubmitHandler() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFormValues();

                if (foundOrLostSpinner.getSelectedItem().toString().equals(FOUND_LOST)) {
                    TextView errorText = (TextView)foundOrLostSpinner.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                } else if (titleStr.isEmpty()) {
                    titleTextView.setError("please enter a title");
                    titleTextView.requestFocus();
                } else if (descriptionEditText.getText().toString().isEmpty()) {
                    descriptionEditText.setError("Please add a description");
                    descriptionEditText.requestFocus();
                } else{
                    uploadToDatabase();
                    clearFormFields();
                }

            }
        });
    }

    private void clearFormFields() {
        foundOrLostSpinner.setSelection(0);
        addPhotoImageView.setImageResource(R.drawable.no_animal_image);
        iHaveItCheckBox.setChecked(false);
        titleTextView.setText("");
        descriptionEditText.setText("");
        currentLocationRadioButton.setChecked(true);
        enterLocationEditText.setText("");
    }

    private void getFormValues() {
        descriptionStr = descriptionEditText.getText().toString().trim();
        foundOrLostStr = foundOrLostSpinner.getSelectedItem().toString();
        titleStr = titleTextView.getText().toString();
        iHaveItBool = iHaveItCheckBox.isChecked();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        timeAndDateStr = df.format(Calendar.getInstance().getTime());

        if (radioGroup.getCheckedRadioButtonId() == currentLocationRadioButton.getId()) {
            locationStr = getCurrentLocation();
        } else if (radioGroup.getCheckedRadioButtonId() == enterLocationRadioButton.getId()) {
            locationStr = enterLocationEditText.getText().toString().trim();
        }
    }


    private void uploadToDatabase() {
        AnimalCardDetails animalCardDetails = new AnimalCardDetails();
        animalCardDetails.setTitle(titleStr);
        animalCardDetails.setFoundOrLost(foundOrLostStr);
        animalCardDetails.setiHaveIt(iHaveItBool);
        animalCardDetails.setDescription(descriptionStr);
        animalCardDetails.setUserId(userIdStr);
        animalCardDetails.setAnimalPhotoUrl(animalPhotoUrlStr);
        animalCardDetails.setTimeAndDate(timeAndDateStr);
        animalCardDetails.setLocation(locationStr);

        if (Objects.equals(foundOrLostStr, FOUND)) {
            String foundAnimalItemId = animalDetailsDB.child("foundAnimals").push().getKey();
            animalCardDetails.setItemId(foundAnimalItemId);
            animalDetailsDB.child("foundAnimals").child(foundAnimalItemId).setValue(animalCardDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity(), "Animal's details updated", Toast.LENGTH_SHORT).show();
                }
            });
        } else {    //foundOrLostStr == Lost
            String lostAnimalItemId = animalDetailsDB.child("lostAnimals").push().getKey();
            animalCardDetails.setItemId(lostAnimalItemId);
            animalDetailsDB.child("lostAnimals").child(lostAnimalItemId).setValue(animalCardDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getActivity(), "Animal's details updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        progressBar.setVisibility(View.GONE);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                photoUri = result.getUri();
                try {
                    Picasso.get()
                            .load(photoUri)
                            .placeholder(R.drawable.no_user_image)
                            .into(addPhotoImageView);
                } catch (Exception e){
                    e.printStackTrace();
                }
                updateUserPhoto(photoUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void updateUserPhoto(Uri newPhotoUri){
        StorageReference userImageRef = mStorageRef.child("animalImages").child(userIdStr).child(newPhotoUri.getLastPathSegment() + "." + getFileExtension(newPhotoUri));
        userImageRef.putFile(newPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        animalPhotoUrlStr = uri.toString();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Uploaded failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });
    }

    private String getFileExtension(Uri uri) { //This method gets the extension of the image (like JPG, PNG, ...)
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}

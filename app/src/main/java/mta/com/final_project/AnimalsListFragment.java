package mta.com.final_project;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import mta.com.final_project.model.AnimalCardDetails;
import mta.com.final_project.model.CardViewHolder;


public class AnimalsListFragment extends Fragment {

    private final String FOUND = "found";
    private final String LOST = "lost";

    private String typeOfListDB;
    private View view;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<AnimalCardDetails> animalDetailsOptions;
    private DatabaseReference lostAnimalDetailsDB;
    private DatabaseReference userDB;
    private ProgressBar progressBar;
    private String currentUserId;

    public AnimalsListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    AnimalsListFragment(String foundOrLost) {
        if (Objects.equals(foundOrLost, LOST)) {
            typeOfListDB = "lostAnimals";
        } else { // (Objects.equals(foundOrLost, FOUND)) {
            typeOfListDB = "foundAnimals";
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_animals_list, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        progressBar = view.findViewById(R.id.progressBar_lostAnimalsListFragment);
        recyclerView = view.findViewById(R.id.animalRecyclerView_lostAnimalsListFragment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        lostAnimalDetailsDB = FirebaseDatabase.getInstance().getReference().child("animalDetails").child(typeOfListDB);
        userDB = FirebaseDatabase.getInstance().getReference().child("users");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        animalDetailsOptions = new FirebaseRecyclerOptions.Builder<AnimalCardDetails>()
                .setQuery(lostAnimalDetailsDB, AnimalCardDetails.class)
                .setLifecycleOwner(this)
                .build();
    }


    private void getUserDetailsFromFirebase(final String userId, final CardViewHolder holder){
        lostAnimalDetailsDB.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                userDB.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        holder.setCardUserName(dataSnapshot.child("username").getValue(String.class));
                        holder.setCardUserPhoto(dataSnapshot.child("photoUrl").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });




    }

    private void onClickDeleteItemHandler(final CardViewHolder holder, final AnimalCardDetails model) {
        lostAnimalDetailsDB.child(model.getItemId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                holder.getDeleteTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Delete Post");
                        builder.setMessage("Are you sure you want to delete this post?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dataSnapshot.getRef().removeValue();
                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void onClickCardItemHandler(CardViewHolder holder, final AnimalCardDetails model) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cardViewItemIntent = new Intent(getContext(), CardViewItemActivity.class);
                cardViewItemIntent.putExtra("userId", model.getUserId());
                cardViewItemIntent.putExtra("description", model.getDescription());
                startActivity(cardViewItemIntent);
            }
        });
    }

    private void initCardItemViews(CardViewHolder holder, AnimalCardDetails model) {
        holder.setCardDescription(model.getDescription());
        holder.setCardTitle(model.getTitle());
        holder.setCardAnimalPhoto(model.getAnimalPhotoUrl());
        holder.setCardTimeAndDate(model.getTimeAndDate());
        holder.setCardLocation(model.getLocation());

        if (Objects.equals(currentUserId, model.getUserId())) {
            holder.setDeleteButton();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initAnimalsLists();
    }


    private void initAnimalsLists() {
        FirebaseRecyclerAdapter<AnimalCardDetails, CardViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AnimalCardDetails, CardViewHolder>(animalDetailsOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CardViewHolder holder, int position, @NonNull final AnimalCardDetails model) {
                        initCardItemViews(holder, model);
                        onClickDeleteItemHandler(holder, model);
                        onClickCardItemHandler(holder, model);  //this method is being invoked after clicking one of the cards
                        getUserDetailsFromFirebase(model.getUserId(), holder);
                    }

                    @NonNull
                    @Override
                    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.animal_card_view, parent, false);

                        CardViewHolder animalViewHolder = new CardViewHolder(view);
                        return animalViewHolder;
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}

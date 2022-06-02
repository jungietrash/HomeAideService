package com.homeaide.post;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.homeaide.post.Adapter.PostAdapter;
import com.homeaide.post.Model.Post;
import com.homeaide.post.Model.Users;
import com.homeaide.post.Booking.booking.sign_in;

import java.util.ArrayList;
import java.util.List;

public class RatingActivity extends AppCompatActivity {
    private ImageView backButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private RecyclerView mRecyclerView;
    private TextView floatingActionButton;
    private PostAdapter adapter;
    private List<Post> list;
    private Query query;
    private ListenerRegistration listenerRegistration;
    private List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        backButton = findViewById(R.id.backButton);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        floatingActionButton = findViewById(R.id.floatingActionButton);
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(RatingActivity.this));
        list = new ArrayList<>();
        usersList = new ArrayList<>();
        adapter = new PostAdapter(RatingActivity.this , list, usersList);

        mRecyclerView.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RatingActivity.this, HomeActivityForAdmin.class);
                startActivity(intent);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(RatingActivity.this , AddPostActivity.class));
            }
        });
        if (firebaseAuth.getCurrentUser() != null){

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isBottom = !mRecyclerView.canScrollVertically(1);
                    if (isBottom)
                        Toast.makeText(RatingActivity.this, "Reached Bottom", Toast.LENGTH_SHORT).show();
                }
            });
            query = firestore.collection("Posts").orderBy("time" , Query.Direction.DESCENDING);
            listenerRegistration = query.addSnapshotListener(RatingActivity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                  for (DocumentChange doc : value.getDocumentChanges()){
                      if (doc.getType() == DocumentChange.Type.ADDED){
                          String postId = doc.getDocument().getId();
                          Post post = doc.getDocument().toObject(Post.class).withId(postId);
                          String postUserId = doc.getDocument().getString("user");
                          firestore.collection("Users").document(postUserId).get()
                                  .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                      @Override
                                      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            Users users = task.getResult().toObject(Users.class);
                                            usersList.add(users);
                                            list.add(post);
                                            adapter.notifyDataSetChanged();
                                        }else{
                                            Toast.makeText(RatingActivity.this, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                        }
                                      }
                                  });

                      }else{
                          adapter.notifyDataSetChanged();
                      }
                  }
                  listenerRegistration.remove();
                }
            });

        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null){
            startActivity(new Intent(RatingActivity.this , sign_in.class));
            finish();
        }else{
            String currentUserId = firebaseAuth.getCurrentUser().getUid();
            firestore.collection("Users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        if (!task.getResult().exists()){
                            startActivity(new Intent(RatingActivity.this , SetUpActivity.class));
                            finish();
                        }
                    }
                }
            });
        }

    }



}
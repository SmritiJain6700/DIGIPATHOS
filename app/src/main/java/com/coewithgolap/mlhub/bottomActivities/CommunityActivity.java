package com.coewithgolap.mlhub.bottomActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.coewithgolap.mlhub.Adapters.PostAdapter;
import com.coewithgolap.mlhub.HomeActivity;
import com.coewithgolap.mlhub.R;
import com.coewithgolap.mlhub.mainPageActivity.PostDetailsActivity;
import com.coewithgolap.mlhub.models.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommunityActivity extends AppCompatActivity {

    RelativeLayout contentView;

    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;

    Dialog popAddPost;
    CircleImageView popupUserImage;
    TextView popupUserName;
    ImageView popupPostImage, popupAddBtn;
    TextView popupTitle, popupDescription;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private RecyclerView postRecyclerView;
    private DatabaseReference postRef;
    private LinearLayout postProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // ini
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //init popup
        iniPopup();

        setupPopupImageClick();

        Button uploadPost = findViewById(R.id.btn_public_post);
        uploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });

        contentView = findViewById(R.id.content_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.community);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.crops:
                                CommunityActivity.this.startActivity(new Intent(CommunityActivity.this,
                                        HomeActivity.class));
                                CommunityActivity.this.finish();
                                CommunityActivity.this.overridePendingTransition(0, 0);
                                return true;
                            case R.id.community:
                                return true;
                            case R.id.detect:
                                CommunityActivity.this.startActivity(new Intent(CommunityActivity.this,
                                        DetectActivity.class));
                                CommunityActivity.this.finish();
                                CommunityActivity.this.overridePendingTransition(0, 0);
                                return true;
                        }
                        return false;
                    }
                });


        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postRecyclerView= findViewById(R.id.post_recyclerView);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        postProgressBar = findViewById(R.id.crops_progree_bar);

        postsList();
    }

    private void postsList() {
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postRef, Post.class)
                .build();
        FirebaseRecyclerAdapter<Post, PostAdapter> postAdapter = new FirebaseRecyclerAdapter<Post,
                PostAdapter>(options) {
            @NonNull
            @Override
            public PostAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent,
                        false);
                PostAdapter holder = new PostAdapter(view);
                return holder;
            }

            @Override
            protected void onBindViewHolder(@NonNull PostAdapter holder, final int position, @NonNull final Post model) {

                holder.tvTitle.setText(model.getTitle());
                holder.tvUsername.setText(model.getUserName());
                Glide.with(CommunityActivity.this).load(model.getPicture()).into(holder.imgPost);

                String userImg = model.getUserPhoto();
                if (userImg != null)
                {
                    Glide.with(CommunityActivity.this).load(userImg).into(holder.imgPostProfile);
                    Glide.with(CommunityActivity.this)
                            .load(userImg)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    holder.progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    holder.progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(holder.imgPostProfile);

                }
                else {
                    Glide.with(CommunityActivity.this).load(R.mipmap.ic_launcher).into(holder.imgPostProfile);

                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent postDetailActivity = new Intent(CommunityActivity.this, PostDetailsActivity.class);

                        postDetailActivity.putExtra("title",model.getTitle());
                        postDetailActivity.putExtra("userName", model.getUserName());
                        postDetailActivity.putExtra("postImage",model.getPicture());
                        postDetailActivity.putExtra("description",model.getDescription());
                        postDetailActivity.putExtra("postKey",model.getPostKey());
                        postDetailActivity.putExtra("userPhoto",model.getUserPhoto());

                        // will fix this later i forgot to add user name to post object
                        //postDetailActivity.putExtra("userName",mData.get(position).getUsername);
                        long timestamp  = (long) model.getTimeStamp();
                        postDetailActivity.putExtra("postDate",timestamp) ;
                        startActivity(postDetailActivity);
                    }
                });
            }
        };

        postRecyclerView.setAdapter(postAdapter);
        postAdapter.startListening();
    }

    private void setupPopupImageClick() {
        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here when image clicked we need to open the gallery
                // before we open the gallery we need to check if our app have the access to user files
                // we did this before in register activity I'm just going to copy the code to save time ...

                checkAndRequestForPermission();


            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(CommunityActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CommunityActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(CommunityActivity.this, "Please accept for required permission",
                        Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(CommunityActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            // everything goes well : we have permission to access user gallery
            openGallery();
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    // when user picked an image ...
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            popupPostImage.setImageURI(pickedImgUri);

        }


    }

    private void iniPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        //ini popup widgets
        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
        popupUserName = popAddPost.findViewById(R.id.pop_up_ser_name);
        popupPostImage = popAddPost.findViewById(R.id.popup_img);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);

        // load Current user profile photo
        if (currentUser.getPhotoUrl() != null)
        {
            popupUserName.setText(currentUser.getDisplayName());
            Glide.with(CommunityActivity.this).load(currentUser.getPhotoUrl()).into(popupUserImage);

        }
        else {
            popupUserName.setText(currentUser.getDisplayName());
            Glide.with(CommunityActivity.this).load(R.mipmap.ic_launcher).into(popupUserImage);

        }

        //Add post click listener
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                if (!popupTitle.getText().toString().isEmpty()
                        && !popupDescription.getText().toString().isEmpty()
                        && pickedImgUri != null) {

                    //everything is okey no empty or null value
                    // TODO Create Post Object and add it to firebase database
                    // first we need to upload post Image
                    // access firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownloadLink = uri.toString();

                                    if (currentUser.getPhotoUrl() != null) {
                                        Post post = new Post(popupTitle.getText().toString(),
                                                popupDescription.getText().toString(),
                                                imageDownloadLink,
                                                currentUser.getUid(),
                                                currentUser.getPhotoUrl().toString(),
                                                currentUser.getDisplayName());

                                        //add post to firebase
                                        addPost(post);
                                    } else {
                                        Post post = new Post(popupTitle.getText().toString(),
                                                popupDescription.getText().toString(),
                                                imageDownloadLink,
                                                currentUser.getUid(),
                                                null,
                                                currentUser.getDisplayName());

                                        //add post to firebase
                                        addPost(post);
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // something goes wrong uploading picture

                                    showMessage(e.getMessage());
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });

                } else {
                    showMessage("Please verify all input fields and choose Post Image");
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);

                }

            }
        });
    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // get post unique ID and upadte post key
        String key = myRef.getKey();
        post.setPostKey(key);


        // add post data to firebase database

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });
    }

    private void showMessage(String s) {

        Toast.makeText(CommunityActivity.this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
package rma.postit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import rma.postit.helper.FirebaseConnector;
import rma.postit.helper.FirestorageHelper;
import rma.postit.helper.ImageLoader;
import rma.postit.model.Post;

public class AddPostActivity extends AppCompatActivity {
    String mCategoryId;
    String mPostId;
    EditText mTitle;
    EditText mDescription;
    EditText mUri;
    FrameLayout mLayoutOpener;
    Button mSaveButton;
    ImageView mImage;
    Post mPost;
    boolean hasChangedImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Bundle extras = getIntent().getExtras();

        extractBundle(extras);
        getComponentsFromView();
        setOnClickListener();

        if( mPostId != null && !mPostId.isEmpty() )
            fillViewWithPost();
    }

    private void fillViewWithPost() {
        FirebaseConnector
            .getInstance()
            .getUserPostByCategoryAndId(mCategoryId, mPostId)
            .addSnapshotListener((document, e) -> {
                mPost = document.toObject(Post.class);
                mTitle.setText(mPost.getTitle());
                mDescription.setText(mPost.getDescription());
                mUri.setText(mPost.getUri());

                if( !mPost.getImageUrl().isEmpty() )
                    new ImageLoader(mImage, mPost.getImageUrl()).execute();
        });
    }

    private void extractBundle(Bundle extras) {
        mCategoryId = extras.getString("id");
        mPostId = extras.getString("pid");
    }

    private void setOnClickListener() {
        mSaveButton.setOnClickListener((event)->{
            if( mPost == null )
                mPost = new Post();

            // fill post
            mPost.setTitle(mTitle.getText().toString());
            mPost.setDescription(mDescription.getText().toString());
            mPost.setUri(mUri.getText().toString());

            Toast.makeText(this, "Saving...", Toast.LENGTH_LONG).show();

            FirebaseConnector
                .getInstance()
                .createUserPostByCategory(mCategoryId, mPost.getId())
                .set(mPost, SetOptions.merge())
                .addOnSuccessListener((_post) -> {
                    if( !hasChangedImage ){
                        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                });

            if( hasChangedImage ){
                String fileName = "post_picture_" + mPost.getId();
                byte[] imageBytes = getImageBytesFromView(mImage);
                FirestorageHelper
                    .getInstance()
                    .uploadImageToStorage(fileName, imageBytes)
                    .addOnSuccessListener((reference) -> {
                        // get download url
                        reference.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                            mPost.setImageUrl(uri.toString());
                            FirebaseConnector
                                    .getInstance()
                                    .createUserPostByCategory(mCategoryId, mPost.getId())
                                    .set(mPost, SetOptions.merge());
                            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                        });
                    });
            }

            finish();
        });

        mLayoutOpener.setOnClickListener((event) -> {
            startActivityForResult(getImageIntents(), 1);
        });
    }
    private Intent getImageIntents() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {getIntent});

        return chooserIntent;
    }

    private void getComponentsFromView() {
        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);
        mLayoutOpener = findViewById(R.id.add_image);
        mSaveButton = findViewById(R.id.save_button);
        mImage = findViewById(R.id.featured_image);
        mUri = findViewById(R.id.uri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Always deny call if we do not have a result ok.
        if( resultCode != RESULT_OK ){
            return;
        }
        
        // check if the user has selected an image
        if( requestCode == 1 ){
            // picked image
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(data.getData());
                Drawable profileImage = Drawable.createFromStream(inputStream, data.getData().toString());
                mImage.setImageDrawable(profileImage);
                hasChangedImage = true;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Will get the bytes of the image from the imageview.
     *
     * @param imageView the image view to retrieve the image from
     */
    public static byte[] getImageBytesFromView(ImageView imageView) {
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // compress bitmap
        // @todo: scale
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        return baos.toByteArray();
    }
}

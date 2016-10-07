package ru.ksu.motygullin.gallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PhotoActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView = (ImageView) findViewById(R.id.max_photo);

        Intent intent = getIntent();
        String max_photoURL = intent.getStringExtra("max_photo");
        Glide.with(this).load(max_photoURL).fitCenter().into(imageView);
    }

}

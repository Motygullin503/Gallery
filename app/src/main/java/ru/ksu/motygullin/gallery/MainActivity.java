package ru.ksu.motygullin.gallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import ru.ksu.motygullin.gallery.network.RxVk;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    TextView mUserName;
    RelativeLayout mMainContent;
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserName = (TextView) findViewById(R.id.user_name);
        mMainContent = (RelativeLayout) findViewById(R.id.main_content);
        mProgress = (ProgressBar) findViewById(R.id.loading_view);

        Button btn_toGallery = (Button) findViewById(R.id.to_gallery);
        btn_toGallery.setOnClickListener(this);

        setUserNameAndShowButtons();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_gallery:
                toGallery();
                break;
        }
    }

    private void setUserNameAndShowButtons() {
        showLoading();
        RxVk vk = new RxVk();
        vk.getUsers(new RxVk.RxVkListener<VKList<VKApiUser>>() {
            @Override
            public void requestFinished(VKList<VKApiUser> requestResult) {
                showMainLayout();
                mUserName.setText(getString(R.string.main_activity_user_name,
                        requestResult.get(0).first_name, requestResult.get(0).last_name));
            }
        }, 0);
    }


    private void showMainLayout() {
        mMainContent.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    private void showLoading() {
        mProgress.setVisibility(View.VISIBLE);
        mMainContent.setVisibility(View.GONE);
    }

    private void toGallery() {
        Intent galleryIntent = new Intent(this, GalleryActivity.class);
        startActivity(galleryIntent);
    }
}

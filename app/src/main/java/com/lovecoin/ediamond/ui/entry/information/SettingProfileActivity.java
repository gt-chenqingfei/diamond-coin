package com.lovecoin.ediamond.ui.entry.information;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.UpdateProfileParams;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.data.UserHelper;
import com.lovecoin.ediamond.event.StartMainEvent;
import com.lovecoin.ediamond.event.UpdateProfileEvent;
import com.lovecoin.ediamond.image.okhttp3.GlideApp;
import com.lovecoin.ediamond.image.okhttp3.GlideRequests;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.widget.CustomToolbar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import activitystarter.Arg;
import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author qingfei.chen
 * @since 2018/5/22.
 * Copyright © 2017 ZheLi Technology Co.,Ltd. All rights reserved.
 */
@MakeActivityStarter
public class SettingProfileActivity extends BaseActivity {
    public static final int RESULT_LOAD_IMAGE = 100;
    @Arg(optional = true)
    String avatar;
    @Arg(optional = true)
    String nickname;
    @Arg(optional = true)
    int gender;
    @Arg
    int title;

    @BindView(R.id.toolbar)
    CustomToolbar customToolbar;
    @BindView(R.id.user_head_image)
    CircleImageView mIvAvatar;
    @BindView(R.id.name_et)
    EditText mEtName;
    @BindView(R.id.rg_gender)
    RadioGroup mRGGender;
    @BindView(R.id.tv_ok)
    TextView mTvOk;

    private String mAvatarUrl;
    private GlideRequests glideRequests;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_profile;
    }

    @Override
    protected void onBeforeSetContentLayout() {

    }

    @Override
    protected void initView() {
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvOk.setEnabled(!TextUtils.isEmpty(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        customToolbar.setTitle(getString(title));
        glideRequests = GlideApp.with(this);
        if (!TextUtils.isEmpty(avatar)) {
            loadUserHead(avatar);
        }

        if (!TextUtils.isEmpty(nickname)) {
            mEtName.setText(nickname);
        }

        if (gender == 1) {
            mRGGender.check(R.id.rb_gender_male);
        } else if (gender == 2) {
            mRGGender.check(R.id.rb_gender_female);
        }
    }


    @OnClick(R.id.tv_ok)
    public void onOKClick(View view) {
        String name = mEtName.getText().toString();
        int gender = getGender();

        updateProfile(name, gender);
    }

    @OnClick(R.id.user_head_image)
    public void onAvatarClick(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    public int getGender() {
        int gender = 0;
        switch (mRGGender.getCheckedRadioButtonId()) {
            case R.id.rb_gender_female:
                gender = 2;
                break;

            case R.id.rb_gender_male:
                gender = 1;
                break;
        }
        return gender;
    }

    private void updateProfile(String nickName, int gender) {
        UpdateProfileParams profileParams = new UpdateProfileParams()
                .setAvatar(mAvatarUrl)
                .setGender(gender)
                .setNickname(nickName);

        Api.getInstance()
                .updateProfile(profileParams)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        showLoading();
                    }

                    @Override
                    protected void onHandleSuccess(Object url) {
                        dismissLoading();
                        EventBus.getDefault().post(new StartMainEvent());
                        EventBus.getDefault().post(new UpdateProfileEvent());
                        SettingProfileActivity.this.finish();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoading();
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                    }
                });
    }

    public static MultipartBody.Part prepareFilePart(String partName, String path) {
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpg"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void uploadImage(String path) {
        MultipartBody.Part file = prepareFilePart("imgFile", path);
        Api.getInstance()
                .uploadFile(UserHelper.getInstance().getToken(), file)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        showLoading();
                    }

                    @Override
                    protected void onHandleSuccess(String url) {
                        dismissLoading();
                        mAvatarUrl = url;
                        loadUserHead(url);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoading();
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                    }
                });
    }

    private void compressImage(String imagePath) {
        //Logger.d("origin file size : %s", FileUtils.getFileSize(imagePath));

        Luban.with(this)
                .load(imagePath)
                .ignoreBy(500)
                //             .setTargetDir(new File(getFilesDir(), "images").getAbsolutePath())  // 设置压缩后文件存储位置
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        showLoading();
                    }

                    @Override
                    public void onSuccess(File file) {
                        //Logger.d("origin file size : %s", FileUtils.getFileSize(file));
                        dismissLoading();
                        loadUserHead(imagePath);
                        uploadImage(file.getPath());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("Compress Image Error");
                        dismissLoading();
                    }
                }).launch();    //启动压缩
    }

    private void loadUserHead(String url) {
        glideRequests.load(url)
                .placeholder(R.drawable.ic_profile_avatar_edit)
                .centerCrop()
                .into(mIvAvatar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            c.close();

            compressImage(imagePath);
        }
    }

}

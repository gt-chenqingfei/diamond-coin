package com.lovecoin.ediamond.ui.profile;

import android.annotation.SuppressLint;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.resp.LoveRelationProfile;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.event.DelRelationEvent;
import com.lovecoin.ediamond.image.okhttp3.GlideApp;
import com.lovecoin.ediamond.image.okhttp3.GlideRequests;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.dialog.CancelRelationDialog;
import com.lovecoin.ediamond.widget.CircleProgressBar;
import com.lovecoin.ediamond.widget.CustomToolbar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@MakeActivityStarter
public class UserProfileActivity extends BaseActivity implements CancelRelationDialog.OnSureListener {

    @BindView(R.id.user_head_image)
    CircleImageView userHeadImage;
    @BindView(R.id.toolbar)
    CustomToolbar mCustomToolbar;
    @BindView(R.id.total_coin_tv)
    TextView totalCoinTv;
    @BindView(R.id.total_coin_value_tv)
    TextView totalCoinValueTv;
    @BindView(R.id.i_recieved_num_tv)
    TextView iRecievedNumTv;
    @BindView(R.id.i_send_num_tv)
    TextView iSendNumTv;
    @BindView(R.id.relationTime_tv)
    TextView relationTimeTv;
    @BindView(R.id.begin_end_time_tv)
    TextView beginEndTimeTv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    GlideRequests glideRequests;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_profile;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        refreshLayout.setOnRefreshListener(refreshlayout -> getLoveRelationProfile());
    }

    @Override
    protected void initData() {
        glideRequests = GlideApp.with(this);
        refreshLayout.autoRefresh();
    }

    @OnClick(R.id.del_relation_btn)
    public void onDelRelationClick(View view) {
        CancelRelationDialog dialog = CancelRelationDialog.newInstance();
        dialog.show(getSupportFragmentManager(), "cancel_dialog");
    }

    private void getLoveRelationProfile() {
        Api.getInstance().getLoveRelationProfile()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<LoveRelationProfile>() {
                    @Override
                    protected void onHandleSuccess(LoveRelationProfile profile) {
                        getLoveRelationProfileSuccess(profile);
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        if ("00003".equals(code)) {
                            ToastUtils.showShort(msg);
                            UserProfileActivity.this.finish();
                        } else {
                            super.onHandleError(code, msg);
                        }
                    }

                    @Override
                    protected void onAfter() {
                        super.onAfter();
                        refreshLayout.finishRefresh();
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void getLoveRelationProfileSuccess(LoveRelationProfile profile) {
        // 上
        String loverTitle = profile.getLoverTitle();
        mCustomToolbar.setTitle(loverTitle);
        totalCoinTv.setText(profile.getTotalCoin());
        totalCoinValueTv.setText(new BigDecimal(profile.getCoinValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

        // 中
        iSendNumTv.setText(profile.getSendNum());
        iRecievedNumTv.setText(profile.getReceiveNum());

        // 下
        relationTimeTv.setText(profile.getRelationTime());
        beginEndTimeTv.setText(String.format("%s~%s", profile.getStartRelationTime(), profile.getEndRelationTime()));

        loadLoverHead(profile.getLoverHeadImgUrl(), false);
    }

    private void delLoveRelation() {

        Api.getInstance().cancelLoveRelation()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onHandleSuccess(Object object) {
                        delLoveRelationSuccess(object);
                    }
                });

    }

    private void delLoveRelationSuccess(Object object) {
        EventBus.getDefault().postSticky(new DelRelationEvent());
        this.finish();
    }

    @Override
    public void onSure() {
        delLoveRelation();
    }

    private void loadLoverHead(String url, boolean isMan) {
        glideRequests.load(url)
                .placeholder(isMan ? R.drawable.my_head_holder : R.drawable.love_header_holder)
                .centerCrop()
                .into(userHeadImage);
    }
}

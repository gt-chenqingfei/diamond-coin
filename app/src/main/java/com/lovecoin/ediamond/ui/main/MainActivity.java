package com.lovecoin.ediamond.ui.main;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.resp.LoveRelation;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.app.App;
import com.lovecoin.ediamond.event.ChangeHeadImageEvent;
import com.lovecoin.ediamond.event.DelRelationEvent;
import com.lovecoin.ediamond.event.SendCoinEvent;
import com.lovecoin.ediamond.image.okhttp3.GlideApp;
import com.lovecoin.ediamond.image.okhttp3.GlideRequests;
import com.lovecoin.ediamond.push.PushIdService;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.guide.GuideActivity;
import com.lovecoin.ediamond.ui.guide.GuideActivityStarter;
import com.lovecoin.ediamond.ui.profile.MyProfileActivityStarter;
import com.lovecoin.ediamond.ui.profile.UserProfileActivityStarter;
import com.lovecoin.ediamond.ui.recharge.RechargeActivityStarter;
import com.lovecoin.ediamond.ui.send.SendActivityStarter;
import com.lovecoin.ediamond.ui.setting.SettingActivityStarter;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@MakeActivityStarter
public class MainActivity extends BaseActivity {

    @BindView(R.id.main_her_bg_image)
    ImageView mainHerBgImage;
    @BindView(R.id.main_her_head_image)
    CircleImageView mainHerHeadImage;
    @BindView(R.id.main_my_bg_image)
    ImageView mainMyBgImage;
    @BindView(R.id.main_my_head_image)
    CircleImageView mainMyHeadImage;
    @BindView(R.id.main_left_aciton_tv)
    ImageView mainLeftAcitonTv;
    @BindView(R.id.main_right_aciton_tv)
    ImageView mainRightAcitonTv;
    @BindView(R.id.main_center_aciton_tv)
    ImageView mainCenterAcitonTv;
    @BindView(R.id.center_relation_image)
    ImageView centerRelationImage;
    @BindView(R.id.my_coin_layout)
    View myCoinLayout;
    @BindView(R.id.my_coin_num_tv)
    TextView myCoinNumTv;
    @BindView(R.id.lover_coin_layout)
    View loverCoinLayout;
    @BindView(R.id.lover_coin_num_tv)
    TextView loverCoinNumTv;
    @BindView(R.id.send_num_tv)
    TextView sendNumTv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private GlideRequests glideRequests;

    private LoveRelation loveRelation;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getNavColorRes() {
        return R.color.white;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        mainHerBgImage.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        refreshLayout.setOnRefreshListener(refreshlayout -> getLoveRelation());

    }

    @Override
    protected void initData() {
        glideRequests = GlideApp.with(this);

        App.getInstance().setMainStart(true);
        refreshLayout.autoRefresh();
        //        if (PushHandler.handlePush(this, getIntent().getExtras())) {
        //            //Logger.e("main handle push");
        //        }
        String token = FirebaseInstanceId.getInstance().getToken();
        if (!StringUtils.isEmpty(token)) {
            PushIdService.sendRegistrationToServer(token);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getLoveRelation();
    }

    @OnClick({R.id.main_her_head_image,
            R.id.main_my_head_image,
            R.id.main_center_aciton_tv,
            R.id.main_left_aciton_tv,
            R.id.main_right_aciton_tv
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_her_head_image:
                if (loveRelation == null || loveRelation.isSingle()) {
                    return;
                }
                UserProfileActivityStarter.start(this);
                break;
            case R.id.main_my_head_image:
                MyProfileActivityStarter.start(this);
                break;
            case R.id.main_right_aciton_tv:
                RechargeActivityStarter.start(this);
                break;
            case R.id.main_center_aciton_tv:
                SendActivityStarter.start(this);
                break;
            case R.id.main_left_aciton_tv:
                SettingActivityStarter.start(this);
                break;
        }
    }

    private void getLoveRelation() {

        Api.getInstance().getLoveRelation()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<LoveRelation>() {
                    @Override
                    protected void onHandleSuccess(LoveRelation loveRelations) {
                        getLoveRelationSuccess(loveRelations);
                    }

                    @Override
                    protected void onAfter() {
                        super.onAfter();
                        refreshLayout.finishRefresh();
                    }
                });

    }

    private void getLoveRelationSuccess(LoveRelation relations) {
        loveRelation = relations;
        loadMyHead(relations.getSponsorHeadImg(), true);
        // test data
//                relations.setReceiverUid("111");
//                relations.setSendNum(111);
//                relations.setReceiveNum(222);
        if (relations.isSingle()) {
            loadLoverHeadSingle();
            hideRelation();
        } else {
            showRelation();
        }

        showGuideIfNeed(relations.isSingle());
    }

    private void loadMyHead(String url, boolean isMan) {
        //        if (isFinishing() || isDestroyed()) {
        //            //Logger.e("activity is finishing, can't load image");
        //            return;
        //        }
        glideRequests
                .load(url)
                .placeholder(isMan ? R.drawable.my_head_holder : R.drawable.love_header_holder)
                .centerCrop()
                .into(mainMyHeadImage);
    }

    private void loadLoverHead(String url, boolean isMan) {
        glideRequests
                .load(url)
                .placeholder(isMan ? R.drawable.my_head_holder : R.drawable.love_header_holder)
                .centerCrop()
                .into(mainHerHeadImage);
    }

    private void loadLoverHeadSingle() {
        glideRequests
                .load(R.drawable.lover_header_single)
                .centerCrop()
                .into(mainHerHeadImage);
    }

    private void showGuideIfNeed(boolean isSingle) {
        String type = isSingle ? GuideActivity.TYPE_NOT_HAS_RELATION : GuideActivity.TYPE_HAS_RELATION;
        boolean shown = SPUtils.getInstance(GuideActivity.PREF_KEY_GUIDE).getBoolean(type, false);
        if (!shown) {
            GuideActivityStarter.start(this, type);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDelEvent(DelRelationEvent e) {
        //Logger.d("del event");
        DelRelationEvent event = EventBus.getDefault().removeStickyEvent(DelRelationEvent.class);
        if (event == null) {
            return;
        }
        hideRelation();
    }

    @Subscribe(sticky = true)
    public void onMessageEvent(ChangeHeadImageEvent e) {
        ChangeHeadImageEvent event = EventBus.getDefault().removeStickyEvent(ChangeHeadImageEvent.class);
        if (event == null) {
            return;
        }
        //Logger.d("change image url");
        loadMyHead(e.getUrl(), true);
    }

    @Subscribe(sticky = true)
    public void onMessageEvent(SendCoinEvent e) {
        SendCoinEvent event = EventBus.getDefault().removeStickyEvent(SendCoinEvent.class);
        if (event == null) {
            return;
        }
        //Logger.d("send coin event");
        try {
            loverCoinNumTv.setText(String.valueOf(Integer.valueOf(loverCoinNumTv.getText().toString()) + e.num));
        } catch (Exception ignore) {

        }

        showSendAnimation(e.num);
    }

    private void showRelation() {
        centerRelationImage.setVisibility(View.VISIBLE);
        loadLoverHead(loveRelation.getReceiverHeadImg(), false);

        myCoinLayout.setVisibility(View.VISIBLE);
        loverCoinLayout.setVisibility(View.VISIBLE);

        myCoinNumTv.setText(String.valueOf(loveRelation.getReceiveNum()));
        loverCoinNumTv.setText(String.valueOf(loveRelation.getSendNum()));
    }

    private void hideRelation() {

        centerRelationImage.setVisibility(View.INVISIBLE);
        myCoinLayout.setVisibility(View.GONE);
        loverCoinLayout.setVisibility(View.GONE);
        loadLoverHeadSingle();
        if (loveRelation != null) {
            loveRelation.setReceiverUid("");
        }
    }

    private void showSendAnimation(int num) {
        sendNumTv.setText("+" + num);
        sendNumTv.setVisibility(View.VISIBLE);

        sendNumTv.post(new Runnable() {
            @Override
            public void run() {

                int transY = sendNumTv.getTop() - mainHerHeadImage.getTop();
                TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -transY);
                animation.setDuration(5000);
                animation.setFillAfter(false);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        sendNumTv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                sendNumTv.startAnimation(animation);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getInstance().setMainStart(false);
    }
}

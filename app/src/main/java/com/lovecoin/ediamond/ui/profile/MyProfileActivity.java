package com.lovecoin.ediamond.ui.profile;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.MyRecordCountParams;
import com.lovecoin.ediamond.api.entity.resp.MyProfile;
import com.lovecoin.ediamond.api.entity.resp.MyRecordCountResp;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.event.BalanceChangeEvent;
import com.lovecoin.ediamond.event.UpdateProfileEvent;
import com.lovecoin.ediamond.image.okhttp3.GlideApp;
import com.lovecoin.ediamond.image.okhttp3.GlideRequests;
import com.lovecoin.ediamond.ui.account.MyAccountActivityStarter;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.entry.information.SettingProfileActivityStarter;
import com.lovecoin.ediamond.ui.recharge.RechargeActivityStarter;
import com.lovecoin.ediamond.ui.record.RecordActivityStarter;
import com.lovecoin.ediamond.ui.send.SendToWalletActivityStarter;
import com.lovecoin.ediamond.utils.DateTimeUtils;
import com.lovecoin.ediamond.widget.CustomToolbar;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Calendar;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@MakeActivityStarter
public class MyProfileActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    CustomToolbar mToolbar;
    @BindView(R.id.user_head_image)
    CircleImageView userHeadImage;
    @BindView(R.id.user_phone_tv)
    TextView userPhoneTv;
    @BindView(R.id.send_to_wallet_btn)
    Button mBtnSendToWallet;
    @BindView(R.id.recharge_btn)
    ViewGroup rechargeBtn;
    @BindView(R.id.year_month_tv)
    TextView yearMonthTv;
    @BindView(R.id.send_num_tv)
    TextView sendNumTv;
    @BindView(R.id.recieve_num_tv)
    TextView recieveNumTv;
    @BindView(R.id.buy_num_tv)
    TextView buyNumTv;
    @BindView(R.id.total_coin_tv)
    TextView totalCoinTv;
    @BindView(R.id.total_coin_value_tv)
    TextView totalCoinValueTv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.my_account_amount_tv)
    TextView myAccountAmountTv;
    private GlideRequests glideRequests;
    MyProfile userProfile;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_profile;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
//        mBtnSendToWallet.setVisibility(View.GONE);
        userHeadImage.setImageResource(R.drawable.my_head_holder);
        refreshLayout.setOnRefreshListener(refreshlayout -> getUserProfile());

        mToolbar.setMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userProfile == null) {
                    return;
                }
                SettingProfileActivityStarter.start(MyProfileActivity.this, userProfile.getHeadImgUrl(), userProfile.getNickName(), userProfile.getSex(), R.string.activity_set_profile_title_edit);
            }
        });
    }

    @Override
    protected void initData() {
        glideRequests = GlideApp.with(this);
        refreshLayout.autoRefresh();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.my_record_layout)
    public void onMyRecordClick() {
        RecordActivityStarter.start(this);
    }


    @OnClick(R.id.recharge_btn)
    public void onRechargeClick() {
        RechargeActivityStarter.start(this);
    }

    @OnClick(R.id.send_to_wallet_btn)
    public void sendToWalletClick() {
        SendToWalletActivityStarter.start(this);
    }

    @OnClick(R.id.year_month_tv)
    public void onYearMonthClick() {
        DateTimeUtils.showPicker(this, new DateTimeUtils.Callback() {
            @Override
            public void onSelected(String year, String month, String monthAndYear) {
                yearMonthTv.setText(monthAndYear);
                getMyRecordCount(year, month);
            }
        });
    }

    @OnClick(R.id.my_account_layout)
    public void onMyAccountClick() {
        MyAccountActivityStarter.start(this);
    }

    private void getUserProfile() {
        Api.getInstance().getUserProfile().compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<MyProfile>() {
                    @Override
                    protected void onHandleSuccess(MyProfile userProfile) {
                        resetData(userProfile);
                    }

                    @Override
                    protected void onAfter() {
                        super.onAfter();
                        refreshLayout.finishRefresh();
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void resetData(MyProfile userProfile) {
        this.userProfile = userProfile;
        totalCoinTv.setText(userProfile.getTotalCoin());
        totalCoinValueTv
                .setText(new BigDecimal(userProfile.getCoinValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        Calendar calendar = Calendar.getInstance();
        yearMonthTv.setText(DateTimeUtils
                .getMothAndYear(String.valueOf(calendar.get(Calendar.YEAR)), calendar.get(Calendar.MONTH)));


        if (TextUtils.isEmpty(userProfile.getNickName())) {
            userPhoneTv.setText(R.string.str_my_love);
        } else {
            userPhoneTv.setText(userProfile.getNickName()); // TODO fb用户手机号设置为什么？？？
        }
        buyNumTv.setText(String.valueOf(userProfile.getBuyNum()));
        recieveNumTv.setText(String.valueOf(userProfile.getReceiveNum()));
        sendNumTv.setText(String.valueOf(userProfile.getSendNum()));

        myAccountAmountTv.setText("$" + new BigDecimal(userProfile.getBalance()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());

        loadUserHead(userProfile.getHeadImgUrl());
    }

    private void getMyRecordCount(String year, String month) {
        MyRecordCountParams myRecordCountParams = new MyRecordCountParams(year, month);
        Api.getInstance()
                .myRecordCount(myRecordCountParams)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<MyRecordCountResp>() {
                    @Override
                    protected void onHandleSuccess(MyRecordCountResp resp) {
                        buyNumTv.setText(resp.getBuyNum());
                        recieveNumTv.setText(resp.getReceiveNum());
                        sendNumTv.setText(resp.getSendNum());
                    }
                });
    }

    private void loadUserHead(String url) {
        glideRequests.load(url)
                .placeholder(R.drawable.my_head_holder)
                .centerCrop()
                .into(userHeadImage);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateProfileEvent(UpdateProfileEvent event) {
        refreshLayout.autoRefresh();
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onBalanceChangeEvent(BalanceChangeEvent e) {
        BalanceChangeEvent event = EventBus.getDefault().removeStickyEvent(BalanceChangeEvent.class);
        if (event == null) {
            return;
        }
        refreshLayout.autoRefresh();
    }
}

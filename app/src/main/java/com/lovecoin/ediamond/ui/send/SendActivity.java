package com.lovecoin.ediamond.ui.send;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.SendParams;
import com.lovecoin.ediamond.api.entity.resp.SendCoinListResp;
import com.lovecoin.ediamond.api.entity.resp.SendResp;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.event.SendCoinEvent;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.dialog.LessEDiamondDialog;
import com.lovecoin.ediamond.ui.recharge.RechargeActivityStarter;
import com.lovecoin.ediamond.utils.ClipUtil;
import com.lovecoin.extra.facebook.FbMessagerShare;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * 给真爱加码
 */
@MakeActivityStarter
public class SendActivity extends BaseActivity {

    @BindView(R.id.send_et)
    EditText sendEt;
    @BindView(R.id.coin_balance_tv)
    TextView coinBalanceTv;
    @BindViews({R.id.send_coin_0_tv, R.id.send_coin_1_tv, R.id.send_coin_2_tv})
    List<TextView> sendCoinTvs;
    @BindView(R.id.action_btn)
    Button actionBtn;
    @BindView(R.id.copy_link_fl)
    ViewGroup vgCopyLinkLayer;

    @BindView(R.id.copy_link_tv)
    TextView tvCopy;

    @BindView(R.id.share_facebook_tv)
    TextView tvFacebook;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    public SendCoinListResp resp;

    CallbackManager callbackManager;
    MessageDialog messageDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_send;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        sendEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                actionBtn.setEnabled(s.toString().length() != 0);
            }
        });
        refreshLayout.setOnRefreshListener(refreshlayout -> getSendCoinList());
    }

    @Override
    protected void initData() {
        refreshLayout.autoRefresh();
        callbackManager = CallbackManager.Factory.create();
        messageDialog = new MessageDialog(this);
        messageDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });
    }

    @OnClick(R.id.action_btn)
    public void onActionClick() {
        if (resp == null) {
            return;
        }

        String s = sendEt.getText().toString();
        int num = 0;
        try {
            num = Integer.valueOf(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (num == 0) {
            return;
        }

        if (num > resp.coinNum) {
            showLessDiamondDialog();
            return;
        }
        send(num);
    }

    @OnClick(R.id.action_layout)
    public void onRechargeClick() {
        RechargeActivityStarter.start(this);
    }

    private void showLessDiamondDialog() {
        LessEDiamondDialog dialog = LessEDiamondDialog.newInstance();
        dialog.show(getSupportFragmentManager(), "less_diamond");
    }

    private void send(int num) {
        showLoading();
        Api.getInstance()
                .directSendCoin(new SendParams(String.valueOf(num)))
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<SendResp>() {
                    @Override
                    protected void onHandleSuccess(SendResp resp) {
                        dismissLoading();
                        if (resp.isHasRelation()) {
                            setBalance(resp.getCoinTotal());
                            EventBus.getDefault().postSticky(new SendCoinEvent(num));
                            SendActivity.this.finish();
                        } else {
                            Logger.d(resp.getUrl());
                            performShare(resp.getUrl());
                        }
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoading();
                    }
                });
    }

    private void performShare(String url) {
        boolean isInstallMessager = MessageDialog.canShow(ShareLinkContent.class);
        vgCopyLinkLayer.setVisibility(View.VISIBLE);
        tvCopy.setTag(url);
        if (!isInstallMessager) {
            tvFacebook.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.copy_link_tv)
    public void onCopyClick(View view) {
        vgCopyLinkLayer.setVisibility(View.GONE);
        ClipUtil.copyText(tvCopy.getTag().toString(), this);
    }

    @OnClick(R.id.share_facebook_tv)
    public void onFacebookClick(View view) {
        vgCopyLinkLayer.setVisibility(View.GONE);
        boolean canShow = FbMessagerShare.share(messageDialog, tvCopy.getTag().toString());
        if (!canShow) {
            ToastUtils.showShort(R.string.tips_messager_not_install);
        }
    }

    @OnClick(R.id.copy_link_fl)
    public void onCopyLinkLayerClick(View view) {
        vgCopyLinkLayer.setVisibility(View.GONE);
    }

    public void getSendCoinList() {
        Api.getInstance()
                .sendCoinList()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<SendCoinListResp>() {

                    @Override
                    protected void onHandleSuccess(SendCoinListResp resp) {
                        SendActivity.this.resp = resp;
                        if (resp.sendCoinLists != null && !resp.sendCoinLists.isEmpty()) {
                            for (int i = 0; i < resp.sendCoinLists.size(); i++) {
                                if (i > 2) {
                                    return;
                                }
                                TextView view = sendCoinTvs.get(i);
                                view.setVisibility(View.VISIBLE);
                                view.setText(String.valueOf(resp.sendCoinLists.get(i).num));
                                view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        sendEt.setText(view.getText().toString());
                                    }
                                });
                            }
                        }

                        setBalance(resp.coinNum);
                    }

                    @Override
                    protected void onAfter() {
                        super.onAfter();
                        refreshLayout.finishRefresh();
                    }
                });
    }

    private void setBalance(int num) {
        coinBalanceTv.setText(getString(R.string.activity_recharge_balance, num));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

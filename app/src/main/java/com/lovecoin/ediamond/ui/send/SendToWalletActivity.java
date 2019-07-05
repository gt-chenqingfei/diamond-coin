package com.lovecoin.ediamond.ui.send;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.widget.MessageDialog;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.SendParams;
import com.lovecoin.ediamond.api.entity.params.WithdrawCurrencyIdentifyingCodeParams;
import com.lovecoin.ediamond.api.entity.params.WithdrawCurrencyParams;
import com.lovecoin.ediamond.api.entity.params.WithdrawCurrencySetPwdParams;
import com.lovecoin.ediamond.api.entity.params.WithdrawCurrencyVerifyPassParams;
import com.lovecoin.ediamond.api.entity.resp.MyProfile;
import com.lovecoin.ediamond.api.entity.resp.SendCoinListResp;
import com.lovecoin.ediamond.api.entity.resp.SendResp;
import com.lovecoin.ediamond.api.entity.resp.WithdrawCurrencyIdentifyingCodeRespond;
import com.lovecoin.ediamond.api.entity.resp.WithdrawCurrencyPwdStatusRespond;
import com.lovecoin.ediamond.api.entity.resp.WithdrawCurrencyTradePoundageRespond;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.event.SendCoinEvent;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.dialog.CustomEDiamondDialog;
import com.lovecoin.ediamond.ui.dialog.LessEDiamondDialog;
import com.lovecoin.ediamond.ui.record.RecordListFromWalletActivityStarter;
import com.lovecoin.ediamond.utils.CommonCountDownTimer;
import com.lovecoin.ediamond.widget.CustomToolbar;
import com.lovecoin.ediamond.widget.IdentifyingCodeView;
import com.lovecoin.extra.facebook.FbMessagerShare;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提币页面
 */
@MakeActivityStarter
public class SendToWalletActivity extends BaseActivity {
    private static final int REQUEST_CODE_SCAN = 1;
    @BindView(R.id.toolbar)
    CustomToolbar toolbar;
    @BindView(R.id.send_et)
    EditText sendEt;
    @BindView(R.id.address_et)
    EditText addressEt;
    @BindView(R.id.et_send_note)
    EditText sendNoteET;
    @BindView(R.id.scan_qr_code_iv)
    ImageView scanQrCodeIv;
    @BindView(R.id.coin_tv)
    TextView coinTv;
    @BindView(R.id.send_btn)
    Button sendBtn;
    @BindView(R.id.all_tv)
    TextView allTv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.total_coin_tv)
    TextView totalCoinTv;
    @BindView(R.id.total_coin_value_tv)
    TextView totalCoinValueTv;
    @BindView(R.id.tv_estimated)
    TextView estimated;
    @BindView(R.id.tv_received_amounts)
    TextView receivedAmounts;
    @BindView(R.id.ll_estimated_amounts)
    LinearLayout estimatedAmountsLl;
    @BindView(R.id.ll_received_amounts)
    LinearLayout receivedAmountsLl;
    @BindView(R.id.tv_send_wallet_nomore_ediamond)
    TextView nomoreEdiamondTextView;

    public SendCoinListResp resp;

    CallbackManager callbackManager;
    MessageDialog messageDialog;

    MyProfile userProfile;

    private CustomEDiamondDialog mCustomEDiamondDialog;
    private CustomEDiamondDialog.Builder mBuilderDialog;
    private View mDialogiew;// Dialog 中传入的 View
    private final String typeWithdrawCurrencyPwd = "6";// 提币密码
    private final String typeWithdrawCurrencyRequest = "7";// 提币请求
    private CommonCountDownTimer countDownTimer;
    private String pwd;
    private String address;
    private int coin;

    @OnClick(R.id.scan_qr_code_iv)
    public void onScanQRCodeClick() {
        startActivityForResult(new Intent(this, ScanQRCodeActivity.class), REQUEST_CODE_SCAN);
    }

    @OnClick(R.id.all_tv)
    public void onAllTvClick() {
        if (null == userProfile) {
            return;
        }
//        sendEt.setText(new BigDecimal(userProfile.getCoinValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
        sendEt.setText(userProfile.getTotalCoin());
        sendEt.setSelection(userProfile.getTotalCoin().length());
    }

    @OnClick(R.id.send_btn)
    public void onSendClick() {
        if (null == userProfile) {
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

        String address = addressEt.getText().toString();
        if (!StringUtils.isEmpty(address)) {
            if (address.startsWith("0x") && address.length() == 42) {
                this.address = address;
            } else {
                ToastUtils.showShort(getString(R.string.withdraw_currency_address_error));
                return;
            }

        }

        if (num > Integer.parseInt(userProfile.getTotalCoin())) {
            showLessDiamondDialog();
            return;
        }
        coin = num;
//        send(num);

        showAddressWarningDialog(sendEt.getText().toString(), addressEt.getText().toString());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_to_wallet;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {
        mBuilderDialog = new CustomEDiamondDialog.Builder(SendToWalletActivity.this);
        toolbar.setMenuClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordListFromWalletActivityStarter.start(SendToWalletActivity.this);
            }
        });
        sendEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputNumString = s.toString();
                if (!StringUtils.isEmpty(inputNumString)) {
                    if (inputNumString.startsWith("0")) {
                        sendBtn.setEnabled(false);
                        return;
                    }
                    estimatedAmountsLl.setVisibility(View.VISIBLE);
                    receivedAmountsLl.setVisibility(View.VISIBLE);
                    getWithdrawCurrencyTradePoundage(Integer.parseInt(s.toString()));
                } else {
                    estimatedAmountsLl.setVisibility(View.GONE);
                    receivedAmountsLl.setVisibility(View.GONE);
                    nomoreEdiamondTextView.setVisibility(View.GONE);
//                    estimated.setText("0");
                }
            }
        });
        addressEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isEmpty(s.toString()) || StringUtils.isEmpty(sendEt.getText())) {
                    sendBtn.setEnabled(false);
                } else {
                    sendBtn.setEnabled(true);
                }
            }
        });
        refreshLayout.setOnRefreshListener(refreshlayout -> getUserProfile());
    }

    @Override
    protected void initData() {
//        estimated.setText("0");
//        receivedAmounts.setText("0");
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

    private void getWithdrawCurrencyTradePoundage(int inputNum) {
        Api.getInstance()
                .txFee()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<WithdrawCurrencyTradePoundageRespond>() {
                    @Override
                    protected void onHandleSuccess(WithdrawCurrencyTradePoundageRespond tradePoundageRespond) {
                        BigDecimal tradePoundageDecimal = tradePoundageRespond.getEstimatedTxFee();
                        BigDecimal inputNumBigDecimal = BigDecimal.valueOf(inputNum);
                        BigDecimal num = inputNumBigDecimal.subtract(tradePoundageDecimal);
                        estimated.setText(tradePoundageDecimal.toString() + " EDD");
                        receivedAmounts.setText(num.toString() + " EDD"); // 用输入的数减去estimated
                        if (tradePoundageDecimal.compareTo(BigDecimal.valueOf(Integer.parseInt(userProfile.getTotalCoin()))) > 0 || tradePoundageDecimal.compareTo(inputNumBigDecimal) > 0) {
                            nomoreEdiamondTextView.setVisibility(View.VISIBLE);
                            receivedAmountsLl.setVisibility(View.GONE);
                            estimatedAmountsLl.setVisibility(View.GONE);
                        } else {
                            nomoreEdiamondTextView.setVisibility(View.GONE);
                            receivedAmountsLl.setVisibility(View.VISIBLE);
                            estimatedAmountsLl.setVisibility(View.VISIBLE);
                        }
                        if (StringUtils.isEmpty(addressEt.getText().toString())) {
                            sendBtn.setEnabled(false);
                            return;
                        }
                        sendBtn.setEnabled(true);
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    private void getUserProfile() {
        Api.getInstance().getUserProfile().compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<MyProfile>() {
                    @Override
                    protected void onHandleSuccess(MyProfile userProfile) {
                        resetData(userProfile);
                        getWithdrawCurrencyPwdStatus();
                    }

                    @Override
                    protected void onAfter() {
                        super.onAfter();
                    }
                });

        // 刷新过后会清空输入的内容，尤其是在提币完成后刷新一下数据
        addressEt.setText("");
        sendEt.setText("");
        sendNoteET.setText("");
        addressEt.clearFocus();
        sendEt.clearFocus();
//        estimated.setText("0");
//        receivedAmounts.setText("0");
        estimatedAmountsLl.setVisibility(View.GONE);
        receivedAmountsLl.setVisibility(View.GONE);
        nomoreEdiamondTextView.setVisibility(View.GONE);
    }

    private void resetData(MyProfile userProfile) {
        this.userProfile = userProfile;
        totalCoinTv.setText(userProfile.getTotalCoin());
        totalCoinValueTv
                .setText(new BigDecimal(userProfile.getCoinValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString());
    }

    private void getWithdrawCurrencyPwdStatus() {
        Api.getInstance()
                .passStatus()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<WithdrawCurrencyPwdStatusRespond>() {
                    @Override
                    protected void onHandleSuccess(WithdrawCurrencyPwdStatusRespond i) {
                        int status = i.getPassStatus();
                        if (status == 0)// 未设置，显示Dialog
                            showSetPwdDialog(typeWithdrawCurrencyPwd, status);
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        refreshLayout.finishRefresh();
                    }

                    @Override
                    protected void onAfter() {
                        super.onAfter();
                        refreshLayout.finishRefresh();
                    }
                });
    }

    private void showSetPwdDialog(String type, int status) {
        mCustomEDiamondDialog = mBuilderDialog.view(R.layout.dialog_send2wallet_setpwd)
                .setViewContent(R.id.setpwd_tv_title, type.equals(typeWithdrawCurrencyPwd) ? getString(R.string.dialog_setpwd_title) : getString(R.string.dialog_setpwd_title_enter))
                .setViewVisibility(R.id.etpwd_fl_pwd_confirm, type.equals(typeWithdrawCurrencyPwd) ? View.VISIBLE : View.GONE)
                .setViewOnClick(R.id.setpwd_bt_pwd_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pwdContent = ((EditText) mDialogiew.findViewById(R.id.setpwd_et_pwd)).getText().toString();
                        String pwdContentConfirm = ((EditText) mDialogiew.findViewById(R.id.setpwd_et_pwd_confirm)).getText().toString();
                        if (StringUtils.isEmpty(pwdContentConfirm)) {
                            if (StringUtils.isEmpty(pwdContent)) {
                                return;
                            }
                            // 先请求服务器查看用户输入的密码是否正确
                            verifyPwdFromeServer(pwdContent);
                        } else {
                            if (pwdContent.equals(pwdContentConfirm)) {
                                pwd = pwdContent;
                                getIdentifyingCode(typeWithdrawCurrencyPwd);// 设置密码
                            } else {
                                ToastUtils.showShort(getString(R.string.dialog_setpwd_confirm_error));
                            }
                        }
                    }
                })
                .build();
        mDialogiew = mCustomEDiamondDialog.getView();
        mCustomEDiamondDialog.show();

        if (status == 0) // 未设置密码，用户点击就把页面关闭
            mCustomEDiamondDialog.canceledOnBackKeyActivity(true);
        else // 设置了密码，用户点击就单纯做返回操作
            mCustomEDiamondDialog.canceledOnBackKeyActivity(false);
    }

    private void verifyPwdFromeServer(String pwdContent) {
        showLoading();
        Api.getInstance()
                .verifyPass(new WithdrawCurrencyVerifyPassParams(pwdContent))
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onHandleSuccess(Object respond) {
                        dismissLoading();
                        pwd = pwdContent;
                        getIdentifyingCode(typeWithdrawCurrencyRequest);// 输入密码 Dialog，准备提币
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                        switch (code) {
                            case "07003":
                                ToastUtils.showLong(R.string.withdraw_currency_07003);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoading();
                    }
                });
    }

    private void showAddressWarningDialog(String num, String address) {
        mCustomEDiamondDialog = mBuilderDialog.view(R.layout.dialog_send2wallet_warning)
                .setViewContent(R.id.warning_tv_content, getString(R.string.dialog_warning_address_content))
                .setViewContent(R.id.warning_tv_content_count, "Are you sure to withdraw '" + num + " EDD' to address")
                .setViewContent(R.id.warning_tv_content_adress, address)
                .setViewOnClick(R.id.warning_bt_pwd_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog(mCustomEDiamondDialog);
                    }
                })
                .setViewOnClick(R.id.warning_bt_pwd_confirm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog(mCustomEDiamondDialog);
                        showSetPwdDialog(typeWithdrawCurrencyRequest, 1);
                    }
                })
                .build();
        mDialogiew = mCustomEDiamondDialog.getView();
        mCustomEDiamondDialog.show();
    }

    private void showLessDiamondDialog() {
        LessEDiamondDialog dialog = LessEDiamondDialog.newInstance();
        dialog.show(getSupportFragmentManager(), "less_diamond");
    }

    private void send(int num) {
        Api.getInstance()
                .directSendCoin(new SendParams(String.valueOf(num)))
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<SendResp>() {
                    @Override
                    protected void onHandleSuccess(SendResp resp) {
                        if (resp.isHasRelation()) {
                            setBalance(resp.getCoinTotal());
                            EventBus.getDefault().postSticky(new SendCoinEvent(num));
                            SendToWalletActivity.this.finish();
                        } else {
                            FbMessagerShare.share(messageDialog, resp.getUrl());
                        }
                    }
                });
    }

    public void getSendCoinList() {
    }

    private void setBalance(int num) {
        coinTv.setText(getString(R.string.activity_recharge_balance, num));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCAN) {
            if (data != null) {
                String result = data.getStringExtra(ScanQRCodeActivity.RESULT_QRCODE_TEXT);
                if (result == null) {
                    ToastUtils.showShort(R.string.str_scan_qr_code_hint);
                    return;
                }
                addressEt.setText(result);
                addressEt.setSelection(result.length());
            }
            return;
        }
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void dismissDialog(CustomEDiamondDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * 获取验证码请求
     * 6 - 设置提币密码
     * 7 - 发起提币请求
     */
    private void getIdentifyingCode(String type) {
        showLoading();
        WithdrawCurrencyIdentifyingCodeParams withdrawCurrencyIdentifyingCodeParams = new WithdrawCurrencyIdentifyingCodeParams(Integer.parseInt(type));
        Api.getInstance()
                .getPassSms(withdrawCurrencyIdentifyingCodeParams)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<WithdrawCurrencyIdentifyingCodeRespond>() {
                    @Override
                    protected void onHandleSuccess(WithdrawCurrencyIdentifyingCodeRespond mobile) {
                        dismissLoading();
                        dismissDialog(mCustomEDiamondDialog);
                        String phone = mobile.getMobile();
                        inputIdentifyingCodeDialog(type, phone);
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

    private void inputIdentifyingCodeDialog(String type, String phone) {
        mCustomEDiamondDialog = mBuilderDialog.view(R.layout.activity_check_code)
                .setViewVisibility(R.id.toolbar, View.GONE)
                .setViewContent(R.id.reg_phone_tv, "+" + phone)
                .setViewVisibility(R.id.check_login_layout, View.VISIBLE)
                .setViewVisibility(R.id.change_phone_tv, View.GONE)
                .setViewOnClick(R.id.action_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = ((IdentifyingCodeView) mDialogiew.findViewById(R.id.check_code_view)).getTextContent();
                        if (content.trim().length() == 4) {
                            if (type.equals(typeWithdrawCurrencyPwd))
                                setWithdrawCurrencyPwd(pwd, content);
                            else
                                withdrawCurrencyRequest(pwd, content);
                        } else {
                            ToastUtils.showShort(getString(R.string.withdraw_currency_identifying_code_error));
                            return;
                        }
                    }
                })
                .build();
        mDialogiew = mCustomEDiamondDialog.getView();
        mDialogiew.findViewById(R.id.action_btn).setEnabled(true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        mDialogiew.findViewById(R.id.tv_check_code_title).setLayoutParams(layoutParams);
        FrameLayout.LayoutParams layoutParamsFrameLayout = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsFrameLayout.setMargins(0, SizeUtils.dp2px(13), 0, 0);
        mDialogiew.findViewById(R.id.check_login_layout).setLayoutParams(layoutParamsFrameLayout);

        TextView textView = mDialogiew.findViewById(R.id.check_count_down_tv);
        countDownTimer = new CommonCountDownTimer(60 * 1000, new CommonCountDownTimer.CommonCountDownCallback() {
            @Override
            public void onTimerTick(long millisUntilFinished) {
                textView.setText(getString(R.string.repeate_verification_code_after_27_seconds, "" + millisUntilFinished / 1000));
            }

            @Override
            public void onTimerFinish() {
                textView.setVisibility(View.GONE);
            }
        });
        countDownTimer.start();
        mCustomEDiamondDialog.show();
    }

    /**
     * 调用设置提币密码接口
     * <p>
     */
    private void setWithdrawCurrencyPwd(String pwd, String code) {
        if (StringUtils.isEmpty(pwd))
            return;
        showLoading();
        WithdrawCurrencySetPwdParams withdrawCurrencySetPwdParams = new WithdrawCurrencySetPwdParams(pwd, code);
        Api.getInstance()
                .withdrawPass(withdrawCurrencySetPwdParams)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onHandleSuccess(Object s) {
                        mCustomEDiamondDialog.canceledOnBackKeyActivity(false);
                        dismissLoading();
                        dismissDialog(mCustomEDiamondDialog);
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                        switch (code) {
                            case "07002":
                                ToastUtils.showLong(R.string.withdraw_currency_07002);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoading();
                    }
                });
    }


    /**
     * 提币接口
     * <p>
     */
    private void withdrawCurrencyRequest(String pwd, String code) {
        showLoading();
        String note = sendNoteET.getText().toString();
        WithdrawCurrencyParams withdrawCurrencyParams = new WithdrawCurrencyParams(coin, address, pwd, code, note);
        Api.getInstance()
                .newTx(withdrawCurrencyParams)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Object>() {
                    @Override
                    protected void onHandleSuccess(Object o) {
                        dismissLoading();
                        dismissDialog(mCustomEDiamondDialog);
                        showWithdrawCurrencyCompleteDialog();
                        getUserProfile();
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        super.onHandleError(code, msg);
                        dismissLoading();
                        // Withdraw Currency Error Hint
                        switch (code) {
                            case "07001":
                                ToastUtils.showLong(R.string.withdraw_currency_07001);
                                break;
                            case "07003":
                                ToastUtils.showLong(R.string.withdraw_currency_07003);
                                break;
                            case "07004":
                                ToastUtils.showLong(R.string.withdraw_currency_07004);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoading();
                    }
                });
    }

    private void showWithdrawCurrencyCompleteDialog() {
        mCustomEDiamondDialog = mBuilderDialog.view(R.layout.dialog_send2wallet_right)
                .canceledOnTouchOutside(true)
                .build();
        mDialogiew = mCustomEDiamondDialog.getView();
        mCustomEDiamondDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog(mCustomEDiamondDialog);
    }
}

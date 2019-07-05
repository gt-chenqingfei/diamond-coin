package com.lovecoin.ediamond.ui.recharge;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.api.Api;
import com.lovecoin.ediamond.api.BaseObserver;
import com.lovecoin.ediamond.api.entity.params.IosBuyParams;
import com.lovecoin.ediamond.api.entity.resp.RechargeResp;
import com.lovecoin.ediamond.api.utils.RxSchedulers;
import com.lovecoin.ediamond.event.BalanceChangeEvent;
import com.lovecoin.ediamond.event.CoinChangeEvent;
import com.lovecoin.ediamond.ui.account.MyAccountActivityStarter;
import com.lovecoin.ediamond.ui.base.BaseActivity;
import com.lovecoin.ediamond.ui.dialog.LessBalanceDialog;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;

import activitystarter.MakeActivityStarter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Buy eDiamonds
 */
@MakeActivityStarter
public class RechargeActivity extends BaseActivity {

    @BindView(R.id.action_btn)
    Button actionBtn;
    @BindView(R.id.recharge_num_et)
    EditText rechargeNumEt;
    @BindView(R.id.balance_tv)
    TextView balanceTv;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.my_account_balance_tv)
    TextView myAccountBalanceTv;
    @BindView(R.id.real_time_price_tv)
    TextView realTimePriceTv;
    @BindView(R.id.real_time_total_price_tv)
    TextView realTimeTotalPriceTv;

    private RechargeResp resp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void onBeforeSetContentLayout() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    protected void initView() {

        rechargeNumEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int len = s.toString().length();
                if (len == 1 && text.equals("0")) {
                    s.clear();
                }

                actionBtn.setEnabled(s.toString().length() != 0);
                if (resp == null) {
                    return;
                }
                int num;
                try {
                    num = Integer.parseInt(s.toString());
                    realTimeTotalPriceTv.setText(String
                            .format("$%s", resp.getSinglePrice().multiply(new BigDecimal(num)).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    realTimeTotalPriceTv.setText("$0.00");
                }
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getRechageList();
            }
        });
    }

    @Override
    protected void initData() {
        refreshLayout.autoRefresh();
    }

    @OnClick(R.id.action_btn)
    public void onViewClick(View view) {
        if (resp == null) {
            return;
        }

        String numStr = rechargeNumEt.getText().toString();
        if (StringUtils.isEmpty(numStr)) {
            return;
        }

        Integer num = Integer.valueOf(numStr);
        buy(num);
        rechargeNumEt.setText("");
    }

    @OnClick(R.id.recharge_tv)
    public void onRechargeClick(View view) {
        MyAccountActivityStarter.start(this);
    }

    private void buy(int num) {
        IosBuyParams params = new IosBuyParams(String.valueOf(num));
        showLoading();
        Api.getInstance()
                .iosBuyCoin(params)
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<Double>() {

                    @Override
                    protected void onHandleSuccess(Double balance) {
                        dismissLoading();
                        resp.setTotalCoin(resp.getTotalCoin() + num);
                        balanceTv.setText(String.valueOf(resp.getTotalCoin()));
                        myAccountBalanceTv.setText(String.format("$%s", new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()));
                        EventBus.getDefault().postSticky(new BalanceChangeEvent(num));
                    }

                    @Override
                    protected void onHandleError(String code, String msg) {
                        dismissLoading();
                        if ("04006".equals(code)) {
                            RechargeActivity activity = RechargeActivity.this;
                            if (activity.isDestroyed() || activity.isFinishing() || hasSave) {
                                return;
                            }
                            LessBalanceDialog dialog = LessBalanceDialog.newInstance();
                            Bundle bundle = new Bundle();
                            bundle.putString(LessBalanceDialog.EXTRA_BALANCE, myAccountBalanceTv.getText().toString());
                            dialog.setArguments(bundle);

                            dialog.show(getSupportFragmentManager(), "less_balance");
                        } else {
                            super.onHandleError(code, msg);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissLoading();
                    }
                });
    }

    private boolean hasSave = false;

    @Override
    protected void onResume() {
        super.onResume();
        hasSave = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        hasSave = true;
    }

    private void getRechageList() {
        Api.getInstance()
                .getRechargeList()
                .compose(RxSchedulers.compose())
                .subscribe(new BaseObserver<RechargeResp>() {
                    @Override
                    protected void onHandleSuccess(RechargeResp resp) {
                        onSuccesss(resp);
                    }

                    @Override
                    protected void onAfter() {
                        super.onAfter();
                        refreshLayout.finishRefresh();
                    }
                });
    }

    private void onSuccesss(RechargeResp resp) {
        this.resp = resp;
        balanceTv.setText(String.valueOf(resp.getTotalCoin()));
        myAccountBalanceTv.setText(String.format("$%s", new BigDecimal(resp.getBalance()).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()));
        realTimePriceTv.setText(String.format("$%s", resp.getSinglePrice().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()));
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
    public void onBalanceChangeEvent(BalanceChangeEvent e) {
        BalanceChangeEvent event = EventBus.getDefault().removeStickyEvent(BalanceChangeEvent.class);
        if (event == null) {
            return;
        }

        refreshLayout.autoRefresh();
    }

}

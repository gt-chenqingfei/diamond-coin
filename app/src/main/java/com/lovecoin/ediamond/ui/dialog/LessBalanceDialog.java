package com.lovecoin.ediamond.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.account.MyAccountActivityStarter;

/**
 * Created on 2017/11/26.
 */

public class LessBalanceDialog extends AppCompatDialogFragment {
    public static String EXTRA_BALANCE = "extra_balance";

    public static LessBalanceDialog newInstance() {
        return new LessBalanceDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.shadow_round_12);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_less_balance, null);

        View recharge = view.findViewById(R.id.action_layout);
        TextView tvInsufficientBalance = view.findViewById(R.id.insufficient_balance_tv);
        String balance = getArguments().getString(EXTRA_BALANCE, "$0.0");
        tvInsufficientBalance.setText(balance);

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAccountActivityStarter.start(getContext());
                dismiss();
            }
        });

        return view;
    }

}

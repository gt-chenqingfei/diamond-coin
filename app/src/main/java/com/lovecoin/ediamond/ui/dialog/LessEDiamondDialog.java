package com.lovecoin.ediamond.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lovecoin.ediamond.R;
import com.lovecoin.ediamond.ui.recharge.RechargeActivityStarter;

/**
 * Created on 2017/11/26.
 */

public class LessEDiamondDialog extends AppCompatDialogFragment {

    public static LessEDiamondDialog newInstance() {
        return new LessEDiamondDialog();
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_less_ediamond, null);

        View recharge = view.findViewById(R.id.action_layout);

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RechargeActivityStarter.start(getContext());
                dismiss();
            }
        });

        return view;
    }

}

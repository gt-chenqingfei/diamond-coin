package com.lovecoin.ediamond.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lovecoin.ediamond.R;

/**
 * Created on 2017/11/26.
 */

public class CancelRelationDialog extends AppCompatDialogFragment {

    private boolean isFirst = true;
    private OnSureListener listener;

    public static CancelRelationDialog newInstance() {
        return new CancelRelationDialog();
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cancel_relation, null);

        TextView confirmTv = view.findViewById(R.id.comfirm_tv);
        TextView cancelTv = view.findViewById(R.id.cancle_tv);
        EditText inputEt = view.findViewById(R.id.input_et);

        TextView tipsAgainTv = view.findViewById(R.id.tips_again_tv);

        String iAmSure = getString(R.string.dialog_cancel_relation_i_am_sure);

        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputEt.getText().toString();
                if (input.isEmpty() || !input.toUpperCase().equals(iAmSure.toUpperCase())) {
                    return;
                }

                if (isFirst) {
                    tipsAgainTv.setVisibility(View.VISIBLE);
                    inputEt.setText("");
                    inputEt.setHint(R.string.dialog_cancel_relation_input_hint_again);
                    isFirst = false;
                    return;
                }

                if (listener != null) {
                    listener.onSure();
                }
                dismiss();
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    public interface OnSureListener {
        void onSure();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnSureListener) context;
        } catch (ClassCastException ignored) {
        }

    }
}

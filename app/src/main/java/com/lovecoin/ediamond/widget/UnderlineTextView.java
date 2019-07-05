package com.lovecoin.ediamond.widget;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * textview with under line
 */

public class UnderlineTextView extends AppCompatTextView {
    public UnderlineTextView(Context context) {
        super(context);
        init();
    }

    public UnderlineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnderlineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }
}

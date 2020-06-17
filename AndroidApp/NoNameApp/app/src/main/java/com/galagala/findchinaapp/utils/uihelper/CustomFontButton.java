package com.galagala.findchinaapp.utils.uihelper;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import androidx.appcompat.widget.AppCompatButton;
import com.galagala.findchinaapp.R;

public class CustomFontButton extends AppCompatButton {
    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomFontButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        applyCustomFont(context, attributeSet);
    }

    public CustomFontButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        applyCustomFont(context, attributeSet);
    }

    private void applyCustomFont(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FontFamily);// CustomFontTextView?? ->FontFamily
        Typeface selectTypeface = selectTypeface(context, obtainStyledAttributes.getString(0), attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "textStyle", 0));
        if (selectTypeface != null) {
            setTypeface(selectTypeface, getTypeface().getStyle());
            obtainStyledAttributes.recycle();
        }
    }

    private Typeface selectTypeface(Context context, String str, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(".ttf");
        return getTypeface(sb.toString(), context);
    }

    private Typeface getTypeface(String str, Context context) {
        try {
            AssetManager assets = context.getAssets();
            StringBuilder sb = new StringBuilder();
            sb.append("font/");
            sb.append(str);
            return Typeface.createFromAsset(assets, sb.toString());
        } catch (Exception e) {
            Log.e("failed", e.getMessage());
            return null;
        }
    }
}

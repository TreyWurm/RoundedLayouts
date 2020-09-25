package com.appmea.roundedlayouts;

import android.content.Context;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

public class ColorUtils {

    private final Context context;

    public ColorUtils(Context context) {
        this.context = context;
    }

    public int getColorSurface() {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(R.attr.colorSurface, typedValue, true)) {
            return typedValue.data;
        }
        return ContextCompat.getColor(context, R.color.dtp_default_surface_color);
    }
}

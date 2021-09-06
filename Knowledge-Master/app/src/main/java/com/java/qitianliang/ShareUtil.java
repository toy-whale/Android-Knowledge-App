package com.java.qitianliang;

import android.content.Context;
import android.content.Intent;

public class ShareUtil {
    public static void shareText(Context context, String text, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, title));
    }
}

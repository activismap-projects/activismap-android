package com.entropy_factory.activismap.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

import com.entropy_factory.activismap.app.ActivisApplication;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ander on 26/11/15.
 */
public class Utils {

    public static Bitmap toBitmap(File file) {
        return toBitmap(file.getAbsolutePath());
    }

    public static Bitmap toBitmap(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public static Bitmap toBitmap(byte[] bitmap) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length, options);
    }

    public static byte[] bitmapToByteArray(File file) {
        return bitmapToByteArray(file.getAbsolutePath());
    }

    public static byte[] bitmapToByteArray(String bitmapPath) {
        Bitmap bitmap = toBitmap(bitmapPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
        return  baos.toByteArray();
    }

    public static String parseDate(long millis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        String pattern = "EEE',' d MMM y HH:mm";
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(c.getTime());
    }

    public static int convertDpToPixels(Context context, int dp) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static long toNanoCoins(String value) {
        double val = Double.parseDouble(value) * 1e8d;
        return BigDecimal.valueOf(val).longValue();
    }

    public static Bitmap decodeBitmap(Context context, Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public static String findByRegularExpression(CharSequence input, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(input);
        boolean found = matcher.find();
        if (found) {
            return matcher.group();
        }

        return null;
    }

    public static boolean isBtcAddress(String address) {
        return !TextUtils.isEmpty(Utils.findByRegularExpression(address, "[13][0-9A-Za-z]{25,34}"));
    }

    public static String uniqueId() {
        return Long.toString(System.currentTimeMillis(), 36);
    }

    public static String locationToString(LatLng location) {
        Geocoder geocoder = new Geocoder(ActivisApplication.INSTANCE, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(address.getAddressLine(i)).append(" ");
                }

                return strReturnedAddress.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Imposible retrive location";
    }

    public static String locationToString(double latitude, double longitude) {
        return locationToString(new LatLng(latitude, longitude));
    }

    public static String humanReadable(long num) {
        if (num > 999999) {
            return  Math.round(num / 1000000) + "M";
        } else if (num > 999) {
            return  Math.round(num / 1000) + "K";
        } else {
            return String.valueOf(num);
        }
    }

    public static String humanReadable(long num, @StringRes int resId) {
        String numString = humanReadable(num);
        return ActivisApplication.INSTANCE.getString(resId, numString);
    }

}

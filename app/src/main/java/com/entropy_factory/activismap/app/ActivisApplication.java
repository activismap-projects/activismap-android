package com.entropy_factory.activismap.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatDelegate;
import android.widget.ImageView;

import com.activeandroid.app.Application;
import com.bumptech.glide.Glide;
import com.entropy_factory.activismap.core.activis.ActivisListener;
import com.entropy_factory.activismap.core.activis.ActivisResponse;
import com.entropy_factory.activismap.core.activis.BaseActivis;
import com.entropy_factory.activismap.core.db.ActivisEvent;
import com.entropy_factory.activismap.core.db.User;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

/**
 * Created by ander on 30/09/16.
 */
public class ActivisApplication extends Application {

    private static final String TAG = "ActiviApplication";

    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    public static ActivisApplication INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        INSTANCE = this;

        ActivisEvent.removeOld();
        new BaseActivis(this).getAccount(new ActivisListener<User>() {
            @Override
            public void onResponse(ActivisResponse<User> response) {
                if (!response.hasError()) {
                    response.getElementAt(0).save();
                }
            }
        });

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }
        });
    }

    public boolean isDebuggable() {
        return isDebuggable(this);
    }

    public boolean isDebuggable(Context ctx) {
        boolean debuggable = false;

        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            for (Signature signature : signatures) {
                ByteArrayInputStream stream = new ByteArrayInputStream(signature.toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) {
                    break;
                }
            }
        } catch (PackageManager.NameNotFoundException | CertificateException e) {
            //debuggable variable will remain false
        }
        return debuggable;
    }
}

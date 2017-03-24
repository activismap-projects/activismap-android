package com.entropy_factory.activismap.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

import static android.provider.DocumentsContract.PROVIDER_INTERFACE;

/**
 * Created by ander on 13/06/16.
 */
public class FileUtils {

    private static final String TAG = "FileUtils";
    private static final String PATH_ROOT = "root";
    private static final String PATH_RECENT = "recent";
    private static final String PATH_DOCUMENT = "document";
    private static final String PATH_CHILDREN = "children";
    private static final String PATH_SEARCH = "search";
    private static final String PATH_TREE = "tree";

    private static final String PARAM_QUERY = "query";
    private static final String PARAM_MANAGE = "manage";

    public static File file(String path) {
        return new File(path);
    }

    public static File file(Context context, Uri uri) throws URISyntaxException {
        return file(getPath(context, uri));
    }

    public static String getName(Context context, Uri uri) throws URISyntaxException {
        String path = getPath(context, uri);
        File f = new File(path);
        return f.getName();
    }

    public static String getContentType(File f) {
        String ext = getExtension(f);
        String mimeType =  MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);

        if (TextUtils.isEmpty(mimeType)) {
            mimeType = "*/*";
        }

        return mimeType;
    }

    public static String getExtension(String path) {
        int index = path.lastIndexOf(".");
        if (index >= 0) {
            return path.substring(index+1, path.length());
        } else {
            return "";
        }
    }

    public static String getExtension (File file) {
        return getExtension(file == null ? "" : file.getAbsolutePath());
    }

    public static String toBase64(String path) throws IOException {
        return  toBase64(new File(path));
    }

    public static String toBase64(File file) {

        byte[] bytes;
        try {
            bytes = loadFile(file);
            byte[] encoded = Base64.encode(bytes, Base64.DEFAULT);

            return new String(encoded);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }

    public static File toFile(byte[] data, String path) throws IOException {
        return toFile(data, new File(path));
    }

    public static File toFile(byte[] data, File file) throws IOException {
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(data);
        fo.flush();
        fo.close();

        return file;
    }

    /**
     * Method for return file path of Gallery image
     *
     * @param context
     * @param uri
     * @return path of the selected image file from gallery
     */
    public static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isDocumentUri(Context context, Uri uri) {
        if (isContentUri(uri) && isDocumentsProvider(context, uri.getAuthority())) {
            final List<String> paths = uri.getPathSegments();
            if (paths.size() == 2) {
                return PATH_DOCUMENT.equals(paths.get(0));
            } else if (paths.size() == 4) {
                return PATH_TREE.equals(paths.get(0)) && PATH_DOCUMENT.equals(paths.get(2));
            }
        }
        return false;
    }

    public static boolean isContentUri(@Nullable Uri uri) {
        return uri != null && ContentResolver.SCHEME_CONTENT.equals(uri.getScheme());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isDocumentsProvider(Context context, String authority) {
        final Intent intent = new Intent(PROVIDER_INTERFACE);
        final List<ResolveInfo> infos = context.getPackageManager()
                .queryIntentContentProviders(intent, 0);
        for (ResolveInfo info : infos) {
            if (authority.equals(info.providerInfo.authority)) {
                return true;
            }
        }
        return false;
    }
}

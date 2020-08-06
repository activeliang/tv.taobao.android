package android.taobao.windvane.xmlmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.taobao.windvane.util.TaoLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.IOException;

public class WVXmlResUtils {
    public static View obtainViewFromAssets(Context cxt, String resourcepath, String filename) {
        return new WVLoadableResources(resourcepath).loadLayout(cxt, filename);
    }

    public static Bitmap obtainBitmapFromAssets(Context cxt, String path) {
        try {
            return BitmapFactory.decodeStream(cxt.getAssets().open(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static View obtainViewFromFile(Context cxt, String path, ViewGroup root) {
        try {
            return LayoutInflater.from(cxt).inflate(new WVFileParser().openXmlResourceParser(path), root);
        } catch (Throwable e) {
            TaoLog.e("Puti Inflater XmlBlock Error", e.toString());
            return null;
        }
    }
}

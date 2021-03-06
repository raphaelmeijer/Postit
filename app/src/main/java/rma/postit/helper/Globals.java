package rma.postit.helper;

import android.app.ProgressDialog;
import android.content.Context;

public class Globals {
    public static final String[] CATEGORIES = {
        "Cars",
        "Technology",
        "Education",
        "Funny",
        "News",
        "Other"
    };
    public static ProgressDialog getLoadingDialog(Context context){
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Loading..");
        pd.setMessage("Loading.... Please wait.");

        return pd;
    }

    public static String parseUriString(String uri) {
        uri = !uri.startsWith("http") && !uri.startsWith("https") ? "http://" + uri : uri;

        return uri;
    }
}

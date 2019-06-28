package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

public class NewsClass {
    private static String _currentFileName = "NewsClass"; // le nom du fichier
    Context _context;
    private String _id;
    private String _message = "";
    private String _prop;
    private String _language_code;
    private long _date_activation;
    private String _image_url;
    private Bitmap _image;
    //private boolean _priority;
    //private boolean _mobile_only;

    public NewsClass(Context context, JSONObject news) {
        try {
            this._context           = context;
            this._id                = news.getJSONObject("_id").getString("$oid");
            for (int i = 0; i < news.getJSONArray("Messages").length(); i++) {
                if ((_language_code = news.getJSONArray("Messages").getJSONObject(i).getString("LanguageCode")).compareTo("en") == 0) { // todo  add a parameter for the language
                    _message = news.getJSONArray("Messages").getJSONObject(i).getString("Message");
                    break;
                }
            }
            this._prop              = news.getString("Prop");
            this._date_activation   = news.getJSONObject("Date").getJSONObject("$date").getLong("$numberLong");
            if (news.has("ImageUrl"))  news.getString("ImageUrl");

            //this._priority          = news.getBoolean("Priority");
            //this._mobile_only       = news.getBoolean("MobileOnly");

        } catch (Exception e) {
            Log.e(_currentFileName,"Cannot load news - " + e.getMessage());
        }
    }

    /**
     * Time elapsed since the news was posted
     *
     * @return      long
     */
    public long getDate() { return ((_date_activation - System.currentTimeMillis()) * -1); }

    /**
     * Activation date
     *
     * @return      long
     */
    public long getDateActivation() { return (_date_activation * -1); }

    /**
     * News language code
     *
     * @return      string
     */
    public String getLanguageCode() { return _language_code; }

    /**
     * News title
     *
     * @return      string
     */
    public String getMessage() { return _message; }

    /**
     * News url
     *
     * @return      string
     */
    public String getUrl() { return _prop; }

    /**
     * News ID
     *
     * @return      string
     */
    public String getId() { return _id;}

    /**
     * News image
     *
     * @return      bitmap
     */
    public Bitmap getImage() {return _image; }

    /**
     * True if news image is downloaded
     *
     * @return      boolean
     */
    private boolean isImageDownloaded() {return _image != null; }

    /**
     * Download news image
     */
    public void downloadImage() {
        if (_image_url != null && !isImageDownloaded()) {
            try {
                URL url = new URL(_image_url);
                _image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception ex) {
                Log.e(_currentFileName,"Cannot load image for news : " + _id);
            }
        }
    }

    /**
     * When the news was posted
     *
     * @return      boolean
     */

    public String getTimeAgo()
    {
        return _context.getResources().getString(_context.getResources().getIdentifier("news_date_ago", "string", _context.getPackageName()), TimestampToTimeleft.convert(getDate(),false));
    }

}
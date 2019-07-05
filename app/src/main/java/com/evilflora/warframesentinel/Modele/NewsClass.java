package com.evilflora.warframesentinel.Modele;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.evilflora.warframesentinel.Utils.NumberToTimeLeft;

import org.json.JSONObject;

import java.net.URL;

public class NewsClass {
    private static String _currentFileName = "NewsClass";
    Context _context;
    private String _id;
    private String _message = "";
    private String _prop;
    private String _languageCode;
    private long _dateActivation;
    private String _imageUrl;
    private Bitmap _image;
    //private boolean _priority;
    //private boolean _mobileOnly;

    public NewsClass(Context context, JSONObject news) {
        try {
            this._context           = context;
            this._id                = news.getJSONObject("_id").getString("$oid");
            for (int i = 0; i < news.getJSONArray("Messages").length(); i++) {
                if ((_languageCode = news.getJSONArray("Messages").getJSONObject(i).getString("LanguageCode")).compareTo("en") == 0) {
                    _message = news.getJSONArray("Messages").getJSONObject(i).getString("Message");
                    break;
                }
            }
            this._prop              = news.getString("Prop");
            this._dateActivation   = news.getJSONObject("Date").getJSONObject("$date").getLong("$numberLong");
            if (news.has("ImageUrl"))  news.getString("ImageUrl");

            //this._priority          = news.getBoolean("Priority");
            //this._mobileOnly       = news.getBoolean("MobileOnly");

        } catch (Exception e) {
            Log.e(_currentFileName,"Cannot load news - " + e.getMessage());
        }
    }

    /**
     * Time elapsed since the news was posted
     *
     * @return      long
     */
    public long getDate() { return ((_dateActivation - System.currentTimeMillis()) * -1); }

    /**
     * Activation date
     *
     * @return      long
     */
    public long getDateActivation() { return (_dateActivation * -1); }

    /**
     * News language code
     *
     * @return      string
     */
    public String getLanguageCode() { return _languageCode; }

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
        if (_imageUrl != null && !isImageDownloaded()) {
            try {
                URL url = new URL(_imageUrl);
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
        return _context.getResources().getString(_context.getResources().getIdentifier("news_date_ago", "string", _context.getPackageName()), NumberToTimeLeft.convert(getDate(),false));
    }

}
package com.evilflora.warframesentinel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

class NewsClass {
    private final String CurrentFileName = "NewsClass"; // le nom du fichier
    private String _id;
    private String _message = "";
    private String _prop;
    private String _language_code;
    private long _date_activation;
    private String _image_url;
    private Bitmap _image;
    //private boolean _priority;
    //private boolean _mobile_only;

    NewsClass(JSONObject news) {
        try {
            this._id                = news.getJSONObject("_id").getString("$oid");
            for (int i = 0; i < news.getJSONArray("Messages").length(); i++) {
                if ( (_language_code = news.getJSONArray("Messages").getJSONObject(i).getString("LanguageCode")).compareTo("en") == 0) { // todo  add a parameter for the language
                    _message = news.getJSONArray("Messages").getJSONObject(i).getString("Message");
                    break;
                }
            }
            this._prop              = news.getString("Prop");
            this._date_activation   = news.getJSONObject("Date").getJSONObject("$date").getLong("$numberLong");
            try {
                this._image_url         = news.getString("ImageUrl");
            } catch(Exception ex) {
                Log.i(CurrentFileName, "News has no images");
            }
            //this._priority          = news.getBoolean("Priority");
            //this._mobile_only       = news.getBoolean("MobileOnly");

        } catch (Exception e) {
            Log.e(CurrentFileName,"Connat load news - " + e.getMessage());
        }
    }

    /**
     * Time elapsed since the news was posted
     *
     * @return      long
     */
    long get_date() { return ((_date_activation - System.currentTimeMillis()) * -1); }

    /**
     * Activation date
     *
     * @return      long
     */
    long get_date_activation() { return (_date_activation * -1); }

    /**
     * News language code
     *
     * @return      string
     */
    String get_language_code() { return _language_code; }

    /**
     * News title
     *
     * @return      string
     */
    String get_message() { return _message; }

    /**
     * News url
     *
     * @return      string
     */
    String get_url() { return _prop; }

    /**
     * News ID
     *
     * @return      string
     */
    String get_id() { return _id;}

    /**
     * News image
     *
     * @return      bitmap
     */
    public Bitmap get_image() {return _image; }

    /**
     * True if news image is downloaded
     *
     * @return      boolean
     */
    public boolean is_image_downloaded() {return _image != null; }

    /**
     * Download news image
     */
    public void download_image() {
        if (_image_url != null && !is_image_downloaded()) {
            try {
                URL url = new URL(_image_url);
                _image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception ex) {
                Log.e(CurrentFileName,"Cannot load image for news : " + _id);
            }
        }
    }

}
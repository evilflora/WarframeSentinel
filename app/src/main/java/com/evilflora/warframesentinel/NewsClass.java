package com.evilflora.warframesentinel;

import android.util.Log;

import org.json.JSONObject;

class NewsClass {
    private String _id;
    private String _message = "";
    private String _prop;
    private String _language_code;
    private long _date_activation;
    //private String _image_url;
    //private Bitmap _image;
    //private boolean _priority;
    //private boolean _mobile_only;

    NewsClass(JSONObject news) { // constructor
        try {
            this._id                = news.getJSONObject("_id").getString("$oid");
            for (int i = 0; i < news.getJSONArray("Messages").length(); i++) {
                if ( (_language_code = news.getJSONArray("Messages").getJSONObject(i).getString("LanguageCode")).compareTo("en") == 0) { // todo il faudra rajouter un paramètre pour la langue
                    _message = news.getJSONArray("Messages").getJSONObject(i).getString("Message");
                    break;
                }
            }
            this._prop              = news.getString("Prop");
            this._date_activation   = news.getJSONObject("Date").getJSONObject("$date").getLong("$numberLong");
            /*try {
                this._image_url         = news.getString("ImageUrl");
            } catch(Exception ex) {
                Log.i("NewsClass", "News has no images");
            }*/
            /*if (_image_url != null) {
                try {
                    URL url = new URL(_image_url);
                    _image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception ex) {
                    Log.e("NewsClass","Cannot load image for news : " + _id);
                }
            }*/
            //this._priority          = news.getBoolean("Priority");
            //this._mobile_only       = news.getBoolean("MobileOnly");

        } catch (Exception e) {
            Log.e("NewsClass","Error while reading news - " + e.getMessage());
        }
    }

    String get_date() {
        return (TimestampToDate.convert(((_date_activation - System.currentTimeMillis())*-1),false) + " ago");
    }

    long get_date_activation() { return (_date_activation * -1); }

    String get_language_code() {
        return _language_code;
    }

    String get_type() {
        return _message;
    }

    String get_url() { return _prop; }

    String get_id() { return _id;}

    /*public Bitmap get_image() {return _image; }*/

    /*public String get_image_url() {return _image_url; }*/

}
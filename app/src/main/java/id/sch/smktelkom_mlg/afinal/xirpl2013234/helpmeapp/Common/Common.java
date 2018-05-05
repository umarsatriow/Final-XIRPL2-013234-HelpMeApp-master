package id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp.Common;

import android.location.Location;

import id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp.Model.Rider;
import id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp.Remote.FCMClient;
import id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp.Remote.IFCMService;

public class Common {
    public static final String driver_tbl = "Drivers";
    public static final String user_driver_tbl = "Users";
    public static final String user_rider_tbl = "Riders";
    public static final String pickup_request_tbl = "PickupRequest";
    public static final String token_tbl = "Tokens";
    public static final int PICK_IMAGE_REQUEST = 9999;
    public static final String fcmURL = "https://fcm.googleapis.com/";
    public static final String user_field = "rider_user";
    public static final String pwd_field = "rider_pwd";
    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";
    public static Rider currentUser;
    public static Location mLastLocation = null;

    public static IFCMService getFCMService() {
        return FCMClient.getClient(fcmURL).create(IFCMService.class);
    }


}

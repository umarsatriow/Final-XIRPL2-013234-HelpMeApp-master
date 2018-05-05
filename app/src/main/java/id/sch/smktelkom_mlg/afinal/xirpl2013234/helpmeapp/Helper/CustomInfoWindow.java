package id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import id.sch.smktelkom_mlg.afinal.xirpl2013234.helpme.R;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    View myView;

    public CustomInfoWindow(Context context) {
        myView = LayoutInflater.from(context)
                .inflate(R.layout.custom_rider_info_window, null);

    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView txtPickupTitle = myView.findViewById(R.id.txtPickupInfo);
        txtPickupTitle.setText(marker.getTitle());

        TextView txtPickupSnippet = myView.findViewById(R.id.txtPickupSnippet);
        txtPickupSnippet.setText(marker.getSnippet());

        return myView;

    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}

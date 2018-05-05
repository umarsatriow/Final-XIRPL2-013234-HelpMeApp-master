package id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp.Remote;

import id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp.Model.FCMResponse;
import id.sch.smktelkom_mlg.afinal.xirpl2013234.helpmeapp.Model.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Umar on 20/04/2018.
 */

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAW4QwFDQ:APA91bEIyYIf6nDF6MaQ4LOE4Ne7q_copcTfoxghYCqwJzPuhRxC0mBdpj1SzI6Zkc53E_LGpf9srtDNdVuw6eWBlpXPC0Bz9qU-Bl4bl4WHrtWcy_dGA1qImrx1_4NBVh_JZN3HHJm5"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}

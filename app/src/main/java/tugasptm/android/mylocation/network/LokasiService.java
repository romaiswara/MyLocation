package tugasptm.android.mylocation.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import tugasptm.android.mylocation.model.response.GeneralResponse;
import tugasptm.android.mylocation.model.response.LocationResponse;

public interface LokasiService {

    @GET("read.php")
    Call<LocationResponse> read();

    @FormUrlEncoded
    @POST("readdetail.php")
    Call<LocationResponse> read(int id);

    @FormUrlEncoded
    @POST("insert.php")
    Call<GeneralResponse> insert(
            @Field("nama") String nama,
            @Field("kota") String kota,
            @Field("lat") double lat,
            @Field("lng") double lng
    );


    @FormUrlEncoded
    @POST("delete.php")
    Call<GeneralResponse> delete(@Field("id") int id);

}

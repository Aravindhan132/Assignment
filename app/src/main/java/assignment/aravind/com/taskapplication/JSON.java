package assignment.aravind.com.taskapplication;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by aravindhan Software on 07/17/18
 */

public interface JSON {

    @Streaming
    @GET
    Observable<ResponseBody> getFile(@Url String url);

}

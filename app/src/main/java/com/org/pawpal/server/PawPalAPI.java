package com.org.pawpal.server;

import com.org.pawpal.model.AddFavoriteResponse;
import com.org.pawpal.model.FavoriteMessageResponse;
import com.org.pawpal.model.FavoriteResponse;
import com.org.pawpal.model.FilterPal;
import com.org.pawpal.model.GetArchieveMessageResponse;
import com.org.pawpal.model.GetInboxMessageResponse;
import com.org.pawpal.model.GetLatestConversationResponse;
import com.org.pawpal.model.GetSentMessageResponse;
import com.org.pawpal.model.GetThreadMessageResponse;
import com.org.pawpal.model.Login;
import com.org.pawpal.model.PalActivitiyResponse;
import com.org.pawpal.model.PostMessage;
import com.org.pawpal.model.PostProfile;
import com.org.pawpal.model.Profile;
import com.org.pawpal.model.Register;
import com.org.pawpal.model.SearchPalResponse;
import com.org.pawpal.model.SendMessageResponse;
import com.org.pawpal.model.UpdateProfile;
import com.org.pawpal.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hp-pc on 24-11-2016.
 */

public interface PawPalAPI {

    @POST("auth/login.json")
    Call<User> loginUser(@Body Login user);

    @POST("auth/register.json")
    Call<User> registerUser(@Body Register user);

    @GET("profile/get_user_profile.json")
    Observable<Profile> getProfile(@Query("profile_id") String profile_id);

    @GET("profile/get_user_profile.json")
    Observable<Profile> getOtherProfile(@Query("profile_id") String profile_id, @Query("other_user_profile_id") String other_user_profile_id);

    @GET("profile/get_activities.json")
    Observable<PalActivitiyResponse> getActivities();

    @FormUrlEncoded
    @POST("user/add_pal_to_favorites.json")
    Observable<AddFavoriteResponse> addFavorite(@Field("profile_id") String profile_id, @Field("favorite_profile_id") String favorite_profile_id);

    @GET("user/get_favorite_pals.json")
    Observable<FavoriteResponse> getFavorites(@Query("profile_id") String profile_id);

    @POST("profile/searchProfile.json")
    /*Observable<SearchPalResponse> searchPal(@Field("profile_id") String profileId, @Field("pet_size") String size,
                                            @Field("lat") String lat,@Field("lng") String lng);*/
    Observable<SearchPalResponse> searchPal(@Body FilterPal filterPal);

    @POST("profile/edit_profile.json")
    Observable<UpdateProfile> saveProfile(@Body PostProfile postProfile);

    @POST("message/send_message.json")
    Observable<SendMessageResponse> sendMessage(@Body PostMessage postMessage);

    @GET("message/get_user_inbox_messages.json")
    Observable<GetInboxMessageResponse> getInboxMessages(@Query("profile_id") String profile_id);

    @GET("message/get_user_sent_messages.json")
    Observable<GetSentMessageResponse> getSentMessages(@Query("profile_id") String profile_id);

    @GET("message/get_thread_detail.json")
    Observable<GetThreadMessageResponse> getThreadMessages(@Query("profile_id") String profile_id,@Query("thread_id") String thread_id);

    @FormUrlEncoded
    @POST("message/archive_message.json")
    Observable<GetArchieveMessageResponse> postArchieveMessage(@Field("profile_id") String profile_id, @Field("thread_id") String thread_id, @Field("is_archive") int is_archive);

    @FormUrlEncoded
    @POST("message/star_message.json")
    Observable<FavoriteMessageResponse> postFavoriteMessage(@Field("profile_id") String profile_id, @Field("thread_id") String thread_id, @Field("isFav") int is_fav);

    @GET("message/get_user_archive_messages.json")
    Observable<GetArchieveMessageResponse> getArchieveMessages(@Query("profile_id") String profile_id);

    @GET("message/get_user_latest_conversation.json")
    Observable<GetLatestConversationResponse> getLatestConversation(@Query("profile_id") String profile_id);

}

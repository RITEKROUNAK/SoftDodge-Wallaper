package com.ritek.freshwalls.api;

import com.ritek.freshwalls.config.Config;
import com.ritek.freshwalls.entity.ApiResponse;
import com.ritek.freshwalls.entity.Box;
import com.ritek.freshwalls.entity.Category;
import com.ritek.freshwalls.entity.Color;
import com.ritek.freshwalls.entity.Comment;
import com.ritek.freshwalls.entity.Pack;
import com.ritek.freshwalls.entity.Slide;
import com.ritek.freshwalls.entity.Tag;
import com.ritek.freshwalls.entity.User;
import com.ritek.freshwalls.entity.Wallpaper;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface apiRest {
    @FormUrlEncoded
    @POST("user/register/"+ Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> register(@Field("name") String name, @Field("username") String username, @Field("password") String password, @Field("type") String type, @Field("image") String image);



    @Multipart
    @POST("user/edit/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> editUser(@Part MultipartBody.Part file,@Part("user") Integer user,@Part("key") String key,@Part("name") String name,@Part("email") String email,@Part("facebook") String facebook,@Part("twitter") String twitter,@Part("instagram") String instagram);


    @GET("version/check/{code}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> check(@Path("code") Integer code);

    @GET("device/{tkn}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addDevice(@Path("tkn")  String tkn);

    @GET("wallpaper/all/{order}/{page}/"+ Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Wallpaper>> wallpapersAll(@Path("order") String order, @Path("page") Integer page);

    @GET("wallpaper/related/{id}/"+ Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Wallpaper>> wallpapersRelated(@Path("id") Integer id);


    @GET("wallpaper/category/{page}/{category}/"+ Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Wallpaper>> wallpapersByCategory(@Path("page") Integer page, @Path("category") Integer category);

    @GET("wallpaper/pack/{page}/{pack}/"+ Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Wallpaper>> wallpapersByPack(@Path("page") Integer page, @Path("pack") Integer pack);


    @GET("wallpaper/color/{page}/{color}/"+ Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Wallpaper>> wallpapersByColor(@Path("page") Integer page, @Path("color") Integer category);


    @GET("wallpaper/get/{id}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<Wallpaper> wallpapersById(@Path("id")  Integer id);

    @GET("wallpaper/query/{page}/{query}/"+ Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Wallpaper>> wallpapersBysearch(@Path("page") Integer page, @Path("query") String query);

    @GET("wallpaper/user/{page}/{user}/"+ Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Wallpaper>> wallpapersByUser(@Path("page") Integer page, @Path("user") Integer user);


    @GET("wallpaper/me/{page}/{user}/"+ Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Wallpaper>> wallpaperssByMe(@Path("page") Integer page, @Path("user") Integer user);

    @GET("slide/all/"+ Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Slide>> slideAll();

    @GET("category/list/{id}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Category>> categoryList(@Path("id") Integer id);

    @GET("category/all/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Category>> categoryAll();


    @GET("category/popular/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Category>> categoriesPopular();


    @GET("rate/add/{user}/{wallpapers}/{value}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addRate(@Path("user")  String user,@Path("wallpapers") Integer wallpapers,@Path("value") float value);

    @GET("rate/get/{user}/{wallpapers}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> getRate(@Path("user")  String user,@Path("wallpapers") Integer wallpapers);

    @FormUrlEncoded
    @POST("report/add/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addReport(@Field("wallpapers") Integer wallpapers, @Field("message") String message);



    @FormUrlEncoded
    @POST("support/add/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addSupport(@Field("email") String email, @Field("name") String name , @Field("message") String message);

    @GET("install/add/{id}/"+ Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addInstall(@Path("id") String id);


    @Multipart
    @POST("wallpaper/upload/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> uploadRingtone(@Part MultipartBody.Part file, @Part("duration") long duration, @Part("id") String id, @Part("key") String key, @Part("title") String title,@Part("description") String description,  @Part("categories") String categories);

    @GET("user/get/{user}/{me}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> getUser(@Path("user") Integer user,@Path("me") Integer me);


    @FormUrlEncoded
    @POST("user/token/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> editToken(@Field("user") Integer user, @Field("key") String key, @Field("token_f") String token_f, @Field("name") String name);

    @GET("category/by/{id}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Category>> CategoriesBy(@Path("id") Integer id);



    @GET("user/follow/{user}/{follower}/{key}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> follow(@Path("user") Integer user,@Path("follower") Integer follower,@Path("key") String key);


    @GET("user/followers/{user}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<User>> getFollowers(@Path("user") Integer user);

    @GET("user/followings/{user}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<User>> getFollowing(@Path("user") Integer user);

    @GET("tags/all/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Tag>> TagList();


    @GET("user/followingstop/{user}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<User>> getFollowingTop(@Path("user") Integer user);


    @GET("wallpaper/by/follow/{page}/{user}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Wallpaper>> followRingtone(@Path("page") Integer page, @Path("user") Integer user);

    @GET("wallpaper/random/{page}/"+Config.TOKEN_APP+"/"+Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Wallpaper>> wallpapersRandom(@Path("page") Integer page);

    @FormUrlEncoded
    @POST("comment/add/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> addComment(@Field("user")  String user,@Field("id") Integer id,@Field("comment") String comment);

    @GET("comment/list/{id}/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Comment>> getComments(@Path("id") Integer id);


    @GET("color/by/{id}/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Color>> getColorsByWallpaper(@Path("id") Integer id);

    @GET("color/all/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Color>> ColorsList();

    @GET("pack/all/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<List<Pack>> PacksList();

    @GET("user/search/{query}/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<List<User>> searchUsers(@Path("query") String query);

    @GET("first/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<Box> loadData();

    @Multipart
    @POST("video/upload/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> uploadVideo(@Part MultipartBody.Part file,@Part MultipartBody.Part file_thum,@Part("id") String id, @Part("key") String key, @Part("title") String title, @Part("description") String description,@Part("colors") String colors,@Part("categories") String categories);

    @Multipart
    @POST("image/upload/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> uploadImage(@Part MultipartBody.Part file,@Part("id") String id, @Part("key") String key, @Part("title") String title, @Part("description") String description,@Part("colors") String colors,@Part("categories") String categories);

    @Multipart
    @POST("gif/upload/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<ApiResponse> uploadGif(@Part MultipartBody.Part file,@Part("id") String id, @Part("key") String key, @Part("title") String title, @Part("description") String description,@Part("colors") String colors,@Part("categories") String categories);

    @FormUrlEncoded
    @POST("wallpaper/add/view/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<Integer> addView(@Field("id") Integer id);

    @FormUrlEncoded
    @POST("wallpaper/add/share/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<Integer> addShare(@Field("id")  Integer id);

    @FormUrlEncoded
    @POST("wallpaper/add/set/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<Integer> addSet(@Field("id")  Integer id);

    @FormUrlEncoded
    @POST("wallpaper/add/download/"+ Config.TOKEN_APP+"/"+ Config.ITEM_PURCHASE_CODE+"/")
    Call<Integer> addDownload(@Field("id")  Integer id);
}


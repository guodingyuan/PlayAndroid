package com.gdy.playandroid.utils.retrofit;

import com.gdy.playandroid.mvp.bean.Article;
import com.gdy.playandroid.mvp.bean.ArticlePage;
import com.gdy.playandroid.mvp.bean.BannerData;
import com.gdy.playandroid.mvp.bean.LoginData;
import com.gdy.playandroid.mvp.bean.Navigation;
import com.gdy.playandroid.mvp.bean.ProjectTree;
import com.gdy.playandroid.mvp.bean.ResponseBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface BaseApiService {

  String Base_URL = "https://www.wanandroid.com/";

    @GET
    Observable<ResponseBody> executeGet(
            @Url String url,
            @QueryMap Map<String, String> maps
    );

    @POST
    @FormUrlEncoded
    Observable<ResponseBody> executePost(
            @Url String url,
            @FieldMap Map<String, String> maps);

    @POST
    Observable<ResponseBody> json(
            @Url String url,
            @Body RequestBody jsonStr);

    @Streaming
    @GET
    Observable<Response<ResponseBody>> downloadFile(@Url String fileUrl);


    /**
     * 登录
     * http://www.wanandroid.com/user/login
     * @param username
     * @param password
     */
    @POST("user/login")
    @FormUrlEncoded
    Observable<ResponseBean<LoginData>> loginByPassword(@Field("username")String username ,@Field("password")String password);


  /**
   * 获取轮播图
   * http://www.wanandroid.com/banner/json
   */
    @GET("banner/json")
    Observable<ResponseBean<List<BannerData>>> requestBannerData();


  /**
   * 获取首页置顶文章列表
   * http://www.wanandroid.com/article/top/json
   */
  @GET("article/top/json")
  Observable<ResponseBean<List<Article>>> getTopArticles();

  /**
   * 获取文章列表
   * http://www.wanandroid.com/article/list/0/json
   * @param pageNum
   */
  @GET("article/list/{pageNum}/json")
  Observable<ResponseBean<ArticlePage>> getArticles(@Path("pageNum")int pageNum);


  /**
   * 导航数据
   * http://www.wanandroid.com/navi/json
   */
  @GET("navi/json")
  Observable<ResponseBean<List<Navigation>>> getNavigationList();

  /**
   * 项目数据
   * http://www.wanandroid.com/project/tree/json
   */
  @GET("project/tree/json")
  Observable<ResponseBean<List<ProjectTree>>> getProjectTree();

  /**
   * 项目列表数据
   * http://www.wanandroid.com/project/list/1/json?cid=294
   * @param page
   * @param cid
   */
  @GET("project/list/{page}/json")
  Observable<ResponseBean<ArticlePage>> getProjectData(@Path("page") int page, @Query("cid")int cid);
}

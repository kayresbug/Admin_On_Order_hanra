package com.example.admin_on_order;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface InterfaceAPI {

    @FormUrlEncoded
    @POST("login")
    Call<JsonObject> login(@Field("userid") String id, @Field("password") String pw);

    @FormUrlEncoded
    @POST("service")
    Call<JsonObject> service(@Field("storecode") String storeCode, @Field("inputtime") String inputTime);

    @FormUrlEncoded
    @POST("service")
    Call<JsonObject> service2(@Field("storecode") String storeCode, @Field("inputtime") String inputTime);

    @GET("test")
    Call<JsonArray> getListTimeService(@Query("inputtime") String inputTime);

    @FormUrlEncoded
    @POST("sorder")
    Call<JsonObject> order(@Field("storecode") String storeCode, @Field("order_time") String orderTime);

    @FormUrlEncoded
    @POST("update_service")
    Call<JsonObject> serviceUpdateStatus(@Field("storecode") String storeCode, @Field("num") String num);

    @FormUrlEncoded
    @POST("sellpayment")
    Call<JsonObject> sellPayment(@Field("storecode") String storeCode, @Field("order_time") String paymentTime, @Field("order_time2") String paymentTime2);

    @FormUrlEncoded
    @POST("order")
    Call<JsonObject> postOrder(@Field("storecode") String storeCode, @Field("tableno") String tableNo, @Field("menuno") String menuNo, @Field("price") String price,
                               @Field("count") String count, @Field("totprice") String totPrice, @Field("paytype") String payType, @Field("menuname") String menuName, @Field("order_time") String order_time, @Field("auth_num") String autnnum,
                               @Field("auth_date") String authDate, @Field("vantr") String vanTr, @Field("cardbin") String cardBin);
    @FormUrlEncoded
    @POST("payment")
    Call<JsonObject> payment(@Field("storecode") String storeCode, @Field("classification") String classification, @Field("telegramtype") String telegramType, @Field("dptid") String dptId, @Field("enterpriseinfo") String enterpriseInfo, @Field("fulltextnum") String fullTextNum,
                             @Field("status") String status, @Field("authdate") String authDate, @Field("message1") String message1, @Field("message2") String message2,
                             @Field("authnum") String authNum, @Field("franchiseid") String franchiseId, @Field("issuecode") String issueCode, @Field("cardname") String cardName,
                             @Field("purchasecode") String purchaseCode, @Field("purchasename") String purchaseName, @Field("remain") String remain, @Field("point1") String point1,
                             @Field("point2") String point2, @Field("point3") String point3, @Field("notice1") String notice1, @Field("notice2") String notice2, @Field("cardtype") String cardType,
                             @Field("cardno") String cardNo, @Field("swmodelnum") String swModelNum, @Field("readermodelnum") String readerModelNum, @Field("vantr") String vanTr, @Field("cardbin") String cardBin, @Field("price") String price);
}

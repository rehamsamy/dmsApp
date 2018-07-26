package com.dmsegypt.dms.rest;

import com.dmsegypt.dms.rest.model.AfterSales;
import com.dmsegypt.dms.rest.model.CardIdsList;
import com.dmsegypt.dms.rest.model.Message;
import com.dmsegypt.dms.rest.model.Response.AgentResponse;
import com.dmsegypt.dms.rest.model.Response.ContractDetailResponse;
import com.dmsegypt.dms.rest.model.Response.ContractResponse;
import com.dmsegypt.dms.rest.model.Response.GetPharmaciesReponse;
import com.dmsegypt.dms.rest.model.Response.GooglePlayReviewsResponse;
import com.dmsegypt.dms.rest.model.Response.MedNetworkResponse;
import com.dmsegypt.dms.rest.model.Response.MedicineOrderResultResponse;
import com.dmsegypt.dms.rest.model.Response.ResponseAfterSales;
import com.dmsegypt.dms.rest.model.Response.ResponseAreas;
import com.dmsegypt.dms.rest.model.Response.ResponseBatchDetail;
import com.dmsegypt.dms.rest.model.Response.ResponseBatchPharmacy;
import com.dmsegypt.dms.rest.model.Response.ResponseBatchSummary;
import com.dmsegypt.dms.rest.model.Response.ResponseChangePassword;
import com.dmsegypt.dms.rest.model.Response.ResponseCities;
import com.dmsegypt.dms.rest.model.Response.ResponseCompany;
import com.dmsegypt.dms.rest.model.Response.ResponseDataStore;
import com.dmsegypt.dms.rest.model.Response.ResponseDoctorSpicific;
import com.dmsegypt.dms.rest.model.Response.ResponseForgetPassword;
import com.dmsegypt.dms.rest.model.Response.ResponseGeneralLookup;
import com.dmsegypt.dms.rest.model.Response.ResponseGetNotifications;
import com.dmsegypt.dms.rest.model.Response.ResponseIndemityRequests;
import com.dmsegypt.dms.rest.model.Response.ResponseItem;
import com.dmsegypt.dms.rest.model.Response.ResponseLogin;
import com.dmsegypt.dms.rest.model.Response.ResponseLookups;
import okhttp3.RequestBody;
import com.dmsegypt.dms.rest.model.Response.ResponseMedicineDetails;
import com.dmsegypt.dms.rest.model.Response.ResponseMembers;
import com.dmsegypt.dms.rest.model.Response.ResponseOrder;
import com.dmsegypt.dms.rest.model.Response.ResponseOtp;
import com.dmsegypt.dms.rest.model.Response.ResponseProviderTypes;
import com.dmsegypt.dms.rest.model.Response.ResponseProviders;
import com.dmsegypt.dms.rest.model.Response.ResponseProvidersSimple;
import com.dmsegypt.dms.rest.model.Response.ResponseRegister;
import com.dmsegypt.dms.rest.model.Response.ResponseRelpy;
import com.dmsegypt.dms.rest.model.Response.ResponseRequestType;
import com.dmsegypt.dms.rest.model.Response.ResponseRequests;
import com.dmsegypt.dms.rest.model.Response.ResponseRunner;
import com.dmsegypt.dms.rest.model.Response.ResponseStatus;
import com.dmsegypt.dms.rest.model.Response.ResponseUpdateProfile;
import com.dmsegypt.dms.rest.model.Response.ResponseUpdateProfileImage;
import com.dmsegypt.dms.rest.model.Response.ResponseUserAccount;
import com.dmsegypt.dms.rest.model.Response.ResponseUserToken;
import com.dmsegypt.dms.rest.model.Response.ReviewReplyResponse;
import com.dmsegypt.dms.rest.model.Response.SalesCallResposne;
import com.dmsegypt.dms.rest.model.Response.SalesReplyResponse;
import com.dmsegypt.dms.rest.model.Response.StatusResponse;
import com.dmsegypt.dms.rest.model.Response.SubServContractResponse;
import com.dmsegypt.dms.rest.model.ReviewReply;
import com.dmsegypt.dms.rest.model.SalesCall;
import com.dmsegypt.dms.rest.model.Response.RefreashTokenResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mohamed Abdallah on 19/02/2017.
 **/

public interface ApiService {
    //Login/name/password/ar/token
    @POST("Login/{username}/{password}/{language}/{token}")
    Call<ResponseLogin> login(@Path("username") String username,
                              @Path("password") String password,
                              @Path("language") String language,
                              @Path("token") String token);



    //register/michael/michael/adliy/nagib/500114-5-4776-1/h/hgvgv/m@m.com/123/en/android/samsung/BD
    @POST("register/{username}/{fullname}/{cardId}/{nationalId}/{mobile}/{email}/{password}/{language}/{OS}/{device}/{birthDate}")
    Call<ResponseRegister> register(@Path("username") String username,
                                    @Path("fullname") String fullname,
                                    @Path("cardId") String cardId,
                                    @Path("nationalId") String nationalId,
                                    @Path("mobile") String mobile,
                                    @Path("email") String email,
                                    @Path("password") String password,
                                    @Path("language") String language,
                                    @Path("OS") String OS,
                                    @Path("device") String device,
                                    @Path("birthDate") String birthDate);



    @POST("changeEmployeeCard/{card_id}/{new_card}/{language}/{updateby}")
    Call<ResponseItem> changeEmployeeCard(@Path("card_id") String card_id,
                                          @Path("new_card") String new_card,
                                          @Path("language") String language,
                                          @Path("updateby") String updateby);



    @POST("reopenEmployeeCard/{card_id}/{reopen_date}/{language}/{updateby}")
    Call<ResponseItem> reopenEmployeeCard(@Path("card_id") String card_id,
                                            @Path("reopen_date") String reopen_date,
                                            @Path("language") String language,
                                            @Path("updateby") String updateby);

    @POST("getAllRunners/{language}/{search}/{pagingStartIndex}")
    Call<ResponseRunner> getAllRunner(@Path("language") String language,@Path("search") String search,@Path("pagingStartIndex") String pagingStartIndex);

    @POST("getStoreData/{packagename}")
    Call<ResponseDataStore> getStoreData(@Path("packagename") String packagename);



    @POST("getAllMassengerOrder/{username}/{language}/{search}/{pagingStartIndex}")
    Call<ResponseOrder> getAllMassengerOrder(@Path("username") String username,@Path("language") String language, @Path("search") String search,@Path("pagingStartIndex") String pagingStartIndex);


    @POST("getcustomerservice/{username}/{language}")
    Call<ResponseItem> getcustomerservice(@Path("username") String username,@Path("language") String language);

    //updateProfile/cardId/email/mobile/en
    @POST("updateProfile/{cardId}/{email}/{mobile}/{language}")
    Call<ResponseUpdateProfile> updateProfile(@Path("cardId") String cardId,
                                              @Path("email") String email,
                                              @Path("mobile") String mobileNumber,
                                              @Path("language") String language);


    //profile image
    //updateProfile/cardId/base64Image
    @Multipart
    @POST("api/updateProfileImage/")
    Call<ResponseUpdateProfileImage> updateProfileImage(@Part("id") RequestBody cardId, @Part("folder") RequestBody folder, @Part("language")RequestBody language,
                                                        @Part MultipartBody.Part part);





    //chronic
    @Multipart
    @POST("api/updateProfileImage/")
    Call<ResponseUpdateProfileImage> updateProfileImage(@Part("id") RequestBody cardID, @Part("folder") RequestBody folder_type, @Part("language")RequestBody LANGUAGE, @Part("username")RequestBody username,
                                                        @Part MultipartBody.Part part);

    //reply
    @Multipart
    @POST("api/updateProfileImage/")
    Call<ResponseUpdateProfileImage> updateProfileImage( @Part("folder") RequestBody folder,@Part("reply_notes") RequestBody reason,@Part("reply_flg") RequestBody reply_flag,@Part("id") RequestBody req_id,@Part("language")RequestBody language,
                                                         @Part MultipartBody.Part part);


    //add Request
    @Multipart
    @POST("api/updateProfileImage/")
    Call<ResponseUpdateProfileImage> updateProfileImage(@Part("id") RequestBody cardId, @Part("folder") RequestBody folder,@Part("title") RequestBody title,@Part("description") RequestBody description,@Part("username") RequestBody username,@Part("language") RequestBody language,@Part("type") RequestBody type,
                                                        @Part MultipartBody.Part part);

    // print
    @Multipart
    @POST("api/updateProfileImage/")
    Call<ResponseUpdateProfileImage> updateProfileImagePrint(@Part("id") RequestBody cardId,@Part("folder") RequestBody folder, @Part("createdby") RequestBody createdby,@Part("reason") RequestBody reason,@Part("language") RequestBody language,
                                                             @Part MultipartBody.Part part);


    // indimnty
    @Multipart
    @POST("api/updateProfileImage/")
    Call<ResponseUpdateProfileImage> updateIndemnityImage(@Part("id") RequestBody cardId,@Part("folder") RequestBody folder, @Part("subFolder") RequestBody subFolder,@Part("language") RequestBody language,
                                                             @Part MultipartBody.Part part);



    //rosheta
    @Multipart
    @POST("api/updateProfileImage/")
    Call<ResponseUpdateProfileImage> updateRoshetaImage(@Part("id") RequestBody cardId,
                                                        @Part MultipartBody.Part part,
                                                        @Part("folder") RequestBody folder,
                                                        @Part("language") RequestBody language,
                                                        @Part("username") RequestBody username,
                                                        @Part("title") RequestBody title,
                                                        @Part("type") RequestBody type,
                                                        @Part("description") RequestBody description,
                                                        @Part("subFolder") RequestBody subFolder,
                                                        @Part("prv_code") RequestBody prvCode,
                                                        @Part("branch_code") RequestBody branchCode,
                                                        @Part("notes") RequestBody notes);

//updateeeeeeeeeeeeeeeeeeeeeeeeeeeeeed


    //addrequest/description/type/cardId,title/en
    @POST("addrequest/{description}/{type}/{cardId}/{title}/{language}")
    Call<StatusResponse>addrequest(@Path("description") String description,
                                   @Path("type") String type,
                                   @Path("cardId") String cardId,
                                   @Path("title") String title,
                                   @Path("language") String language
    );


    @POST("addMassengerOrder/{orderdate}/{ordertype}/{ordernotes}/{compid}/{compname}/{compaddress}/{compphone1}/{compphone2}/{compperson}/{compgover}/{comparea}/{compcc}/{createdby}/{language}/{reasonsId}/{vip}")
    Call<StatusResponse>addMassengerOrder(@Path("orderdate") String orderdate,
                                          @Path("ordertype") String  ordertype,
                                          @Path("ordernotes") String ordernotes,
                                          @Path("compid") String compid,
                                          @Path("compname") String compname,
                                          @Path("compaddress") String compaddress,
                                          @Path("compphone1") String compphone1,
                                          @Path("compphone2") String compphone2,
                                          @Path("compperson") String compperson,
                                          @Path("compgover") String compgover,
                                          @Path("comparea") String comparea,
                                          @Path("compcc") String compcc,
                                          @Path("createdby") String createdby,
                                          @Path("language") String language,
                                          @Path("reasonsId") String reasonsId,
                                          @Path("vip") String vip);

    @POST("ConfirmMassengerOrder/{order_id}/{run_id}/{notes}/{rate}/{updatedBy}/{language}")
    Call<StatusResponse> ConfirmMassengerOrder(@Path("order_id") String order_id,
                                               @Path("run_id") String run_id,
                                               @Path("notes") String notes,
                                               @Path("rate") String rate,
                                               @Path("updatedBy") String updatedBy,
                                               @Path("language") String language);

    @POST("ApproveMassengerOrder/{order_id}/{runner_id}/{time}/{updatedBy}/{language}")
    Call<StatusResponse>ApproveMassengerOrder(@Path("order_id") String order_id,
                                              @Path("runner_id") String runner_id,
                                              @Path("time") String time,
                                              @Path("updatedBy") String updatedBy,
                                              @Path("language") String language);

    @POST("getCompanies/{hrName}/{search}/{language}/{rowIndex}")
    Call<ResponseCompany> getCompanies(@Path("hrName") String hrName,@Path("language") String language, @Path("search") String search, @Path("rowIndex") String rowIndex);



    @POST("getReasonsList/{language}")
    Call<ResponseItem> getReasonsList(@Path("language") String language);

    @POST("AddRunner/{a_name}/{e_name}/{birth_date}/{national_id}/{address}/{own}/{licence}/{type}/{created_by}/{language}/{username}/{password}")
    Call<StatusResponse> AddRunner(@Path("a_name") String a_name,@Path("e_name") String e_name,@Path("birth_date") String birth_date,
                                   @Path("national_id") String national_id,@Path("address") String address,@Path("own") String own,@Path("licence") String licence,
                                   @Path("type") String type,@Path("created_by") String created_by,@Path("language") String language,@Path("username") String username,@Path("password") String password);


    @POST("HoldMassengerOrder/{order_id}/{hold_time}/{updatedBy}/{language}")
    Call<StatusResponse>HoldMassengerOrder(@Path("order_id") String order_id,@Path("hold_time") String hold_time,
                                           @Path("updatedBy") String updatedBy,@Path("language") String language);



    @POST("indvidualApproval/{language}/{title}/{description}/{cardId}/{ename}/{imagepath}")
    Call<StatusResponse> indvidualApproval(@Path("language") String language,
                                           @Path("title") String title,
                                           @Path("description")String description,
                                           @Path("cardId") String cardId,
                                           @Path("ename") String ename,
                                           @Path("imagepath") String imagepath);


    //forgetPassword/10484-3-246-1/12345
    @POST("forgetPassword/{cardId}/{language}")
    Call<ResponseForgetPassword> forgetPassword(@Path("cardId") String cardId,
                                                @Path("language") String language);

    //changepassword/500113-3-1029-5/old/new_image/en
    @POST("changePassword/{cardId}/{oldPassword}/{newPassword}/{language}")
    Call<ResponseChangePassword> changePassword(@Path("cardId") String cardId,
                                                @Path("oldPassword") String oldPassword,
                                                @Path("newPassword") String newPassword,
                                                @Path("language") String language);

    //verifyMobile/10484-3-246-1/12345
    @POST("verifyMobile/{cardId}/{mobileNumber}")
    Call<ResponseOtp> postOtp(@Path("cardId") String cardId,
                              @Path("mobileNumber") String mobileNumber);

    //updateToken/userId/token
    @POST("updateToken/{userId}/{token}")
    Call<ResponseUserToken> updateToken(@Path("userId") String userId,
                                        @Path("token") String token);

    //GetProviderTypes/ar
    @POST("GetProviderTypes/{language}/{type}")
    Call<ResponseProviderTypes> getProviderTypes(@Path("language") String language, @Path("type") int type);

    //GetDoctorSpecific
    @POST("GetDoctorSpecific/{language}/{type}")
    Call<ResponseDoctorSpicific> getDoctorSpecific(@Path("language") String language, @Path("type") int type);

    //getProviders/city/area/classNumber/type/en
    @POST("getProviders/{city}/{classNumber}/{type}/{language}/{pagingStartIndex}/{text}/{areaId}/{screenType}/{specificId}")
    Call<ResponseProviders> getProviders(@Path("city") String city,
                                         @Path("classNumber") String classNumber,
                                         @Path("type") String type,
                                         @Path("language") String language,
                                         @Path("pagingStartIndex") String pagingStartIndex,
                                         @Path("text") String text,
                                         @Path("areaId") String areaId,
                                         @Path("screenType") int screenType,
                                         @Path("specificId") String specificId);

    //getProviders/city/area/classNumber/type/en
    @POST("getProviders/{city}/{classNumber}/{type}/{language}/{pagingStartIndex}/{text}/{areaId}/{screenType}/{specificId}")
    Call<ResponseProvidersSimple> getSimpleProviders(@Path("city") String city,
                                                     @Path("classNumber") String classNumber,
                                                     @Path("type") String type,
                                                     @Path("language") String language,
                                                     @Path("pagingStartIndex") String pagingStartIndex,
                                                     @Path("text") String text,
                                                     @Path("areaId") String areaId,
                                                     @Path("screenType") int screenType,
                                                     @Path("specificId") String specificId);



    @POST("searchByCardId/{cardid}/{username}/{password}/{language}")
    Call<ResponseMembers> searchByCardId(@Path("cardid") String cardId, @Path("username") String username,@Path("password") String password,@Path("language")String language);


    //getCities/en
    @POST("getCities/{language}/{type}")
    Call<ResponseCities> getCities(@Path("language") String language, @Path("type") int type);


    //getRequestType/en
    @POST("getRequestType/{language}/{type}")
    Call<ResponseRequestType> getRequestType(@Path("language") String language,@Path("type") int type);

    @POST("getChronicMedication/{cardId}/{language}")
    Call<ResponseMedicineDetails> getChronicMedication(@Path("cardId") String cardId,@Path("language") String language);

    @POST("addReply/{reply_notes}/{reply_flg}/{req_id}")
    Call<StatusResponse>addReply(
            @Path("reply_notes") String notes,
            @Path("reply_flg") String reply_flg,
            @Path("req_id") String req_id);

    //getAllRequests/en/1
    @POST("getAllRequests/{language}/{type}/{pagingStartIndex}")
    Call<ResponseRequests> getAllRequests(@Path("language") String language,@Path("type") int type,@Path("pagingStartIndex") String pagingStartIndex);

    @POST("getUserRequestsReply/{language}/{cardId}/{pagingStartIndex}")
    Call<ResponseRequests> getUserRequests(@Path("language") String language,@Path("cardId") String CardId ,@Path("pagingStartIndex") String pagingStartIndex);

    @POST("getUserReply/{language}/{req_id}")
    Call<ResponseRelpy> getUserReply(@Path("language") String language, @Path("req_id") String req_id);



    @POST("getAllIndvidualRequests/{language}/{search}/{pagingStartIndex}")
    Call<ResponseIndemityRequests> getAllIndvidualRequests(@Path("language") String language,@Path("search") String search ,@Path("pagingStartIndex") String pagingStartIndex);


    @POST("getUserIndvidualRequests/{language}/{cardId}/{pagingStartIndex}")
    Call<ResponseIndemityRequests> getUserIndvidualRequests(@Path("language") String language,@Path("cardId") String cardId,@Path("pagingStartIndex") String pagingStartIndex);



    //getareas/cityId/en
    @POST("getareas/{cityId}/{language}/{type}")
    Call<ResponseAreas> getAreas(@Path("cityId") String cityId,
                                 @Path("language") String language, @Path("type") int type);

    //getNotifications/cardId/en
    @POST("getNotifications/{cardId}/{language}")
    Call<ResponseGetNotifications> getNotifications(@Path("cardId") String cardId,
                                                    @Path("language") String language);


    @POST("getClassNameList/{username}/{language}")
    Call<ResponseItem> getClassNameList(@Path("username") String username,@Path("language") String language);


    @POST("addEmployeeRequest/{id}/{name}/{national_id}/{birth_date}/{gender}/{relation}/{branch}/{email}/{address}/{mobile}/{card_id}/{type}/{register_type}/{language}/{createdBy}/{arbname}")
    Call<StatusResponse> addEmployeeRequest(@Path("id") String id,
                                            @Path("name") String name,
                                            @Path("national_id") String national_id,
                                            @Path("birth_date") String birth_date,
                                            @Path("gender") String gender,
                                            @Path("relation") String relation,
                                            @Path("branch") String branch,
                                            @Path("email") String email,
                                            @Path("address") String address,
                                            @Path("mobile") String mobile,
                                            @Path("card_id") String card_id,
                                            @Path("type") String type,
                                            @Path("register_type") String register_type,
                                            @Path("language") String language,
                                            @Path("createdBy") String createdBy,
                                            @Path("arbname") String arbname);




    //lookups/en
    @POST("lookups/{language}")
    Call<ResponseLookups> getLookups(@Path("language") String language);


    @POST("updateEmployeeRequest/{card_id}/{emp_class}/{class_reason}/{language}/{updateby}")
    Call<ResponseItem>updateEmployeeRequest(@Path("card_id") String card_id,
                                            @Path("emp_class") String emp_class,
                                            @Path("class_reason") String class_reason,
                                            @Path("language") String language,
                                            @Path("updateby") String updateby);

    @POST("getBatchDetails/{language}/{pharmacycode}/{batchstate}/{batchnumber}/{card_id}/{approval_no}/{medicine_code}/{rowIndex}")
    Call<ResponseBatchDetail>getBatchDetails(@Path("language") String language,
                                             @Path("pharmacycode") String pharmacycode,
                                             @Path("batchstate") String batchstate,
                                             @Path("batchnumber") String batchnumber,
                                             @Path("card_id") String card_id,
                                             @Path("approval_no") String approval_no,
                                             @Path("medicine_code") String medicine_code,
                                             @Path("rowIndex") String rowIndex);


    @POST("getBatchSummary/{language}/{pharmacycode}/{batchstate}/{batchnumber}/{rowIndex}")
     Call<ResponseBatchSummary>getBatchSummary(
            @Path("language") String language,
            @Path("pharmacycode") String pharmacycode,
            @Path("batchstate") String batchstate,
            @Path("batchnumber") String batchnumber,
            @Path("rowIndex") String rowIndex
                               );


    @POST("getBatchPharmcy/{language}/{pharmacyname}/{rowIndex}")
    Call<ResponseBatchPharmacy> getBatchPharmcy(@Path("language") String language,
                                                @Path("pharmacyname") String pharmacyname,
                                                @Path("rowIndex") String rowIndex);

    @POST("ServiceDepartments/{language}")
    Call<ResponseGeneralLookup> getServiceDepartments(@Path("language") String language);

    @POST("SubmitProviderComplaint/{title}/{details}/{providerId}/{cardId}/{language}")
    Call<ResponseGeneralLookup> submitProviderComplaint(@Path("title") String title,
                                                        @Path("details") String details,
                                                        @Path("providerId") String providerId,
                                                        @Path("cardId") String cardId,
                                                        @Path("language") String language);

    @POST("SubmitServiceComplaint/{title}/{details}/{serviceid}/{cardId}/{language}")
    Call<ResponseGeneralLookup> submitServiceComplaint(@Path("title") String title,
                                                       @Path("details") String details,
                                                       @Path("serviceid") String serviceid,
                                                       @Path("cardId") String cardId,
                                                       @Path("language") String language);

    @POST("getMembers/{cardId}/{language}")
    Call<ResponseMembers> getMembers(@Path("cardId") String cardId, @Path("language") String language);

    @POST("getChroniclastTransaction/{cardId}/{language}")
    Call<StatusResponse> getChroniclastTransaction(@Path("cardId") String cardId, @Path("language") String language);


    @POST("getImagebase54/{Id}/{Type}")
    Call<StatusResponse> getImagebase54(@Path("Id") String Id,@Path("Type") String Type);

    @POST("checkStatus/{cardId}/{language}")
    Call<ResponseStatus> checkStatus(@Path("cardId") String cardId, @Path("language") String language);

    @POST("deleteMember/{cardId}/{memberCardId}/{language}")
    Call<StatusResponse> deleteMembers(@Path("cardId") String cardId, @Path("memberCardId") String memberCardId, @Path("language") String language);

    @POST("addMember/{fullname}/{memberCardId}/{nationalId}/{mobile}/{email}/{language}/{birthDate}/{parentCardId}")
    Call<Message> addMembers(@Path("fullname") String fullname, @Path("memberCardId") String memberCardId,
                             @Path("nationalId") String nationalId,
                             @Path("mobile") String mobile,
                             @Path("email") String email,
                             @Path("birthDate") String birthDate,
                             @Path("parentCardId") String parentCardId,
                             @Path("language") String language);

    @GET("addUserApproval/{cardId}/{firstName}/{secondName}/{lastName}/{manager}/{accountId}/{accountName}/{language}")
    Call<StatusResponse> addUserApproval(@Path("cardId") String cardId,
                                         @Path("firstName") String firstName,
                                         @Path("secondName") String secondName,
                                         @Path("lastName") String lastName,
                                         @Path("manager") String manager,
                                         @Path("accountId") String accountId,
                                         @Path("accountName") String accountName,
                                         @Path("language") String language
    );


    @POST("getCompanyEmployee/{username}/{password}/{itemIndex}/{language}")
    Call<ResponseMembers> getEmployees(@Path("username") String username,
                                       @Path("password") String password,
                                       @Path("itemIndex") int itemIndex,
                                       @Path("language") String language);

    @POST("changestatus/{cardid}/{status}/{language}")
    Call<StatusResponse> changeEmployeeStatus(@Path("cardid") String cardid,
                                              @Path("status") String status,
                                              @Path("language") String language);


    @POST("changeEmployeename/{card_id}/{e_name}/{a_name}/{updateby}/{language}")
    Call<ResponseItem> changeEmployeeName(@Path("card_id") String cardid,
                                            @Path("e_name") String e_name,
                                            @Path("a_name") String a_name,
                                              @Path("updateby") String updateby,
                                              @Path("language") String language);





    @POST("addEmployee/{fullname}/{nationalId}/{mobile}/{email}/{language}/{birthDate}")
    Call<StatusResponse> addEmployee(@Path("fullname") String fullname,
                                     @Path("nationalId") String nationalId,
                                     @Path("mobile") String mobile,
                                     @Path("email") String email,
                                     @Path("birthDate") String birthDate,
                                     @Path("language") String language);





    @POST("getMedEmployee/{username}/{rowindex}/{pagesize}/{language}")
    Call<ResponseMembers> getMedEmployee(@Path("username") String username,
                                         @Path("rowindex") String indexrow,
                                         @Path("pagesize") String pagesize,
                                         @Path("language") String language);


    @POST("addNewProvider/{area_name}/{state_name}/{proivder_type}/{provider_name}/{provider_address}/{contact_name}/{contact_phone}/{updateby}/{language}/{numberofpeople}")
    Call<ResponseItem> addNewProvider(@Path("area_name") String areaname,
                                      @Path("state_name") String state_name,
                                      @Path("proivder_type") String provider_type,
                                      @Path("provider_name") String provider_name,
                                      @Path("provider_address") String provider_address,
                                      @Path("contact_name") String contact_name,
                                      @Path("contact_phone") String contact_phone,
                                      @Path("updateby") String updateby,
                                      @Path("language") String lang,
                                      @Path("numberofpeople") String pepole_num);

    @POST("getBatchPharmcy/{language}/{queryString}/{rowIndex}")
    Call<ResponseBatchPharmacy> getBathcPharmacy(@Path("language")String language, @Path("queryString")String queryString, @Path("rowIndex")String rowIndex);

    @POST("loginAdmin/{username}/{password}")
    Call<StatusResponse>loginAdmin(@Path("username")String username, @Path("password")String password);
    @POST("filterUserAccounts/{type}/{page}/{pagesize}/{language}")
    Call<ResponseUserAccount>getUserAccounts(@Path("type")String query, @Path("page")String page,@Path("pagesize")String pagesize,@Path("language")String language);
    @POST("activateUserAccount/{cardId}/{newState}/{language}")
    Call<StatusResponse>activateUserAccount(@Path("cardId")String cardId, @Path("newState")String newState,@Path("language")String language);


    @POST("addAccount/{id}/{username}/{fullname}/{cardId}/{nationalId}/{mobile}/{email}/{password}/{language}/{OS}/{device}/{birthDate}/{status}/{userType}/{companyId}/{departmentId}/{prv_id}/{prv_type}/{action_type}")
    Call<ResponseRegister>addAccount(@Path("id")String id,@Path("username")String username,@Path("fullname")String fullname,@Path("cardId")String carId,@Path("nationalId")String nationalId,@Path("mobile")String mobile,@Path("email")String email,@Path("password")String password,@Path("language")String lang,@Path("OS")String os,@Path("device")String device,@Path("birthDate")String birthdate
            ,@Path("status")String status,@Path("userType")String usertype,@Path("companyId")String comapnyId,@Path("departmentId")String departmentId,@Path("prv_id") String providerId,@Path("prv_type") String providerType,@Path("action_type") String action_type);


    @POST("sendUserNotifiction/{msg}")
    Call<StatusResponse> sendUserNotification(@Body CardIdsList cardIdsList, @Path(value = "msg",encoded = true)String msg);
    @POST("deleteAccount/{cardid}/{language}")
    Call<StatusResponse>deleteAccount(@Path("cardid")String cardId,@Path("language") String language);

@POST("getCompanySummrayDetail/{comp_id}/{contract_id}/{contract_long}/{contract_type}/{language}")
Call<ContractDetailResponse>getContractDetail(@Path("comp_id")String comp_id,@Path("contract_id")String contract_id,@Path("contract_long")String contract_long
,@Path("contract_type")String contract_type,@Path("language")String language);

    @POST("getCompanycontract/{comp_id}/{language}")
    Call<ContractResponse>getCompanyContract(@Path("comp_id")String comp_id, @Path("language")String language);

@POST("getSubSeviceContract/{comp_id}/{contract_id}/{class_code}/{serv_code}/{language}")
Call<SubServContractResponse>getSubSeviceContract(@Path("comp_id")String comp_id, @Path("contract_id")String contract_id, @Path("class_code")String class_code
        , @Path("serv_code")String serv_code, @Path("language")String language);

    @POST("SearchSalesCall/{SalesId}/{StartDate}/{EndDate}")
    Call<SalesCallResposne>searchSalesCall(@Path("SalesId")String SalesId, @Path("StartDate")String StartDate, @Path("EndDate")String EndDate);
    @POST("FinishSalesCall/{Id}")
    Call<Message>finishSalesCall(@Path("Id")String id);
    @POST("CancelSalesCall/{Id}")
    Call<Message>cancelSalesCall(@Path("Id")String id);
    @POST("AddSalesCall")
    Call<Message>addSalesCall(@Body SalesCall salesCall);
    @POST("UpdateSalesCall")
    Call<Message>updateSalesCall(@Body SalesCall salesCall);
    @POST("getReplyList")
    Call<SalesReplyResponse>getReplyList();
   @POST("getNoneInterestReplyList")
    Call<SalesReplyResponse>getNonInterestReplyList();
    //https://www.googleapis.com/androidpublisher/v2/applications/{packageName}/reviews
    @GET("{packageName}/reviews")
    Call<GooglePlayReviewsResponse>getStoreReviews(@Path("packageName")String packageName,
                                                   @Query("access_token") String token);


    @Multipart
    @POST("https://accounts.google.com/o/oauth2/token")
    Call<RefreashTokenResponse> getRefreashToken(@Part("grant_type") RequestBody grantType,
                                                 @Part("client_id") RequestBody clientId,
                                                 @Part("client_secret") RequestBody clientSecret  ,
                                                 @Part("refresh_token") RequestBody refreashToken  );


    //https://www.googleapis.com/androidpublisher/v2/applications/packageName/reviews/reviewId:reply
    @POST("https://www.googleapis.com/androidpublisher/v2/applications/{packageName}/reviews/{reviewId}:reply")
    Call<ReviewReplyResponse> replyOnReview(@Path("packageName")String packageNam,
                                            @Path("reviewId")String reviewId,
                                            @Query("access_token") String token,
                                            @Body ReviewReply reply);


    @POST("getAccommodationList")
    Call<MedNetworkResponse> getAccommodationList();

    @POST("getMedNetworkList")
    Call<MedNetworkResponse> getMedNetworkList();



    @POST("getProviderList/{query}/{prv_type}/{rowIndex}")
    Call<GetPharmaciesReponse> getPharmacies(@Path("query")String query,
                                             @Path("prv_type")String branch,
                                             @Path("rowIndex")String rowIndex);

    //getUserMedicineOrders/{card_id}/{state}/{rowIndex}
    @POST("getUserMedicineOrders/{card_id}/{state}/{rowIndex}")
    Call<MedicineOrderResultResponse> getUserMedicineOrders(@Path("card_id")String cardId,
                                                            @Path("state")String state,
                                                            @Path("rowIndex")String rowIndex);

    //getPharmMedicineOrders/{prv_id}/{state}/{rowIndex}

    @POST("getPharmMedicineOrders/{prv_id}/{state}/{rowIndex}")
    Call<MedicineOrderResultResponse> getPharmMedicineOrders(@Path("prv_id")String prvId,
                                                             @Path("state")String state,
                                                             @Path("rowIndex")String rowIndex);

    //changeMedicineOrders/{req_id}/{state}
    @POST("changeMedicineOrders/{req_id}/{state}/{reply}")
    Call<Message> changeMedicineOrders(@Path("prv_id")String prvId,
                                       @Path("state")String state,
                                       @Path("reply")String reply);

@POST("checkCardValidation/{CardID}")
    Call<Message>checkCardValdiation(@Path("CardID") String cardId);
    //searchCompany/{query}/{index}
    @POST("searchCompany/{query}/{index}")
    Call<ResponseCompany>searchCompany(@Path("query") String query,@Path("index") String index);


    //getCompanyBranch/{comp_id}
    @POST("getCompanyBranch/{comp_id}")
    Call<ResponseCompany>getCompanyBranch(@Path("comp_id") String compId);


    //getDepAgent/{department}
    @POST("getDepAgent/{department}")
    Call<AgentResponse>getDepAgent(@Path("department") String department);



    ///UpdateAfterSales
    @POST("UpdateAfterSales")
    Call<Message>updateAfterSales(@Body AfterSales afterSales);

    @POST("AddAfterSales")
    Call<Message>addAfterSales(@Body AfterSales afterSales);

    @POST("SearchAfterSales/{SalesId}/{StartDate}/{EndDate}")
    Call<ResponseAfterSales> SearchAfterSales(@Path("SalesId") String searchKey,@Path("StartDate") String fromDate,@Path("EndDate") String toDate);
}



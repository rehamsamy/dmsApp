package com.dmsegypt.dms.app;

import com.dmsegypt.dms.rest.model.AreaItem;
import com.dmsegypt.dms.rest.model.BatchPharmacy;
import com.dmsegypt.dms.rest.model.BatchSummary;
import com.dmsegypt.dms.rest.model.City;
import com.dmsegypt.dms.rest.model.DoctorSpicific;
import com.dmsegypt.dms.rest.model.IndemnityRequest;
import com.dmsegypt.dms.rest.model.Item;
import com.dmsegypt.dms.rest.model.NotificationItem;
import com.dmsegypt.dms.rest.model.Provider;
import com.dmsegypt.dms.rest.model.ProviderSimpleItem;
import com.dmsegypt.dms.rest.model.ProviderType;
import com.dmsegypt.dms.rest.model.Request;
import com.dmsegypt.dms.rest.model.RequestType;
import com.dmsegypt.dms.rest.model.UserPhone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Abdallah on 15/04/2017.
 **/

public class Constants {


    public static final String NODE_MEMBERS ="members" ;
    public static final String NODE_CHATS ="chats" ;
    public static final String NODE_MESSAGES ="messages" ;
    public static final String NODE_USERS ="users" ;
    public static final String USER_TYPE_FAMILY_MEMBER ="4" ;
    public static final String PAGE_SIZE ="20" ;
    public static final String NONE ="-1" ;
    public static final String FIRST_PAGE ="0" ;
    public static final String NOT_ACTIVE ="0" ;
    public static final String ACTIVE ="1" ;
    public static final String STATE_YES ="Y" ;
    public static final String STATE_NO ="N" ;
    public static final String PHARMCY_TYPE ="2" ;

    //
    public class Language{

        public static final String EN = "En";
        public static final String AR = "Ar";

    }


    public static final int KEY_SEARCH_ID = 1;
    public static final int KEY_COMPLAINTS_ID = 1;
    public static final String KEY_COMPLAINTS_TYPE = "key_complaints_type";
    public static final int KEY_COMPLAINTS_PROVIDER = 0;
    public static final int KEY_COMPLAINTS_SERVICE = 1;

/*
    public static final String BASE_IMAGES_URL="http://217.139.89.21/imageUploader/images/";
    public static final String BASE_SERVER_IP="217.139.89.21";
*/
public static final String BASE_IMAGES_URL="dmsapi-001-site1.atempurl.com/uploader/images/";
    public static final String BASE_SERVER_IP="dmsapi-001-site1.atempurl.com";

    public static final String USER_TYPE_NORMAL = "0";
    public static final String USER_TYPE_HR = "1";
    public static final String USER_TYPE_PROVIDER = "2";
    public static final String USER_TYPE_DMS = "3";
    public static final String USER_TYPE_DMS_MEDICAL = "2";
    public static final String KEY_ADD_TYPE = "key_add_type";
    public static final int KEY_TYPE_MEMBER = 0;
    public static final int KEY_TYPE_EMPLOYEE = 1;
    public static final String ACTION_UPDATE_LANGUAGE ="update_lang" ;
    public static final String EXTRA_IMAGE_PROFILE ="extra_image_profile" ;


    public static class UserType{
        public static final String HR="hr";
        public static final String DMS="dms";

    }

    //image types
    public static final String PROFILE_TYPE_PATH="PROFILE_IMAGE";


    public static List<ProviderType> providerTypesList;
    public static List<City> providerCities;
    public static List<AreaItem> providerAreas;
    public static List<Provider> providers;
    public static List<NotificationItem> Notifications;
    public static List<City> dmsServices;
    public static List<ProviderSimpleItem> simpleProviders;


    public static List<IndemnityRequest> IndemnityList;


    public static List<Request> Requests;
    public static List<Request> UserListRequests;

    public static List<DoctorSpicific> doctorSpicificList;




    public static List<RequestType> requestTypeList;
    public static List<Item> CustomerServiceList;

    public static List<BatchPharmacy> PharmacyBatchList;

    public static List<Item> DegreeList;
    public static List<BatchSummary> BatchSummaryList;


    /**
     * System approve
     */
    public static List<UserPhone> providerUserPhone;
    public static List<String> Categories=new ArrayList<String>(){{add("Category");add("ID");}};
    public static List<String> FamilyMember=new ArrayList<String>(){{add("self");add("Second wife");add("Husband/wife");
    add("Daughter");add("Son");add("Grandfather");add("Grandmother");add("Son/Daughter");add("Sister");add("Mother");add("Father");}};
    public static List<String> Gender=new ArrayList<String>(){{add("Male");add("Female");}};
    public static List<String> Langs=new ArrayList<String>(){{add("English");add("العربية");}};
    public static List<String> EditCardPrintOption=new ArrayList<String>(){{add("Select Reason"); add("Replacement of lost"); add("Change photo");}};

    public static List<String> MassengerType=new ArrayList<String>(){{add("Technical Support");add("Customer Service");}};

    public static List<String> MassengerOwn=new ArrayList<String>(){{add("Bike");add("Motorcycle");add("Car");}};

}

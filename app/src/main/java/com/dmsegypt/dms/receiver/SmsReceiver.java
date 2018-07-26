package com.dmsegypt.dms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.dmsegypt.dms.app.Config;
import com.dmsegypt.dms.service.HttpService;

/**
 * Created by Mohamed Abdallah on 19/02/2017.
 **/

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
//        final Bundle bundle = intent.getExtras();
//        try {
            return;
//            if (bundle != null) {
//                Object[] pdusObj = (Object[]) bundle.get("pdus");
//                for (Object aPdusObj : pdusObj) {
//                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
//                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
//                    String message = currentMessage.getDisplayMessageBody();
//
//                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);
//
//                    // if the SMS is not from our gateway, ignore the message
//                    if (!senderAddress.toLowerCase().contains(Config.SMS_ORIGIN.toLowerCase())) {
//                        return;
//                    }
//
//                    // verification code from sms
//                    String verificationCode = getVerificationCode(message);
//
//                    Log.e(TAG, "OTP received: " + verificationCode);
//
//                    Intent hhtpIntent = new_image Intent(context, HttpService.class);
//                    hhtpIntent.putExtra("otp", verificationCode);
//                    context.startService(hhtpIntent);
//                }
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//            Toast.makeText(context , e.getMessage() , Toast.LENGTH_LONG).show();
//        }
    }

    private String getVerificationCode(String message) {
        String code = null;
        try {

            int index = message.indexOf(Config.OTP_DELIMITER);

            if (index != -1) {
                int length = 5;//TODO length of code
                code = message.substring(index+1);
                return code.trim();
            }

        }
        catch (Exception ex) {
        }
        return code;

    }

}

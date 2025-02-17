// http://stackoverflow.com/a/10548680

package com.pbakondy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionSupport;
import android.speech.RecognitionSupportCallback;
import android.speech.RecognizerIntent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

import java.util.List;

public class LanguageDetailsChecker extends BroadcastReceiver {

    private static final String ERROR = "Could not get list of languages";

    private List<String> supportedLanguages;
    private final CallbackContext callbackContext;

    public LanguageDetailsChecker(CallbackContext callbackContext) {
        super();
        this.callbackContext = callbackContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
	public RecognitionSupportCallback getRecognitionSupportCallback() {
        return new RecognitionSupportCallback() {
            @Override
            public void onSupportResult(@NonNull RecognitionSupport recognitionSupport) {
				supportedLanguages = recognitionSupport.getSupportedOnDeviceLanguages();
				JSONArray languages = new JSONArray(supportedLanguages);
				callbackContext.success(languages);
			}

            @Override
            public void onError(int error) {
                // callbackContext.error(ERROR);
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle results = getResultExtras(true);

        if (results.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
            supportedLanguages = results.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES);

            JSONArray languages = new JSONArray(supportedLanguages);
            callbackContext.success(languages);
            return;
        }

        callbackContext.error(ERROR);
    }

    public List<String> getSupportedLanguages() {
        return supportedLanguages;
    }
}

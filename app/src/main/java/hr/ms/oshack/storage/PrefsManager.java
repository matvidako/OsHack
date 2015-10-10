package hr.ms.oshack.storage;

import android.content.Context;
import android.preference.PreferenceManager;

public class PrefsManager {

    private static final String KEY_ONBOARDING_SEEN = "KEY_ONBOARDING_SEEN";

    public static boolean didSeeOnboarding(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_ONBOARDING_SEEN, false);
    }

    public static void onOnboardingSeen(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(KEY_ONBOARDING_SEEN, true).commit();
    }
}

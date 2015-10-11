package hr.ms.oshack.storage;

import android.content.Context;
import android.preference.PreferenceManager;

public class PrefsManager {

    private static final String KEY_ONBOARDING_SEEN = "KEY_ONBOARDING_SEEN";
    private static final String KEY_TRAP_TUTORIAL_SEEN = "KEY_TRAP_TUTORIAL_SEEN";

    public static boolean didSeeOnboarding(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_ONBOARDING_SEEN, false);
    }

    public static void onOnboardingSeen(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(KEY_ONBOARDING_SEEN, true).commit();
    }

    public static boolean didSeeTrapTutorial(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_TRAP_TUTORIAL_SEEN, false);
    }

    public static void onTrapTutorialSeen(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(KEY_TRAP_TUTORIAL_SEEN, true).commit();
    }

}

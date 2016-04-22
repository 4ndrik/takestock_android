package java.com.devabit.takestock;

import android.util.Log;

/**
 * Enables injection of production implementations for
 * {@link } at compile time.
 */
public class Injection {
    public static void injectTest() {
        Log.d("Tag", "inject");
    }
}

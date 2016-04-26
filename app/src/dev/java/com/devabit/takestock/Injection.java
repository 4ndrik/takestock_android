package com.devabit.takestock;

import android.content.Context;
import android.support.annotation.NonNull;
import com.devabit.takestock.data.DataRepository;
import com.devabit.takestock.data.remote.RemoteDataSource;

import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link com.devabit.takestock.data.DataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 *
 * Created by Victor Artemyev on 22/04/2016.
 */

public class Injection {
    public static DataRepository provideDataRepository(@NonNull Context context) {
        checkNotNull(context);
        return DataRepository.getInstance(RemoteDataSource.getInstance(context));
    }
}

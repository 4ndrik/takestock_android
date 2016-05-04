package com.devabit.takestock;

import android.content.Context;
import android.support.annotation.NonNull;
import com.devabit.takestock.data.source.DataRepository;
import com.devabit.takestock.data.source.local.LocalDataSource;
import com.devabit.takestock.data.source.remote.RemoteDataSource;
import com.devabit.takestock.data.source.DataSource;

import static com.devabit.takestock.util.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link DataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 * <p/>
 * Created by Victor Artemyev on 22/04/2016.
 */

public class Injection {
    public static DataRepository provideDataRepository(@NonNull Context context) {
        checkNotNull(context);
        return DataRepository.getInstance(
                RemoteDataSource.getInstance(context),
                LocalDataSource.getInstance(context));
    }
}

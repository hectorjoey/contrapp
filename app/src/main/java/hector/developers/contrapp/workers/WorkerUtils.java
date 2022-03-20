package hector.developers.contrapp.workers;

import androidx.work.Constraints;
import androidx.work.NetworkType;

final public class WorkerUtils {
    private static final String TAG = WorkerUtils.class.getSimpleName();

    //define network constraint for the worker
    public static Constraints networkConstraint(){
         return new Constraints.Builder()
                 .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
    }

}

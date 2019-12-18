package tugasptm.android.mylocation.model.response;

import java.util.List;

import tugasptm.android.mylocation.model.Location;

public class LocationResponse {
    boolean success;
    List<Location> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Location> getResult() {
        return result;
    }

    public void setResult(List<Location> result) {
        this.result = result;
    }
}

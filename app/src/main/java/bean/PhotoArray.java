package bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by android_chen on 2016/3/27.
 */
public class PhotoArray implements Serializable {
    private boolean error;
    private ArrayList<Photo> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<Photo> getResults() {
        return results;
    }

    public void setResults(ArrayList<Photo> results) {
        this.results = results;
    }
}

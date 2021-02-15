package ua.kpi.cosmys.io8209.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Lab2ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Lab2ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Lab 2 coming soon");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
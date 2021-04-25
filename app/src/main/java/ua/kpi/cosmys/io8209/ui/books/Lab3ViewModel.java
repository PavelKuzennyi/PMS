package ua.kpi.cosmys.io8209.ui.books;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Lab3ViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public Lab3ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Lab3");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
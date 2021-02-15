package ua.kpi.cosmys.io8209.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Lab1ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Lab1ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Кузенний Павло\nГрупа ІО-82\nЗК ІО-8209");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
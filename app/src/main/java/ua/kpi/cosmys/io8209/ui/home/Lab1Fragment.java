package ua.kpi.cosmys.io8209.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ua.kpi.cosmys.io8209.R;

public class Lab1Fragment extends Fragment {

    private Lab1ViewModel Lab1ViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Lab1ViewModel =
                new ViewModelProvider(this).get(Lab1ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lab1, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        Lab1ViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
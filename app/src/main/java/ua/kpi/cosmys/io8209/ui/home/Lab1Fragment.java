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

import java.util.ArrayList;
import java.util.HashMap;

import ua.kpi.cosmys.io8209.R;

public class Lab1Fragment extends Fragment {

    private Lab1ViewModel Lab1ViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Contents contents = new Contents();
        System.out.println("Завдання 1");
        HashMap<String, ArrayList<String>> studentsGroups =
                contents.groupStudents(Contents.studentsStr);
        System.out.println();

        System.out.println("Завдання 2");
        int[] points = new int[] {12, 12, 12, 12, 12, 12, 12, 16};
        HashMap<String, HashMap<String, ArrayList<Integer>>> StudentsPoints =
                contents.fillGrades(studentsGroups, points);
        System.out.println();

        System.out.println("Завдання 3");
        HashMap<String, HashMap<String, Integer>> sumPoints = contents.showGradesSum(StudentsPoints);
        System.out.println();

        System.out.println("Завдання 4");
        HashMap<String, Float> groupAvg = contents.showAvgGradesInGroups(sumPoints);
        System.out.println();

        System.out.println("Завдання 5");
        HashMap<String, ArrayList<String>> passedPerGroup = contents.showBestInGroups(sumPoints);
        System.out.println();

        System.out.println("Друга частина:");
        CoordinatePK first = new CoordinatePK();
        CoordinatePK second, third, fourth;

        second = new CoordinatePK(-40, 23, 25, Direction.LATITUDE);
        third = new CoordinatePK(30, 32, 15, Direction.LATITUDE);
        fourth = new CoordinatePK(150, 55, 28, Direction.LONGITUDE);
        System.out.println();

        System.out.println("Перша координата: " + first.getCoordinateA());
        System.out.println("Друга координата: " + second.getCoordinateB());
        System.out.println("Третя координата: " + third.getCoordinateA());
        System.out.println("Четверта координата: " + fourth.getCoordinateB());

        System.out.println();

        System.out.println("Середнє першим способом: " + second.getMeanCoordinate(third).getCoordinateA());
        System.out.println("Середнє другим способом: " + first.getMeanCoordinate(second, third).getCoordinateA());
        System.out.println("Середнє при різному напрямку: " + second.getMeanCoordinate(fourth));

        System.out.println();

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
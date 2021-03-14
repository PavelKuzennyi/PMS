package ua.kpi.cosmys.io8209.ui.home;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class Contents {

// Частина 1

    // Дано рядок у форматі "Student1 - Group1; Student2 - Group2; ..."
    public static String studentsStr = "Дмитренко Олександр - ІП-84; Матвійчук Андрій - ІВ-83; Лесик Сергій - ІО-82; Ткаченко Ярослав - ІВ-83; Аверкова Анастасія - ІО-83; Соловйов Даніїл - ІО-83; Рахуба Вероніка - ІО-81; Кочерук Давид - ІВ-83; Лихацька Юлія- ІВ-82; Головенець Руслан - ІВ-83; Ющенко Андрій - ІО-82; Мінченко Володимир - ІП-83; Мартинюк Назар - ІО-82; Базова Лідія - ІВ-81; Снігурець Олег - ІВ-81; Роман Олександр - ІО-82; Дудка Максим - ІО-81; Кулініч Віталій - ІВ-81; Жуков Михайло - ІП-83; Грабко Михайло - ІВ-81; Іванов Володимир - ІО-81; Востриков Нікіта - ІО-82; Бондаренко Максим - ІВ-83; Скрипченко Володимир - ІВ-82; Кобук Назар - ІО-81; Дровнін Павло - ІВ-83; Тарасенко Юлія - ІО-82; Дрозд Світлана - ІВ-81; Фещенко Кирил - ІО-82; Крамар Віктор - ІО-83; Іванов Дмитро - ІВ-82";

    // Завдання 1
// Заповніть словник, де:
// - ключ – назва групи
// - значення – відсортований масив студентів, які відносяться до відповідної групи
    public HashMap<String, ArrayList<String>> groupStudents(String studentStr) {
        HashMap<String, ArrayList<String>> studentsGroups = new HashMap<>();
        String[] splitStud1 = studentStr.split("; ");
        ArrayList<String> splitStud2 = new ArrayList<>();
        String element;

// Ваш код починається тут
        for (String student : splitStud1) {
            String[] studGroupPair = student.split(" - ");
            if (studGroupPair.length == 2) {
                splitStud2.add(studGroupPair[0]);
                splitStud2.add(studGroupPair[1]);
            }
        }

        for (int i = 0; i < splitStud2.size(); i++) {
            element = splitStud2.get(i);
            if (i % 2 == 1) {
                if (!studentsGroups.containsKey(element))
                    studentsGroups.put(element, new ArrayList<>());

                studentsGroups.get(element).add(splitStud2.get(i - 1));
            }
        }

        for (Map.Entry<String, ArrayList<String>> group: studentsGroups.entrySet()) {
            Collections.sort(group.getValue());
        }

        studentsGroups.entrySet()
                .stream()
                .sorted(Map.Entry.<String, ArrayList<String>>comparingByKey())
                .forEach(System.out::println);

        return studentsGroups;

    }


// Ваш код закінчується тут


// Завдання 2
// Заповніть словник, де:
// - ключ – назва групи
// - значення – словник, де:
//   - ключ – студент, який відносяться до відповідної групи
//   - значення – масив з оцінками студента (заповніть масив випадковими значеннями, використовуючи функцію `randomValue(maxValue: Int) -> Int`)

    private int randValue(int maxValue) {
        Random random = new Random();
        switch (random.nextInt(6)) {
            case 1:
                return (int) Math.ceil((float) maxValue * 0.7);
            case 2:
                return (int) Math.ceil((float) maxValue * 0.9);
            case 3:
            case 4:
            case 5:
                return maxValue;
            default:
                return 0;
        }
    }

    // Ваш код починається тут

    public HashMap<String, HashMap<String, ArrayList<Integer>>> fillGrades(HashMap<String,
            ArrayList<String>> studentsGroups, int[] points) {

        HashMap<String, HashMap<String, ArrayList<Integer>>> StudentsPoints = new HashMap<>();

        for (Map.Entry<String, ArrayList<String>> group: studentsGroups.entrySet()) {

            HashMap<String, ArrayList<Integer>> studentsOfTheGroup = new HashMap<>();

            for (String student : group.getValue()) {

                ArrayList<Integer> randomGrades = new ArrayList<>();

                for (int point : points) {
                    randomGrades.add(randValue(point));
                }

                studentsOfTheGroup.put(student, randomGrades);
            }
            StudentsPoints.put(group.getKey(), studentsOfTheGroup);
        }

        StudentsPoints.entrySet()
                .stream()
                .sorted(Map.Entry.<String, HashMap<String, ArrayList<Integer>>>comparingByKey())
                .forEach(System.out::println);

        return StudentsPoints;
    }

    // Ваш код закінчується тут

    // Завдання 3
    // Заповніть словник, де:
    // - ключ – назва групи
    // - значення – словник, де:
    //   - ключ – студент, який відносяться до відповідної групи
    //   - значення – сума оцінок студента


    // Ваш код починається тут

    public HashMap<String, HashMap<String, Integer>> showGradesSum(HashMap<String,
            HashMap<String, ArrayList<Integer>>> grades) {

        HashMap<String, HashMap<String, Integer>> sumPoints = new HashMap<>();

        for (Map.Entry<String, HashMap<String, ArrayList<Integer>>> group : grades.entrySet()) {

            HashMap<String, Integer> studGrade = new HashMap<>();

            for (Map.Entry<String, ArrayList<Integer>> student : group.getValue().entrySet()) {

                int sum = 0;
                for (int i : student.getValue()) {
                    sum += i;
                }

                studGrade.put(student.getKey(), sum);
            }
            sumPoints.put(group.getKey(), studGrade);
        }

        sumPoints.entrySet()
                .stream()
                .sorted(Map.Entry.<String, HashMap<String, Integer>>comparingByKey())
                .forEach(System.out::println);

        return sumPoints;
    }


        // Ваш код закінчується тут


    // Завдання 4
    // Заповніть словник, де:
    // - ключ – назва групи
    // - значення – середня оцінка всіх студентів групи


    // Ваш код починається тут

    public HashMap<String, Float> showAvgGradesInGroups(HashMap<String,
            HashMap<String, Integer>> sumGrades) {

        HashMap<String, Float> groupAvg = new HashMap<>();

        for (Map.Entry<String, HashMap<String, Integer>> group: sumGrades.entrySet()) {

            float sumInGroup = 0;

            for (Map.Entry<String, Integer> student: group.getValue().entrySet()) {
                sumInGroup += student.getValue();
            }

            float avgGrade = (float) sumInGroup / group.getValue().size();
            groupAvg.put(group.getKey(), avgGrade);
        }

        groupAvg.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Float>comparingByKey())
                .forEach(System.out::println);

        return groupAvg;
    }

    // Ваш код закінчується тут

    // Завдання 5
    // Заповніть словник, де:
    // - ключ – назва групи
    // - значення – масив студентів, які мають >= 60 балів

    // Ваш код починається тут

    public HashMap<String, ArrayList<String>> showBestInGroups(HashMap<String,
            HashMap<String, Integer>> sumGrades) {

        HashMap<String, ArrayList<String>> passedPerGroup = new HashMap<>();

        for (Map.Entry<String, HashMap<String, Integer>> group: sumGrades.entrySet()) {

            ArrayList<String> bestStudents = new ArrayList<>();

            for (Map.Entry<String, Integer> student: group.getValue().entrySet()) {
                if (student.getValue() >= 60)
                    bestStudents.add(student.getKey());

            }

            passedPerGroup.put(group.getKey(), bestStudents);
        }

        passedPerGroup.entrySet()
                .stream()
                .sorted(Map.Entry.<String, ArrayList<String>>comparingByKey())
                .forEach(System.out::println);

        return passedPerGroup;
    }

    // Ваш код закінчується тут

}







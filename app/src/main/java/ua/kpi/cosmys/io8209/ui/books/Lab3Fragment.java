package ua.kpi.cosmys.io8209.ui.books;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import ua.kpi.cosmys.io8209.R;

public class Lab3Fragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lab3, container, false);

        LinearLayout bookList = root.findViewById(R.id.book_linear_scrolable);
        ArrayList<ConstraintLayout> booksLinear = new ArrayList<>();

        try {
            ArrayList<Book> books = parseBooks(readFile(root.getContext(), R.raw.bookslist));
            for (Book book :
                    books) {
                ConstraintLayout bookLayTmp = new ConstraintLayout(root.getContext());
                bookLayTmp.setLayoutParams(
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                bookList.addView(bookLayTmp);

                ImageView imageTmp = new ImageView(root.getContext());
                imageTmp.setId(imageTmp.hashCode());
                ConstraintLayout.LayoutParams imgParams =
                        new ConstraintLayout.LayoutParams(200, 200);
                if (book.getImagePath().length() != 0)
                    imageTmp.setImageResource(
                            getResId(book.getImagePath().toLowerCase()
                                    .split("\\.")[0], R.drawable.class));
                bookLayTmp.addView(imageTmp, 0, imgParams);

                ConstraintLayout textConstraint = new ConstraintLayout(root.getContext());
                textConstraint.setId(textConstraint.hashCode());
                bookLayTmp.addView(textConstraint, 1);

                TextView textTitle = new TextView(root.getContext());
                textTitle.setId(textTitle.hashCode());
                textTitle.setPadding(0, 1, 5, 2);
                textTitle.setText(book.getTitle());
                ConstraintLayout.LayoutParams textTitleParams =
                        new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                ConstraintLayout.LayoutParams.WRAP_CONTENT);
                textConstraint.addView(textTitle, 0, textTitleParams);

                TextView textSubtitle = new TextView(root.getContext());
                textSubtitle.setText(book.getSubtitle());
                textSubtitle.setPadding(0, 1, 5, 2);
                textSubtitle.setId(textSubtitle.hashCode());
                ConstraintLayout.LayoutParams textSubtitleParams =
                        new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                                ConstraintLayout.LayoutParams.WRAP_CONTENT);
                textConstraint.addView(textSubtitle, 1, textSubtitleParams);

                TextView textPrice = new TextView(root.getContext());
                textPrice.setText(book.getPrice());
                textPrice.setPadding(0, 0, 5, 2);
                textPrice.setId(textPrice.hashCode());
                ConstraintLayout.LayoutParams textPriceParams =
                        new ConstraintLayout.LayoutParams(ConstraintSet.WRAP_CONTENT,
                                ConstraintSet.WRAP_CONTENT);
                textConstraint.addView(textPrice, 2, textPriceParams);

                ConstraintSet textConstraintSet = new ConstraintSet();
                textConstraintSet.clone(textConstraint);

                textConstraintSet.connect(textTitle.getId(), ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                textConstraintSet.connect(textTitle.getId(), ConstraintSet.START,
                        ConstraintSet.PARENT_ID, ConstraintSet.START);
                textConstraintSet.connect(textSubtitle.getId(), ConstraintSet.TOP,
                        textTitle.getId(), ConstraintSet.BOTTOM);
                textConstraintSet.connect(textSubtitle.getId(), ConstraintSet.START,
                        ConstraintSet.PARENT_ID, ConstraintSet.START);
                textConstraintSet.connect(textSubtitle.getId(), ConstraintSet.BOTTOM,
                        textPrice.getId(), ConstraintSet.TOP);
                textConstraintSet.connect(textPrice.getId(), ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                textConstraintSet.connect(textPrice.getId(), ConstraintSet.START,
                        ConstraintSet.PARENT_ID, ConstraintSet.START);

                textConstraintSet.setVerticalBias(textSubtitle.getId(), 0.25f);

                textConstraintSet.setMargin(textSubtitle.getId(), ConstraintSet.TOP, 1);
                textConstraintSet.setMargin(textSubtitle.getId(), ConstraintSet.BOTTOM, 1);

                textConstraintSet.applyTo(textConstraint);

                ConstraintSet bookLayTmpSet = new ConstraintSet();
                bookLayTmpSet.clone(bookLayTmp);

                bookLayTmpSet.connect(imageTmp.getId(), ConstraintSet.START,
                        ConstraintSet.PARENT_ID, ConstraintSet.START);
                bookLayTmpSet.connect(imageTmp.getId(), ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                bookLayTmpSet.connect(imageTmp.getId(), ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                bookLayTmpSet.connect(textConstraint.getId(), ConstraintSet.END,
                        ConstraintSet.PARENT_ID, ConstraintSet.END);
                bookLayTmpSet.connect(textConstraint.getId(), ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID, ConstraintSet.TOP);
                bookLayTmpSet.connect(textConstraint.getId(), ConstraintSet.BOTTOM,
                        ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                bookLayTmpSet.connect(textConstraint.getId(), ConstraintSet.START,
                        imageTmp.getId(), ConstraintSet.END);

                bookLayTmpSet.constrainWidth(textConstraint.getId(), ConstraintSet.MATCH_CONSTRAINT);
                bookLayTmpSet.constrainHeight(textConstraint.getId(), ConstraintSet.MATCH_CONSTRAINT);

                bookLayTmpSet.applyTo(bookLayTmp);

                booksLinear.add(bookLayTmp);
            }
        } catch (ParseException e) {
            System.err.println("Incorrect content of JSON file!");
            e.printStackTrace();
        }

        LinearLayout fragmentBooksLay = root.findViewById(R.id.fragment_books_lay);

        fragmentBooksLay.post(() -> {
            int width = fragmentBooksLay.getWidth();
            for (ConstraintLayout bookshelf :
                    booksLinear) {
                bookshelf.getChildAt(0).setLayoutParams(
                        new ConstraintLayout.LayoutParams(width / 3, width / 4));
            }
        });

        return root;
    }

    public static String readFile(Context context, @RawRes int id) {
        InputStream inputStream = context.getResources().openRawResource(id);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int size;
        try {
            while ((size = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, size);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            System.err.println("FIle cannot be reading!");
            e.printStackTrace();
        }
        return outputStream.toString();
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private ArrayList<Book> parseBooks(String jsonText) throws ParseException {
        ArrayList<Book> result = new ArrayList<>();

        JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonText);

        JSONArray books = (JSONArray) jsonObject.get("books");
        for (Object book : books) {
            JSONObject tmp = (JSONObject) book;
            result.add(new Book(
                    (String) tmp.get("title"),
                    (String) tmp.get("subtitle"),
                    (String) tmp.get("isbn13"),
                    (String) tmp.get("price"),
                    (String) tmp.get("image")
            ));
        }

        return result;
    }
}
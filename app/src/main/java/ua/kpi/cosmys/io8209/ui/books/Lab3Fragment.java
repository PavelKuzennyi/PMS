package ua.kpi.cosmys.io8209.ui.books;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.daimajia.swipe.SwipeLayout;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import ua.kpi.cosmys.io8209.R;

public class Lab3Fragment extends Fragment {

    private HashMap<ConstraintLayout, Book> booksMap;
    private View root;

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_lab3, container, false);

        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);

        LinearLayout bookList = root.findViewById(R.id.book_linear_scrolable);
        booksMap = new HashMap<>();

        try {
            ArrayList<Book> books = parseBooks(readTextFile(root.getContext(), R.raw.bookslist));
            for (Book book :
                    books) {
                addNewBook(root, bookList, book, backgroundResource);
            }
        } catch (ParseException e) {
            System.err.println("Incorrect content of JSON file!");
            e.printStackTrace();
        }


        SearchView simpleSearchView = root.findViewById(R.id.btn_search);

        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                int countResults = 0;
                for (ConstraintLayout book :
                        booksMap.keySet()) {
                    if (newText == null){
                        ((SwipeLayout) book.getParent()).setVisibility(View.VISIBLE);
                        countResults++;
                    }
                    else {
                        if (booksMap.get(book).getTitle().toLowerCase()
                                .contains(newText.toLowerCase()) || newText.length() == 0){
                            ((SwipeLayout) book.getParent()).setVisibility(View.VISIBLE);
                            countResults++;
                        }
                        else
                            ((SwipeLayout) book.getParent()).setVisibility(View.GONE);
                    }
                }

                if (countResults == 0){
                    root.findViewById(R.id.no_items_view).setVisibility(View.VISIBLE);
                }
                else {
                    root.findViewById(R.id.no_items_view).setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int countResults = 0;
                for (ConstraintLayout book :
                        booksMap.keySet()) {
                    if (newText == null){
                        ((SwipeLayout) book.getParent()).setVisibility(View.VISIBLE);
                        countResults++;
                    }
                    else {
                        if (booksMap.get(book).getTitle().toLowerCase()
                                .contains(newText.toLowerCase()) || newText.length() == 0){
                            ((SwipeLayout) book.getParent()).setVisibility(View.VISIBLE);
                            countResults++;
                        }
                        else
                            ((SwipeLayout) book.getParent()).setVisibility(View.GONE);
                    }
                }

                if (countResults == 0){
                    root.findViewById(R.id.no_items_view).setVisibility(View.VISIBLE);
                }
                else {
                    root.findViewById(R.id.no_items_view).setVisibility(View.GONE);
                }
                return false;
            }
        });

        ImageButton btnAddBook = root.findViewById(R.id.btn_add_book);
        btnAddBook.setOnClickListener(v -> {
            BookAddView popUpClass = new BookAddView();
            Object[] popups = popUpClass.showPopupWindow(v);

            View popupView = (View) popups[0];
            PopupWindow popupWindow = (PopupWindow) popups[1];

            EditText inputTitle = popupView.findViewById(R.id.input_title);
            EditText inputSubtitle = popupView.findViewById(R.id.input_subtitle);
            EditText inputPrice = popupView.findViewById(R.id.input_price);

            Button buttonAdd = popupView.findViewById(R.id.button_add_add);
            buttonAdd.setOnClickListener(v1 -> {
                if (inputTitle.getText().toString().length() != 0 &&
                        inputSubtitle.getText().toString().length() != 0 &&
                        inputPrice.getText().toString().length() != 0) {

                    addNewBook(root, bookList, new Book(inputTitle.getText().toString(),
                                    inputSubtitle.getText().toString(), "$" +
                                    inputPrice.getText().toString()),
                            backgroundResource);
                    changeLaySizes();

                    popupWindow.dismiss();
                }
                else{
                    Toast.makeText(getActivity(), "You must fill all fields!",
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        changeLaySizes();

        return root;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        changeLaySizes();
    }

    private void changeLaySizes(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) root.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        for (ConstraintLayout bookshelf :
                booksMap.keySet()) {
            bookshelf.getChildAt(0).setLayoutParams(
                    new ConstraintLayout.LayoutParams(width/3, width/3));
        }
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "RtlHardcoded"})
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addNewBook(View root, LinearLayout bookList,
                            Book book, int backgroundResource){
        SwipeLayout swipeLay = new SwipeLayout(root.getContext());
        swipeLay.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLay.setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.RIGHT));
        bookList.addView(swipeLay);

        ImageButton btnBin = new ImageButton(root.getContext());
        btnBin.setPadding(50, 0, 50, 0);
        btnBin.setImageResource(R.drawable.ic_baseline_delete_outline_24);
        btnBin.setBackgroundResource(R.drawable.outline_bin);

        btnBin.setForeground(root.getContext().getDrawable(backgroundResource));

        LinearLayout.LayoutParams btnBinParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        btnBinParams.gravity = Gravity.RIGHT;
        swipeLay.addDrag(SwipeLayout.DragEdge.Right, btnBin);
        swipeLay.addView(btnBin, 0, btnBinParams);

        ConstraintLayout bookLayTmp = new ConstraintLayout(root.getContext());
        bookLayTmp.setBackgroundResource(R.drawable.color_outline);

        bookLayTmp.setForeground(root.getContext().getDrawable(backgroundResource));

        bookLayTmp.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        swipeLay.addView(bookLayTmp, 1);

        btnBin.setOnClickListener(v -> binClicked(bookList, swipeLay, booksMap, bookLayTmp));

        bookLayTmp.setOnClickListener(v -> {
            if (book.getIsbn13().length() != 0 && !book.getIsbn13().equals("noid")) {
                BookInfoView popUpClass = new BookInfoView();
                popUpClass.showPopupWindow(v, book);
            }
        });

        ImageView imageTmp = new ImageView(root.getContext());
        imageTmp.setId(imageTmp.hashCode());
        ConstraintLayout.LayoutParams imgParams =
                new ConstraintLayout.LayoutParams(300, 300);
        imageTmp.setImageResource(
                (book.getImagePath().length() == 0)? R.drawable.no_image:
                        getResId(book.getImagePath().toLowerCase().split("\\.")[0], R.drawable.class));
        bookLayTmp.addView(imageTmp, 0, imgParams);

        ConstraintLayout textConstraint = new ConstraintLayout(root.getContext());
        textConstraint.setId(textConstraint.hashCode());
        bookLayTmp.addView(textConstraint, 1);

        TextView textTitle = new TextView(root.getContext());
        textTitle.setId(textTitle.hashCode());
        textTitle.setPadding(0, 1, 5, 1);
        textTitle.setText(book.getTitle());
        ConstraintLayout.LayoutParams textTitleParams =
                new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        textConstraint.addView(textTitle, 0, textTitleParams);

        TextView textSubtitle = new TextView(root.getContext());
        textSubtitle.setText(book.getSubtitle());
        textSubtitle.setPadding(0, 1, 5, 1);
        textSubtitle.setId(textSubtitle.hashCode());
        ConstraintLayout.LayoutParams textSubtitleParams =
                new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        textConstraint.addView(textSubtitle, 1, textSubtitleParams);

        TextView textPrice = new TextView(root.getContext());
        textPrice.setText(book.getPrice());
        textPrice.setPadding(0, 0, 5, 4);
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

        textConstraintSet.setVerticalBias(textSubtitle.getId(), 0.3f);

        textConstraintSet.setMargin(textSubtitle.getId(), ConstraintSet.TOP, 3);
        textConstraintSet.setMargin(textSubtitle.getId(), ConstraintSet.BOTTOM, 3);

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

        booksMap.put(bookLayTmp, book);
    }

    private void binClicked(LinearLayout bookshelf,
                            SwipeLayout swipeLayout,
                            HashMap<ConstraintLayout, Book> map,
                            ConstraintLayout key){
        map.remove(key);
        swipeLayout.removeAllViewsInLayout();
        bookshelf.removeView(swipeLayout);
    }

    private static String readTextFile(Context context, @RawRes int id){
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

    private static int getResId(String resName, Class<?> c) {
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
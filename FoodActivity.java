package com.example.fastfood.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fastfood.DAO.FoodDao;
import com.example.fastfood.DAO.ImageDao;
import com.example.fastfood.Model.Food;
import com.example.fastfood.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FoodActivity extends AppCompatActivity {
    Button add;
    int a, checkImg = 0, REQUEST_CODE_FOLDER = 123;
    ImageView img;
    Bitmap bitmap;
    FoodDao foodDao;
    ImageDao imageDao;
    ListView lv;
    CollectionReference foodref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_toolbar)));
        getSupportActionBar().setTitle("FOOD");

        add = findViewById(R.id.food_btn_add);
        lv = findViewById(R.id.food_lv);

        SharedPreferences pref = getSharedPreferences("USER_FILE_LOGIN", MODE_PRIVATE);
        String userName = pref.getString("USERNAME_LOGIN", "");

        foodDao = new FoodDao(this);
        imageDao = new ImageDao(this);

        if (!userName.equals("admin")) {
            add.setVisibility(View.GONE);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = -1;
                openDialog(Gravity.CENTER);
            }
        });

        foodref = FirebaseFirestore.getInstance().collection("Food");
        foodref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                foodDao.getFood(lv);
            }
        });

    }

    private void openDialog(int gravity) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_food);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }

        //
        TextView tvTitle = dialog.findViewById(R.id.dialog_food_tv_title);

        EditText edtName = dialog.findViewById(R.id.dialog_food_tv_name);
        EditText edtPrice = dialog.findViewById(R.id.dialog_food_tv_price);

        TextInputLayout tilName = dialog.findViewById(R.id.dialog_food_til_name);
        TextInputLayout tilPrice = dialog.findViewById(R.id.dialog_food_til_price);

        img = dialog.findViewById(R.id.dialog_food_img);

        Button btnadd = dialog.findViewById(R.id.dialog_food_btn_add);
        Button btncancel = dialog.findViewById(R.id.dialog_food_btn_cancel);


        if (a == -1) {
            //Dialog them
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_FOLDER);
                }
            });

            btnadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int temp = 0;
                    if (edtName.getText().toString().isEmpty()) {
                        tilName.setError("Chưa nhập tên món");
                        temp++;
                    } else {
                        tilName.setErrorEnabled(false);
                    }
                    if (edtPrice.getText().toString().isEmpty()) {
                        tilPrice.setError("Chưa nhập giá món");
                        temp++;
                    } else {
                        tilPrice.setErrorEnabled(false);
                    }
                    if (checkImg == 1) {
                        if (temp == 0) {
                            foodDao.addFood(new Food(edtName.getText().toString(), Integer.parseInt(edtPrice.getText().toString()), 1, 0), img);
                            dialog.dismiss();
                        }
                    } else {
                        Toast.makeText(FoodActivity.this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(FoodActivity.this, "Huỷ thêm", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                checkImg = 1;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
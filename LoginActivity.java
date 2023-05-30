package com.example.fastfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout tiltk, tilmk;
    EditText edttk, edtmk;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_toolbar)));
        getSupportActionBar().setTitle("LOGIN");

        tiltk = findViewById(R.id.login_til_tk);
        tilmk = findViewById(R.id.login_til_mk);
        edttk = findViewById(R.id.login_edt_tk);
        edtmk = findViewById(R.id.login_edt_mk);
        btn = findViewById(R.id.login_btn);

        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        String userName = pref.getString("USERNAME", "");
        String password = pref.getString("USERPASS", "");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FoodDao foodDao;
//                foodDao = new FoodDao();
//                foodDao.addFood(new Food( "123", "1",  1, 1));
//                foodDao.getFood();
//                foodDao.updateFood(new Food( "RyZENyxCxCtCt52KaMaR", "Hải mõm",  10000, 1000));
//                foodDao.deleteFood("17tM6k8p8iTHBxiXVU5U");
                if (edttk.getText().toString().equals(userName) && edtmk.getText().toString().equals(password)) {
                    tiltk.setError("");
                    tilmk.setError("");
                    rememberUser(userName, password);
                    startActivity(new Intent(LoginActivity.this, MainKhachHang.class));
                } else {
                    tiltk.setError("Tài khoản hoặc mật khẩu không chính xác");
                    tilmk.setError("Tài khoản hoặc mật khẩu không chính xác");
                }
            }
        });


    }

    public void rememberUser(String u, String p) {
        SharedPreferences pref = getSharedPreferences("USER_FILE_LOGIN", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("USERNAME_LOGIN", u);
        editor.putString("USERPASS_LOGIN", p);
        editor.commit();
    }
}
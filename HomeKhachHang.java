package com.example.fastfood.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastfood.Activity.DrinkActivity;
import com.example.fastfood.Activity.FoodActivity;
import com.example.fastfood.DAO.DrinkDao;
import com.example.fastfood.LoginActivity;
import com.example.fastfood.R;
import com.example.fastfood.databinding.KhachHangHomeBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeKhachHang extends Fragment {
    private KhachHangHomeBinding binding;
    TextView tvLogin;
    LinearLayout btn_food, btn_drink;
    RecyclerView lv;
    DrinkDao drinkDao;
    CollectionReference foodref;
    CollectionReference drinkref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = KhachHangHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvLogin = root.findViewById(R.id.home_khach_hang_login);

        btn_food = root.findViewById(R.id.home_ln_food);
        btn_drink = root.findViewById(R.id.home_ln_drink);
        lv = root.findViewById(R.id.home_lv);

        drinkDao = new DrinkDao(getActivity());
        foodref = FirebaseFirestore.getInstance().collection("Food");
        drinkref = FirebaseFirestore.getInstance().collection("Drink");
        foodref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                drinkDao.getTopHome(lv, 0);
            }
        });
        drinkref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                drinkDao.getTopHome(lv, 0);
            }
        });

        SharedPreferences pref = getActivity().getSharedPreferences("USER_FILE_LOGIN", getActivity().MODE_PRIVATE);
        String userName = pref.getString("USERNAME_LOGIN", "");

        if (userName.equals("admin")) {
            tvLogin.setVisibility(View.GONE);
        }
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FoodActivity.class));
            }
        });

        btn_drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DrinkActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
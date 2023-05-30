package com.example.fastfood.ui.GioHang;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fastfood.DAO.BillDao;
import com.example.fastfood.DAO.BillFvsDDao;
import com.example.fastfood.R;
import com.example.fastfood.databinding.KhachHangGioHangBinding;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class GioHangKhachHang extends Fragment {

    private KhachHangGioHangBinding binding;

    TextView tvTenBan, tv_id;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = KhachHangGioHangBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tvTenBan = root.findViewById(R.id.gio_hang_tv_tenBan);
        listView = root.findViewById(R.id.gio_hang_lv);

        BillFvsDDao billFvsDDao = new BillFvsDDao(getActivity());


        SharedPreferences pref = getActivity().getSharedPreferences("TABLE", getActivity().MODE_PRIVATE);
        String name = pref.getString("NAME", "");
        String id = pref.getString("IDBASKET", "K lay dc");
        FirebaseFirestore.getInstance().collection("BillFood").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                billFvsDDao.getBillBasket(listView, id);
            }
        });
        FirebaseFirestore.getInstance().collection("BillDrink").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                billFvsDDao.getBillBasket(listView, id);
            }
        });
        billFvsDDao.getBillBasket(listView, id);
        if (!name.equals("")) {
            tvTenBan.setText(name);
        } else {
            root.findViewById(R.id.gio_hang_btn_add).setEnabled(false);
        }

        root.findViewById(R.id.gio_hang_btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Order Thành Công", Toast.LENGTH_SHORT).show();
                BillDao billDao = new BillDao(getActivity());
                billDao.addBill(0, id);
                billFvsDDao.xacnhanorder(id);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("IDBILL", id);
                editor.commit();
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
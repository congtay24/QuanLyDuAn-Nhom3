package com.example.fastfood.ui.DonHang;

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

import com.example.fastfood.DAO.BasketDao;
import com.example.fastfood.DAO.BillDao;
import com.example.fastfood.DAO.BillFvsDDao;
import com.example.fastfood.DAO.TableDao;
import com.example.fastfood.Model.Table;
import com.example.fastfood.R;
import com.example.fastfood.databinding.KhachHangHoaDonBinding;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class HoaDonKhachHang extends Fragment {

    private KhachHangHoaDonBinding binding;

    ListView listView;
    TextView tvGia;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = KhachHangHoaDonBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        listView = root.findViewById(R.id.hoa_don_lv);
        tvGia = root.findViewById(R.id.hoa_don_gia);

        SharedPreferences pref = getActivity().getSharedPreferences("TABLE", getActivity().MODE_PRIVATE);
        String id = pref.getString("ID", "");
        String name = pref.getString("NAME", "");
        String idBill = pref.getString("IDBILL", "");
        BillFvsDDao billFvsDDao = new BillFvsDDao(getActivity());
        FirebaseFirestore.getInstance().collection("BillFood").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                billFvsDDao.getHoaDon(listView, idBill, tvGia);
            }
        });
        FirebaseFirestore.getInstance().collection("BillDrink").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                billFvsDDao.getHoaDon(listView, idBill, tvGia);
            }
        });
        if (idBill.equals("")) {
            root.findViewById(R.id.hoa_don_btn_xac_nhan).setEnabled(false);
        }
        root.findViewById(R.id.hoa_don_btn_xac_nhan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idBill.equals("") || tvGia.getText().toString().equals("0")) {
                    Toast.makeText(getContext(), "Bạn chưa order đồ!", Toast.LENGTH_SHORT).show();
                } else {
                    TableDao tableDao = new TableDao(getActivity());
                    tableDao.bochonBan(new Table(id, name, false));
                    BasketDao basketDao = new BasketDao(getActivity());
                    basketDao.deleteBasket(pref.getString("IDBASKET", "K lay dc"));
                    BillDao billDao = new BillDao(getActivity());
                    billDao.updateBill(pref.getString("IDBASKET", "K lay dc"), Integer.parseInt(tvGia.getText().toString()), true);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    billFvsDDao.getHoaDon(listView, "idBill", tvGia);
                    root.findViewById(R.id.hoa_don_btn_xac_nhan).setEnabled(false);
                    Toast.makeText(getContext(), "Đã thanh toán", Toast.LENGTH_SHORT).show();
                }
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
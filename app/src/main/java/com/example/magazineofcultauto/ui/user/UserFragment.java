package com.example.magazineofcultauto.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.magazineofcultauto.LoginActivity;
import com.example.magazineofcultauto.R;
import com.example.magazineofcultauto.activityRegistration;
import com.example.magazineofcultauto.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);
        Intent log = new Intent(getActivity(), LoginActivity.class);
        Intent regis = new Intent(getActivity(), activityRegistration.class);


        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button login = root.findViewById(R.id.log);
        Button reg = root.findViewById(R.id.reg);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(log);
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(regis);
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
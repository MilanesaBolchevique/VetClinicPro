package com.example.vetclinicpro;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgottenPassword extends DialogFragment {
    FirebaseAuth mAuth;

    Button resetPassword;
    String mail;
    EditText mEditTextEmail;
    MediaPlayer mp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgotten_password, container, false);
        mAuth = FirebaseAuth.getInstance();
        resetPassword = view.findViewById(R.id.btn_resetpassword);
        mEditTextEmail = view.findViewById(R.id.editTextMail);

        mp = MediaPlayer.create(getContext(), R.raw.click_sound);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                mail = mEditTextEmail.getText().toString();

                if(mail.isEmpty()){
                    Toast.makeText(getContext(), "Ingrese su dirección de correo", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword();
                }
            }
        });
        return view;
    }

    // Método para restablecer la contraseña con Firebase
    private void resetPassword() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Se ha enviado un correo para restablecer sus datos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No se ha podido restablecer la contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
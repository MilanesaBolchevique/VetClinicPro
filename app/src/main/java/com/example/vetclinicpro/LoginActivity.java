package com.example.vetclinicpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private Button btn_register, btn_login;
    private Button btn_forgottenpassword;
    private EditText nameUser, mailUser, passwordUser;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Si el usuario ya se ha logueado previamente, mantenemos abierta la sesion
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_login);

        mp = MediaPlayer.create(this, R.raw.click_sound);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(this);
        mailUser = findViewById(R.id.mail);
        passwordUser = findViewById(R.id.password);
        btn_forgottenpassword = findViewById(R.id.btn_forgottenpassword);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);


        //Dar funcion al boton de registro
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                //Creamos las variables que le pasaremos a firebase
                String mail, password;
                mail = mailUser.getText().toString();
                password = passwordUser.getText().toString();
                //Condicion por si introducimos datos vacios, si no se cumple, procedemos con el registro
                if (mail.isEmpty() || password.isEmpty()) {
                    Snackbar.make(findViewById(R.id.cl_baseview), "Complete los campos", Snackbar.LENGTH_SHORT).show();
                } else {
                    registerUser(mail, password);
                }
            }
        });

        //Dar funcion Log-in al boton de logueo (metodo utilizando lambda para recortar codigo)
        btn_login.setOnClickListener(v -> {
            mp.start();
            //Creamos las variables que le pasaremos a firebase
            String mail, password;
            mail = String.valueOf(mailUser.getText());
            password = String.valueOf(passwordUser.getText());

            //Condicion por si introducimos datos vacios, si no se cumple, procedemos con el registro
            if (mail.isEmpty() || password.isEmpty()) {
                Snackbar.make(findViewById(R.id.cl_baseview), "Complete los campos", Snackbar.LENGTH_SHORT).show();
                //Toast.makeText(RegisterActivity.this, "Complete los datos", Toast.LENGTH_SHORT).show();
            } else {
                //Ejecutamos el metodo de log-in
                loginUser(mail, password);
            }
        });

        btn_forgottenpassword.setOnClickListener(v -> {
            ForgottenPassword forgottenPasswordFragment = new ForgottenPassword();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .add(android.R.id.content, forgottenPasswordFragment, "tag_forgotten_password_fragment")
                    .addToBackStack(null)
                    .commit();
        });
    }


    //M E T O D O S



    //Metodo para logear usuarios con correo en Firebase
    private void loginUser(String mailUser, String passwordUser) {
        // Mostrar ProgressBar al iniciar el registro
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(mailUser, passwordUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Credenciales Correctas", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Credenciales Incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //Metodo para registrar usuarios con correo en Firebase
    private void registerUser(String mailUser, String passwordUser) {
        // Mostrar ProgressBar al iniciar el registro
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(mailUser, passwordUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    // Crearemos un objeto HashMap para los datos del usuario
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", mailUser);
                    user.put("ranking", 0);
                    // Agregaremos el fichero del usuario a una colección llamada "usuarios"
                    mFirestore.collection("usuarios").document(userId)
                            .set(user)
                            .addOnSuccessListener(aVoid -> {
                                Snackbar.make(findViewById(R.id.cl_baseview), "Usuario registrado correctamente", Snackbar.LENGTH_SHORT).show();
                                // Ocultamos la progressbar una vez terminado
                                progressBar.setVisibility(View.GONE);
                            })
                            .addOnFailureListener(e -> {
                                Snackbar.make(findViewById(R.id.cl_baseview), "Error: El usuario ya existe", Snackbar.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            });
                }
            } else {
                Snackbar.make(findViewById(R.id.cl_baseview), "Error: El usuario ya existe", Snackbar.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
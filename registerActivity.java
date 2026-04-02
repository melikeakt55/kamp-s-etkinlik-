package com.example.kampsuyg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText username, email, phone, password;
    RadioGroup roleGroup;
    Button registerBtn;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.reg_username);
        email = findViewById(R.id.reg_email);
        phone = findViewById(R.id.reg_phone);
        password = findViewById(R.id.reg_password);
        roleGroup = findViewById(R.id.reg_role_group);
        registerBtn = findViewById(R.id.registerBtn);
        db = new DBHelper(this);

        registerBtn.setOnClickListener(v -> {
            String user = username.getText().toString().trim();
            String mail = email.getText().toString().trim();
            String tel = phone.getText().toString().trim();
            String pass = password.getText().toString().trim();

            int selectedId = roleGroup.getCheckedRadioButtonId();
            String role = "student";
            if (selectedId == R.id.radio_club) {
                role = "club";
            }

            if (user.isEmpty() || mail.isEmpty() || tel.isEmpty() || pass.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kullanıcı adı benzersizlik kontrolü
            if (db.checkUsernameExists(user)) {
                Toast.makeText(RegisterActivity.this, "Bu kullanıcı adı zaten alınmış!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Şifre doğrulama
            String passwordError = validatePassword(pass);
            if (passwordError != null) {
                Toast.makeText(RegisterActivity.this, passwordError, Toast.LENGTH_LONG).show();
                return;
            }

            boolean insert = db.insertUser(user, mail, tel, pass, role);
            if (insert) {
                Toast.makeText(RegisterActivity.this, "Kayıt Başarılı!", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("username", user);
                intent.putExtra("password", pass);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Kayıt sırasında bir hata oluştu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String validatePassword(String pass) {
        if (pass.length() < 6) {
            return "Şifre en az 6 karakter olmalıdır!";
        }
        if (!Pattern.compile("[A-Z]").matcher(pass).find()) {
            return "Şifre en az bir büyük harf içermelidir!";
        }
        if (!Pattern.compile("[0-9]").matcher(pass).find()) {
            return "Şifre en az bir rakam içermelidir!";
        }
        if (!Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(pass).find()) {
            return "Şifre en az bir özel karakter içermelidir!";
        }
        
        // Ardışık karakter kontrolü (örn: 123, abc)
        if (hasConsecutiveChars(pass)) {
            return "Şifre ardışık rakam veya harf (123, abc gibi) içermemelidir!";
        }

        return null;
    }

    private boolean hasConsecutiveChars(String pass) {
        pass = pass.toLowerCase();
        for (int i = 0; i < pass.length() - 2; i++) {
            char c1 = pass.charAt(i);
            char c2 = pass.charAt(i + 1);
            char c3 = pass.charAt(i + 2);

            // Rakam veya harf mi kontrol et
            if (Character.isLetterOrDigit(c1) && Character.isLetterOrDigit(c2) && Character.isLetterOrDigit(c3)) {
                if ((c2 == c1 + 1 && c3 == c2 + 1)) {
                    return true;
                }
            }
        }
        return false;
    }
}

package com.example.map3dtest.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.nbapp.R;
import com.example.map3dtest.main.MapProjectActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_passwd;
    private EditText et_account;
    private ImageView show_passwd;
    private Boolean showPassword =true;
    private Button login;
    private TextView forget;
    private TextView join;
    private  TextView contact_us;
    private  TextView about_us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(Button) findViewById(R.id.login);
        et_passwd=(EditText)findViewById(R.id.et_passwd);
        et_account=(EditText) findViewById(R.id.et_account);
        show_passwd=(ImageView)findViewById(R.id.show_passwd);
        forget=(TextView)findViewById(R.id.forget);
        join=(TextView)findViewById(R.id.join);
        contact_us=(TextView)findViewById(R.id.contact_us);
        about_us=(TextView)findViewById(R.id.about_us);
        show_passwd.setImageDrawable(getResources().getDrawable(R.drawable.pwdshow));
        show_passwd.setOnClickListener(this);
        login.setOnClickListener(this);
        forget.setOnClickListener(this);
        join.setOnClickListener(this);
        contact_us.setOnClickListener(this);
        about_us.setOnClickListener(this);

    }
    @Override
    public void onClick(View view){
        if(view.getId() == R.id.login){
            String account=et_account.getText().toString();
            String password=et_passwd.getText().toString();
            if(account.equals("admin")&&password.equals("admin"))
            {
                Intent intent =new Intent(LoginActivity.this,MapProjectActivity.class);
                startActivity(intent);
            }
            else Toast.makeText(LoginActivity.this,"account or password is invalid",Toast.LENGTH_SHORT).show();
        }else if(view.getId() == R.id.show_passwd){
            if (showPassword) {// 显示密码
                show_passwd.setImageResource(R.drawable.pwdshow);
                et_passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                et_passwd.setSelection(et_passwd.getText().toString().length());
                Log.d("haha", "show");
                showPassword = !showPassword;
            } else {// 隐藏密码
                show_passwd.setImageResource(R.drawable.pwdhide);
                et_passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                et_passwd.setSelection(et_passwd.getText().toString().length());
                showPassword = !showPassword;
                Log.d("haha", "hide");
            }

        }else if(view.getId() == R.id.forget){

        }else if(view.getId() == R.id.join){

        }else if(view.getId() == R.id.contact_us){

        }else if(view.getId() == R.id.about_us){

        }


    }
}

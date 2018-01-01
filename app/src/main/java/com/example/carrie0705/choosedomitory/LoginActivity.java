package com.example.carrie0705.choosedomitory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class LoginActivity extends Activity implements View.OnClickListener {
    private static final int LOGIN_SUCCESS = 1;
    private EditText userid,password;
    private Button login;
    String stuid,stupass;
    private CheckBox checkBox;

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch ((msg.what)){
                case LOGIN_SUCCESS:
                    ifsucceed((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userid = (EditText) findViewById(R.id.number);
        password = (EditText) findViewById(R.id.password_edit);
        login = (Button) findViewById(R.id.loginButton);
        login.setOnClickListener(this);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }
    public void onClick(View view){
        if(view.getId() == R.id.loginButton){

            Log.d("login","login on click");
            stuid = userid.getText().toString();
            stupass = password.getText().toString();
            if(stuid == null){
                Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
            }else if(stupass == null){
                Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
            }else{
                Login(stuid,stupass);
            }
        }

    }
    private void Login(String stuid,String stupass){

        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/Login?username="+stuid+"&password="+stupass;
        Log.d("login",address);

        new Thread(new Runnable(){
            public void run() {
                HttpURLConnection con = null;
                // Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }};
                // Install the all-trusting trust manager
                try {
                    HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
                    SSLContext sc = SSLContext.getInstance("TLS");
                    sc.init(null, trustAllCerts, new SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                    URL url = new URL(address);

                    con = (HttpURLConnection) url.openConnection();
                    con.setConnectTimeout(5000);
                    con.setRequestMethod("GET");
                    con.connect();

                    System.out.println(con.getResponseCode() + " " + con.getResponseMessage());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));//设置编码,否则中文乱码
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine())!=null){
                        response.append(str);
                        Log.d("login",str);
                    }
                    String responseStr = response.toString();
                    String result = jsonLogin(responseStr);
                    Message msg = new Message();
                    msg.what = LOGIN_SUCCESS;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                    // 断开连接

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
    public void ifsucceed(String result){
        Log.d("login",result);
        if(result.equals("40001")||result.equals("40002")){
            Toast.makeText(LoginActivity.this,"登录失败,请检查用户名或密码是否正确.",Toast.LENGTH_LONG).show();
        }else if(result.equals("0")){
            Log.d("login","登陆成功");
            Intent i = new Intent(this,UserMessageActivity.class);
            i.putExtra("stuid",stuid);
            i.putExtra("page","login");
            startActivity(i);
            //startActivityForResult(i,1);
        }
    }

    private String jsonLogin(String jsondata){
        String res = null;
        try{
            JSONObject json = new JSONObject(jsondata);
            res = json.getString("errcode");
            Log.d("login",res);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return res;
    }
}

package com.example.carrie0705.choosedomitory;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import bean.Student;

public class UserMessageActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int ID_SUCCESS = 1;
    private TextView nameTv,studentIDTv,sexTv,schoolPlaceTv,gradeTv,vcodeTv,dormTv,roomTv;
    private Button enterB;
    private String gender,stuidForIntent,location;

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg1){
            switch ((msg1.what)){
                case ID_SUCCESS:
                    Log.d("student","handler");
                    init((Student)msg1.obj);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);

        enterB = (Button) findViewById(R.id.enter);
        enterB.setOnClickListener(this);

        Intent intent = this.getIntent();
        String stuid = intent.getStringExtra("stuid");
        getStuInformation(stuid);
    }
    @Override
   public void onClick(View v) {
        if(v.getId() == R.id.enter){
            if(location.equals("大兴")) {
                Log.d("student", "turn to select");
                Intent i = new Intent(this, SelectyActivity.class);
                i.putExtra("stuid", stuidForIntent);
                i.putExtra("gender", gender);
                startActivityForResult(i,1);
            }else{
                Toast.makeText(UserMessageActivity.this,"无需选择宿舍",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void init(Student student){

        Log.d("student","init");
        nameTv = (TextView) findViewById(R.id.stuName);
        studentIDTv = (TextView) findViewById(R.id.stuId);
        sexTv = (TextView) findViewById(R.id.stuSex);
        schoolPlaceTv =(TextView) findViewById(R.id.stuPlace);
        gradeTv = (TextView) findViewById(R.id.stuGrade);
        vcodeTv = (TextView) findViewById(R.id.stuVCode);
        dormTv = (TextView) findViewById(R.id.buildingNum);
        roomTv = (TextView) findViewById(R.id.domitoryNum);

        gender = student.getStuSex();
        stuidForIntent = student.getStuId();
        Log.d("student",student.getStuName());
        nameTv.setText("姓名："+student.getStuName());
        studentIDTv.setText("学号："+student.getStuId());
        sexTv.setText("性别："+student.getStuSex());

        schoolPlaceTv.setText("校区："+student.getStuPlace());
        gradeTv.setText("年级："+student.getStuGrade());
        vcodeTv.setText("验证码："+student.getStuVcode());
        dormTv.setText("宿舍楼号："+student.getBuildNum());
        roomTv.setText("宿舍号："+student.getDomitoryNum());
    }
    private void getStuInformation(String stuid){

        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getDetail?stuid="+stuid;
        Log.d("student",address);

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
                Student student = null;
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
                    }
                    String responseStr = response.toString();
                    student = jsonInformation(responseStr);
                    if(student != null){
                        Log.d("student",student.toString());
                    }
                    Message msg1 = new Message();
                    msg1.what = ID_SUCCESS;
                    msg1.obj = student;
                    mHandler.sendMessage(msg1);

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

    public Student jsonInformation(String jsondata){
        Student student = new Student();
        String res = null;
        try{
            JSONObject json1 = new JSONObject(jsondata);
            res = json1.getString("data");
            Log.d("student","res"+res);

            JSONObject json = json1.getJSONObject("data");
            String id = json.getString("studentid");
            student.setStuId(id);
            student.setStuName(json.getString("name"));
            student.setStuSex(json.getString("gender"));
            student.setStuGrade(json.getString("grade"));
            student.setStuPlace(json.getString("location"));
            location = json.getString("location");
            student.setStuVcode(json.getString("vcode"));
            if(json.has("room")){
                student.setDomitoryNum(json.getString("room"));
            }else{
                student.setDomitoryNum("暂未选择宿舍");
            }
            if(json.has("building")){
                student.setBuildNum(json.getString("building"));
            }else{
                student.setBuildNum("暂未选择宿舍楼");
            }

            student.setDomitoryNum(json.getString("room"));
        }catch(JSONException e){
            e.printStackTrace();
        }
        return student;
    }


}

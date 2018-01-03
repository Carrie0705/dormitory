package com.example.carrie0705.choosedomitory;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import bean.Building;
import bean.Student;

public class SelectyActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int STUNUM_SUCCESS =1;
    private static final int DORM_SELECT_INFOMATION = 3;
    private Spinner buildingSP,stuSP;
    private ArrayList<String> stuNum,buildingNum;
    private ArrayAdapter<String> stuAdapter,buildAdapter;
    private TextView numForBedTv,stuidTv;
    private LinearLayout stu2,stu3,stu4,stu2Vc,stu3Vc,stu4Vc;
    private int countStu;
    private int building;
    private String gender;
    private String stuid;
    private Button submit;
    private EditText stuID1,stuID2,stuID3,stuVcode1,stuVcode2,stuVcode3;

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg2){
            switch ((msg2.what)){
                case STUNUM_SUCCESS:
                    Log.d("select","handler");
                    initForBed((Building)msg2.obj);
                    break;
                case DORM_SELECT_INFOMATION:
                    Log.d("select","dorm select");
                    int errorCode = (int) msg2.obj;
                    feedback(errorCode);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);

        Intent intent = this.getIntent();
        stuid = intent.getStringExtra("stuid");
        String genderStr = intent.getStringExtra("gender");
        if(genderStr.equals("女")){
            gender = "2";
        }else{
            gender = "1";
        }

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        init();
        SpinnerSet();


    }
    private void init(){
        stu2 = (LinearLayout) findViewById(R.id.stu1);
        stu3 = (LinearLayout) findViewById(R.id.stu2);
        stu4 = (LinearLayout) findViewById(R.id.stu3);

        stu2Vc = (LinearLayout) findViewById(R.id.stuvcode1);
        stu3Vc = (LinearLayout) findViewById(R.id.stuvcode2);
        stu4Vc = (LinearLayout) findViewById(R.id.stuvcode3);

        buildingSP = (Spinner) findViewById(R.id.spinnerForbuild);
        stuSP = (Spinner) findViewById(R.id.spinnerForStuNum);
        numForBedTv = (TextView) findViewById(R.id.numForBed);

        stuidTv = (TextView) findViewById(R.id.stuselfID);
        stuidTv.setText("学号:"+stuid);

        stu2.setVisibility(View.INVISIBLE);
        stu3.setVisibility(View.INVISIBLE);
        stu4.setVisibility(View.INVISIBLE);
        stu2Vc.setVisibility(View.INVISIBLE);
        stu3Vc.setVisibility(View.INVISIBLE);
        stu4Vc.setVisibility(View.INVISIBLE);
    }
    private void initForBed(Building build){

        switch(building){
            case 5:
                numForBedTv.setText("5号楼剩余床位："+build.getBuilding5());
                break;
            case 13:
                numForBedTv.setText("13号楼剩余床位："+build.getBuilding13());
                break;
            case 14:
                numForBedTv.setText("14号楼剩余床位："+build.getBuilding14());
                break;
            case 8:
                numForBedTv.setText("8号楼剩余床位："+build.getBuilding8());
                break;
            case 9:
                numForBedTv.setText("9号楼剩余床位："+build.getBuilding9());
                break;
            default:
                break;
        }
    }

    private void SpinnerSet(){
        stuNum = new ArrayList<>();
        stuNum.add("单人办理");
        stuNum.add("双人办理");
        stuNum.add("三人办理");
        stuNum.add("四人办理");
        stuAdapter = new ArrayAdapter<String>(SelectyActivity.this,R.layout.support_simple_spinner_dropdown_item,stuNum);
        stuAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        stuSP.setAdapter(stuAdapter);
        stuSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        countStu = 1;
                        stu2.setVisibility(View.INVISIBLE);
                        stu3.setVisibility(View.INVISIBLE);
                        stu4.setVisibility(View.INVISIBLE);
                        stu2Vc.setVisibility(View.INVISIBLE);
                        stu3Vc.setVisibility(View.INVISIBLE);
                        stu4Vc.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        countStu = 2;
                        stu2.setVisibility(View.VISIBLE);
                        stu3.setVisibility(View.INVISIBLE);
                        stu4.setVisibility(View.INVISIBLE);
                        stu2Vc.setVisibility(View.VISIBLE);
                        stu3Vc.setVisibility(View.INVISIBLE);
                        stu4Vc.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        countStu = 3;
                        stu2.setVisibility(View.VISIBLE);
                        stu3.setVisibility(View.VISIBLE);
                        stu4.setVisibility(View.INVISIBLE);
                        stu2Vc.setVisibility(View.VISIBLE);
                        stu3Vc.setVisibility(View.VISIBLE);
                        stu4Vc.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        countStu = 4;
                        stu2.setVisibility(View.VISIBLE);
                        stu3.setVisibility(View.VISIBLE);
                        stu4.setVisibility(View.VISIBLE);
                        stu2Vc.setVisibility(View.VISIBLE);
                        stu3Vc.setVisibility(View.VISIBLE);
                        stu4Vc.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}


        });
       //选择宿舍楼号
        buildingNum = new ArrayList<>();
        buildingNum.add("5号楼");
        buildingNum.add("13号楼");
        buildingNum.add("14号楼");
        buildingNum.add("8号楼");
        buildingNum.add("9号楼");
        buildAdapter = new ArrayAdapter<String>(SelectyActivity.this,R.layout.support_simple_spinner_dropdown_item,buildingNum);
        buildAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        buildingSP.setAdapter(buildAdapter);
        buildingSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        building = 5;
                        break;
                    case 1:
                        building = 13;
                        break;
                    case 2:
                        building = 14;
                        break;
                    case 3:
                        building = 8;
                        break;
                    case 4:
                        building = 9;
                        break;
                    default:
                        break;
                }
                getBuildingNum();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void getBuildingNum(){

        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/getRoom?gender="+gender;
        Log.d("select",address);

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
                Building build = null;
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
                    build = jsonForRoom(responseStr);
                    if(build != null){
                        Log.d("select",build.toString());
                    }
                    Message msg2 = new Message();
                    msg2.what = STUNUM_SUCCESS;
                    msg2.obj = build;
                    mHandler.sendMessage(msg2);

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

    private Building jsonForRoom(String jsondata){
        Building build = new Building();
        try {
            JSONObject json1 = new JSONObject(jsondata);
            JSONObject json = json1.getJSONObject("data");
            build.setBuilding5(json.getString("5"));
            build.setBuilding8(json.getString("8"));
            build.setBuilding9(json.getString("9"));
            build.setBuilding13(json.getString("13"));
            build.setBuilding14(json.getString("14"));
        }catch(JSONException e){
            e.printStackTrace();
        }
        return build;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submit){
            //String json = getJson();
            //Log.d("select",json);
            SelectRoom(getJson());
        }
    }
    private String getJson(){
        stuID1 = (EditText) findViewById(R.id.stuId1);
        stuID2 = (EditText) findViewById(R.id.stuId2);
        stuID3 = (EditText) findViewById(R.id.stuId3);

        stuVcode1 = (EditText) findViewById(R.id.stuvcodeEdit1);
        stuVcode2 = (EditText) findViewById(R.id.stuvcodeEdit2);
        stuVcode3 = (EditText) findViewById(R.id.stuvcodeEdit3);


        String id2 = stuID1.getText().toString();
        String id3 = stuID2.getText().toString();
        String id4 = stuID3.getText().toString();

        String vcode2 = stuVcode1.getText().toString();
        String vcode3 = stuVcode2.getText().toString();
        String vcode4 = stuVcode3.getText().toString();

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("num", countStu);
            jsonObj.put("stuid", stuid);
            jsonObj.put("stu1id", id2);
            jsonObj.put("v1code", vcode2);
            jsonObj.put("stu2id", id3);
            jsonObj.put("v2code", vcode3);
            jsonObj.put("stu3id", id4);
            jsonObj.put("v3code", vcode4);
            jsonObj.put("buildingNo", building);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("select", jsonObj.toString());
        return jsonObj.toString();
    }
    private void SelectRoom(final String jsondata){
        final String address = "https://api.mysspku.com/index.php/V1/MobileCourse/SelectRoom";
        new Thread(new Runnable() {
            @Override
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
                int errorCode = -1;
                try {
                    //MyX509TrustManager.allowAllSSL();
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.setConnectTimeout(4000);

                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream(), "utf-8");
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    bufferedWriter.write(jsondata);
                    bufferedWriter.flush();

                    InputStream inputStream = con.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = bufferedReader.readLine()) != null) {
                        response.append(str);
                    }
                    String responseStr = response.toString();
                    Log.d("select", responseStr);
                    bufferedReader.close();
                    inputStream.close();
                    bufferedWriter.close();
                    outputStreamWriter.close();
                    errorCode = getErrorCode(responseStr);
                    Log.d("select", errorCode+" errorCode");

                    Message msg2 = new Message();
                    msg2.what = DORM_SELECT_INFOMATION;
                    msg2.obj = errorCode;
                    mHandler.sendMessage(msg2);
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

    public int getErrorCode(String jsonData) {
        int errorCode = -1;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            if (jsonObject != null) {
                errorCode = jsonObject.getInt("errcode");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorCode;
    }

    public void feedback(int errorcode){
        if (errorcode == 0) {
            if(Integer.parseInt(stuid) %2 !=0){
                Toast.makeText(SelectyActivity.this, "选择宿舍失败", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, UserMessageActivity.class);
                intent.putExtra("stuid", stuid);
                startActivityForResult(intent,1);

            }else{
                Toast.makeText(SelectyActivity.this, "选择宿舍成功", Toast.LENGTH_LONG).show();
                Log.d("select", "成功");
                Intent intent = new Intent(this, UserMessageActivity.class);
                intent.putExtra("stuid", stuid);
                startActivityForResult(intent,1);
            }

        } else if (errorcode == 40001) {
            Log.d("select", "40001");
            Toast.makeText(SelectyActivity.this, "学号不存在，请重新填写", Toast.LENGTH_LONG).show();
        } else if (errorcode == 40002) {
            Log.d("select", "40002");
            Toast.makeText(SelectyActivity.this, "验证码错误，请重新填写", Toast.LENGTH_LONG).show();
        } else if (errorcode == 40009) {
            Log.d("select", "40009");
            Toast.makeText(SelectyActivity.this, "参数错误", Toast.LENGTH_LONG).show();
        }
    }
}

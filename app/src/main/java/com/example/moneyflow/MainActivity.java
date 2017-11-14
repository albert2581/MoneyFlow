package com.example.moneyflow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button food,play,income,inquir,reset,exit,back,saveBMoney;
    private EditText what,money,date,sbm;
    private TextView doWhat,nowMoney,selM;
    private CheckBox incomeCheck,expenseCheck,moneyCheck;
    private ListView incomeListView,expenseListView,userListView;
    private List<String> incomeList,expenseList,userList;
    private ArrayAdapter<String> listAdapter;
    private Spinner spinner;
    private ArrayAdapter<String> monthList;
    private Context mContext;
    private String[] month = {"本月","一月", "二月", "三月", "四月",
            "五月","六月", "七月", "八月", "九月", "十月","十一月", "十二月"};
    int resetB=0,incomeT=0,expenseT=0,incomeM=0,expenseM=0;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization_userName("userName",true);
        actionBar = this.getSupportActionBar();
        final String[] userNameAfterSplit = read("userName","money","").split(",");
        userList = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(read("userName", "knowLine","")); i++) {
            userList.add(userNameAfterSplit[i]);
        }
        actionBar.setTitle(userNameAfterSplit[0]);
        setBtn();
        initialization(actionBar.getTitle().toString() + "/");
        btnDo();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "選擇使用者");
        menu.add(1, Menu.FIRST, Menu.NONE, "新增使用者");
        menu.add(2, Menu.FIRST, Menu.NONE, "移除使用者");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getGroupId() == Menu.NONE) {
            chooseUser();
            Toast.makeText(getApplicationContext(),
                    item.getTitle(),
                    Toast.LENGTH_SHORT).show();
        }else if (item.getGroupId() == 1){
            addUser();
            Toast.makeText(getApplicationContext(),
                    item.getTitle(),
                    Toast.LENGTH_SHORT).show();
        }else if (item.getGroupId() == 2){
            delUser();
            Toast.makeText(getApplicationContext(),
                    item.getTitle(),
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void setBtn(){
        food = (Button)findViewById(R.id.food);
        play = (Button)findViewById(R.id.play);
        income = (Button)findViewById(R.id.income);
        inquir = (Button)findViewById(R.id.inquire);
        reset = (Button)findViewById(R.id.reset);
        exit = (Button)findViewById(R.id.exit);
    }
    private void setListEdit(View item){
        what = (EditText) item.findViewById(R.id.what);
        money = (EditText) item.findViewById(R.id.money);
        date = (EditText) item.findViewById(R.id.date);
    }
    private void btnDo(){
        food.setOnClickListener(btnDoListener);
        play.setOnClickListener(btnDoListener);
        income.setOnClickListener(btnDoListener);
        inquir.setOnClickListener(btnDoListener);
        reset.setOnClickListener(btnDoListener);
        exit.setOnClickListener(btnDoListener);
    }
    private Button.OnClickListener btnDoListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.food:
                            resetB=0;
                            alertDio("　　吃什麼：","食物","expense","",0,"",0);
                            break;
                        case R.id.play:
                            resetB=0;
                            alertDio("　做了什麼：", "玩樂","expense","",0,"",0);
                            break;
                        case R.id.income:
                            resetB=0;
                            alertDio("　　為何：　","進帳","income","",0,"",0);
                            break;
                        case R.id.inquire:
                            resetB=0;
                            alertDioSearch("查詢");
                            break;
                        case R.id.reset:
                            resetB=resetB+1;
                            if (resetB==1)
                                Toast.makeText(getApplicationContext(), "請再按4次", Toast.LENGTH_SHORT).show();
                            else if (resetB==2)
                                Toast.makeText(getApplicationContext(), "請再按3次", Toast.LENGTH_SHORT).show();
                            else if (resetB==3)
                                Toast.makeText(getApplicationContext(), "請再按2次", Toast.LENGTH_SHORT).show();
                            else if (resetB==4)
                                Toast.makeText(getApplicationContext(), "確定要重置就再按一次吧", Toast.LENGTH_SHORT).show();
                            else {
                                for(int i=1;i<13;i++){
                                    del(Integer.toString(i),"income/doWhat",true,true,actionBar.getTitle().toString()+"/");
                                    del(Integer.toString(i),"income/money",true,true,actionBar.getTitle().toString()+"/");
                                    del(Integer.toString(i),"income/date",true,true,actionBar.getTitle().toString()+"/");
                                    del(Integer.toString(i),"expense/doWhat",true,true,actionBar.getTitle().toString()+"/");
                                    del(Integer.toString(i),"expense/money",true,true,actionBar.getTitle().toString()+"/");
                                    del(Integer.toString(i),"expense/date",true,true,actionBar.getTitle().toString()+"/");
                                }

                                del("beforeMoney",false,true,actionBar.getTitle().toString()+"/");
                                del("monthUsedMoney",false,true,actionBar.getTitle().toString()+"/");
                                Toast.makeText(getApplicationContext(), "重置完畢", Toast.LENGTH_SHORT).show();
                                resetB=0;
                            }
                            break;
                        case R.id.exit:
                            android.os.Process.killProcess(android.os.Process.myPid());
                            break;
                    }
                }
            };
    private void chooseUser(){
        final String[] userNameAfterSplit = read("userName","money","").split(",");
        userList = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(read("userName", "knowLine","")); i++) {
            userList.add(userNameAfterSplit[i]);
        }
        new AlertDialog.Builder(MainActivity.this)
                .setItems(userList.toArray(new String[userList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = userList.get(which);
                        Toast.makeText(getApplicationContext(),  name, Toast.LENGTH_SHORT).show();
                        actionBar.setTitle(name);
                    }
                })
                .show();
    }
    private void addUser(){
        alertDio("　　使用者名稱：", "新增使用者", "userName", "addUser", 0, "", 0);
    }
    private void delUser(){
        final String[] userNameAfterSplit = read("userName", "money", "").split(",");
        userList = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(read("userName", "knowLine","")); i++) {
            userList.add(userNameAfterSplit[i]);
        }
        new AlertDialog.Builder(MainActivity.this)
                .setItems(userList.toArray(new String[userList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = userList.get(which);
                        alertDioSure("警告！", "確認要刪除本項目嗎？\n", "userName", which, "user", 0, name);
                    }
                })
                .show();
    }
    private void alertDio(String whatD,String title, final String sourse, final String method
            , final int position, final String select, final int setMonth){
        final View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.list_layout, null);
        doWhat =(TextView)item.findViewById(R.id.doWhat);
        doWhat.setText(whatD);
        if (method.equals("edit")){
            doWhat.setText("　　緣由：　");
            editList(sourse,position,item);
        }else if (method.equals("addUser")){
            TextView moneyView =(TextView)item.findViewById(R.id.moneyView);
            doWhat =(TextView)item.findViewById(R.id.doWhat);
            TextView dateView =(TextView)item.findViewById(R.id.dateView);
            money=(EditText)item.findViewById(R.id.money);
            what = (EditText)item.findViewById(R.id.what);
            date = (EditText)item.findViewById(R.id.date);
            money.setInputType(InputType.TYPE_CLASS_TEXT);
            moneyView.setText(whatD);
            doWhat.setVisibility(View.INVISIBLE);
            dateView.setVisibility(View.INVISIBLE);
            what.setVisibility(View.INVISIBLE);
            date.setVisibility(View.INVISIBLE);
        }
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setView(item)
                .setNeutralButton("填完了!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setListEdit(item);
                        if (method.equals("addUser") && !(money.getText().toString().equals(""))) {
                            save(money.getText().toString(), sourse, true,"");
                            Toast.makeText(getApplicationContext(), "儲存\n" + money.getText().toString(), Toast.LENGTH_SHORT).show();
                            initialization(money.getText().toString()+"/");
                        } else {
                            if (!(what.getText().toString().equals("")) &&
                                    !(money.getText().toString().equals("")) &&
                                    !(date.getText().toString().equals(""))) {
                                if (method.equals("edit")) {
                                    delList(position, sourse + "/doWhat",actionBar.getTitle().toString()+"/");
                                    delList(position, sourse + "/money",actionBar.getTitle().toString()+"/");
                                    delList(position, sourse + "/date",actionBar.getTitle().toString()+"/");
                                }
                                save(what.getText().toString(), sourse + "/doWhat", true,actionBar.getTitle().toString()+"/");
                                save(money.getText().toString(), sourse + "/money", true,actionBar.getTitle().toString()+"/");
                                save(date.getText().toString(), sourse + "/date", true,actionBar.getTitle().toString()+"/");
                                if (method.equals("edit") && select.equals("total")) {
                                    incomeT = 0;
                                    expenseT = 0;
                                    jumpToMain();
                                    jumpToMoneyLayout_total();
                                } else if (method.equals("edit") && select.equals("month")) {
                                    incomeM = 0;
                                    expenseM = 0;
                                    jumpToMain();
                                    jumpToMoneyLayout_month(setMonth);
                                }
                                Toast.makeText(getApplicationContext(), "儲存\n" + what.getText().toString() + "\n"
                                        + money.getText().toString() + "\n" + date.getText().toString(), Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getApplicationContext(), "有欄位沒填喔！"
                                        , Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
    private void alertDioSearch(String title){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage("選擇欲選擇項目")
                .setNegativeButton("查詢總額", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        jumpToMoneyLayout_total();
                    }
                })
                .setNeutralButton("查詢月收支", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        jumpToMoneyLayout_month(0);
                    }
                })
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
    private void alertDioSure(String title,String msg,final String path,final int position, final String select, final int setMonth, final String name){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (select.equals("total")) {
                            delList(position, path + "/doWhat",actionBar.getTitle().toString()+"/");
                            delList(position, path + "/money",actionBar.getTitle().toString()+"/");
                            delList(position, path + "/date",actionBar.getTitle().toString()+"/");
                            incomeT = 0;
                            expenseT = 0;
                            jumpToMain();
                            jumpToMoneyLayout_total();
                        } else if (select.equals("month")) {
                            delList(position, path + "/doWhat",actionBar.getTitle().toString()+"/");
                            delList(position, path + "/money",actionBar.getTitle().toString()+"/");
                            delList(position, path + "/date",actionBar.getTitle().toString()+"/");
                            incomeM = 0;
                            expenseM = 0;
                            jumpToMain();
                            jumpToMoneyLayout_month(setMonth);
                        } else if (select.equals("user")) {
                            File mSDFile = null;
                            //檢查有沒有SD卡裝置
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)) {
                            } else    //取得SD卡儲存路徑
                                mSDFile = Environment.getExternalStorageDirectory();
                            //建立文件檔儲存路徑
                            File mFile = new File(mSDFile.getParent() + "/" + mSDFile.getName() + "/MoneyFlow/" + name);
                            mFile.delete();
                            delList(position, path,"");
                        }
                    }
                })
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
    public void rename(String path,String pathN,String user){
        Log.d("renameOP",path);
        Log.d("renameNP",pathN);
        String[] AfterSplit = new String[0];
        if (Integer.parseInt(read(path, "knowLine",user))==0) {
            makeDir(pathN, true,user);
            Log.d("renameMakeDir0",pathN);
        }
        else
            AfterSplit = read(path,"money",user).split(",");
        for (int i = 0; i < Integer.parseInt(read(path, "knowLine",user)); i++) {
            if (Integer.parseInt(read(path, "knowLine",user))==0){
            }
            else{
                save(AfterSplit[i], pathN, true,user);
                Log.d("renameSave",pathN);
            }
        }
        del(path, true, false,user);
    }
    public String makeDir(String mounth,String path,Boolean continuance,String user){hello_IM_ERROR
        String address="";
        File mSDFile = null;
        //檢查有沒有SD卡裝置
        if(Environment.getExternalStorageState().equals( Environment.MEDIA_REMOVED))
            return "noSD";
        else    //取得SD卡儲存路徑
            mSDFile = Environment.getExternalStorageDirectory();
        //建立文件檔儲存路徑
        File mFile = new File(mSDFile.getParent() + "/" + mSDFile.getName() + "/MoneyFlow/"+user);
        File mFile4 = new File(mSDFile.getParent() + "/" + mSDFile.getName() + "/MoneyFlow/"+user+mounth);
        for(int i=1;i<13;i++){
            File mFile2 = new File(mSDFile.getParent() + "/" + mSDFile.getName() + "/MoneyFlow/"+user+Integer.toString(i)+"income");
            File mFile3 = new File(mSDFile.getParent() + "/" + mSDFile.getName() + "/MoneyFlow/"+user+Integer.toString(i)+"expense");
            if(!mFile2.exists())
                mFile2.mkdirs();
            if(!mFile3.exists())
                mFile3.mkdirs();
        }
        //若沒有檔案儲存路徑時則建立此檔案路徑
        if(!mFile.exists())
            mFile.mkdirs();
        if(!mFile4.exists())
            mFile4.mkdirs();

        try {
            FileWriter mFileWriter = new FileWriter( mSDFile.getParent() + "/" +
                    mSDFile.getName() + "/MoneyFlow/"+user+mounth+"/"+path+".txt",continuance );
            address=mSDFile.getParent() + "/" +mSDFile.getName() + "/MoneyFlow/"+user+mounth+path+".txt";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }
    private void save(String doWhat,String path,boolean continuance,String user){
        try
        {
            FileWriter mFileWriter = new FileWriter( makeDir(path,continuance,user),continuance );
            mFileWriter.write(doWhat);
            mFileWriter.write("\n");
            mFileWriter.close();
        }catch (Exception e)
        {
        }
    }
    public void initialization_userName(String path,Boolean continuance){
        File mSDFile = null;
        //檢查有沒有SD卡裝置
        if(Environment.getExternalStorageState().equals( Environment.MEDIA_REMOVED))
            return ;
        else    //取得SD卡儲存路徑
            mSDFile = Environment.getExternalStorageDirectory();
        //建立文件檔儲存路徑
        File mFile = new File(mSDFile.getParent() + "/" + mSDFile.getName() + "/MoneyFlow");
        if(!mFile.exists())
            mFile.mkdirs();
        try {
            FileWriter mFileWriter = new FileWriter( mSDFile.getParent() + "/" +
                    mSDFile.getName() + "/MoneyFlow/"+path+".txt",continuance );
            mFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileReader mFileReader = new FileReader(mSDFile.getParent() + "/" + mSDFile.getName()
                    + "/MoneyFlow/"+ path+".txt");
            BufferedReader mBufferedReader = new BufferedReader(mFileReader);
            String mReadText = "";
            String mTextLine= mBufferedReader.readLine();
            if (mTextLine==null) {
                FileWriter mFileWriter = new FileWriter( mSDFile.getParent() + "/" +
                        mSDFile.getName() + "/MoneyFlow/"+path+".txt",continuance );
                mFileWriter.write("user");
                mFileWriter.write("\n");
                mFileWriter.close();
            }
            mFileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialization(String user){
        makeDir("0","income/doWhat", true,user);
        makeDir("0","income/money", true,user);
        makeDir("0","income/date", true,user);
        makeDir("0","expense/doWhat", true,user);
        makeDir("0","expense/money", true,user);
        makeDir("0","expense/date", true,user);
        makeDir("0","beforeMoney", true,user);
        makeDir("0","monthUsedMoney", true,user);
    }
    public void del(String mounth,String path,Boolean continuance,Boolean rebuild,String user){hello_IM_ERROR
        Log.d("del", "start");
        File f =  new File(Environment.getExternalStorageDirectory().getParent()
                + "/" + Environment.getExternalStorageDirectory().getName()
                + "/MoneyFlow/"+user +mounth+"/"+ path+".txt");
        f.delete();
        if (rebuild)
            makeDir("0",path, continuance,user);
    }
    private void setMoneyItem(){
        //Button
        saveBMoney=(Button)findViewById(R.id.saveBMoney);
        back= (Button)findViewById(R.id.back);
        //TextView
        selM=(TextView)findViewById(R.id.selM);
        nowMoney=(TextView)findViewById(R.id.nowMoney);
        //EditView
        sbm=(EditText)findViewById(R.id.sbm);
        //CheckBox
        incomeCheck = (CheckBox)findViewById(R.id.checkBox);
        expenseCheck = (CheckBox)findViewById(R.id.checkBox2);
        moneyCheck =(CheckBox)findViewById(R.id.checkBox3);
        //spinner
        spinner = (Spinner)findViewById(R.id.spinner);
    }
    private void checkTDo(){
        incomeCheck.setOnCheckedChangeListener(CheckT);
        expenseCheck.setOnCheckedChangeListener(CheckT);
        moneyCheck.setOnCheckedChangeListener(CheckT);
    }
    private void checkMDo(){
        incomeCheck.setOnCheckedChangeListener(CheckM);
        expenseCheck.setOnCheckedChangeListener(CheckM);
        moneyCheck.setOnCheckedChangeListener(CheckM);
    }
    //read beforeMoney or monthUsedMoney and check information
    public void catchSbm(String readPath){
        int money=0;
        if (Integer.parseInt(read(readPath, "knowLine",actionBar.getTitle().toString()+"/"))!=0){
            money=Integer.parseInt(read(readPath, "before",actionBar.getTitle().toString()+"/"));
            Log.d("catchSbmMoney",String.valueOf(money));
        }
        else
            Log.e("catchSbmMoney", String.valueOf(money));
    }
    public void nowMoneyRefresh(){
        incomeCheck.setChecked(false);
        incomeCheck.setChecked(true);
    }
    public void jumpToMoneyLayout_total() {
        setContentView(R.layout.money_layout);
        setMoneyItem();
        moneyCheck.setText("原有金錢：　　");
        sbm.setText(read("beforeMoney", "before",actionBar.getTitle().toString()+"/"));
        selM.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        checkTDo();
        catchSbm("beforeMoney");
        if (Integer.parseInt(read("income/doWhat", "knowLine",actionBar.getTitle().toString()+"/"))==0){
        }
        else {
            String[] incomesDoWhatAfterSplit = read("income/doWhat","money",actionBar.getTitle().toString()+"/").split(",");
            String[] incomesMoneyAfterSplit = read("income/money","money",actionBar.getTitle().toString()+"/").split(",");
            String[] incomesDateAfterSplit = read("income/date","money",actionBar.getTitle().toString()+"/").split(",");
            incomeList = new ArrayList<>();
            for (int i = 0; i < Integer.parseInt(read("income/doWhat", "knowLine",actionBar.getTitle().toString()+"/")); i++) {
                Log.d("incomeAfterSplit", incomesDoWhatAfterSplit[i] + "　　" + incomesMoneyAfterSplit[i]
                        + "　　" + incomesDateAfterSplit[i]);
                incomeList.add(incomesDoWhatAfterSplit[i] + "　　" + incomesMoneyAfterSplit[i]
                        + "　　" + incomesDateAfterSplit[i]);
                incomeT=Integer.parseInt(incomesMoneyAfterSplit[i])+incomeT;
                Log.d("incomeT",String.valueOf(incomeT));
            }
            incomeListView = (ListView)findViewById(R.id.listView);
            listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,incomeList);
            incomeListView.setAdapter(listAdapter);
            incomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    alertDio("", "編輯", "income","edit",position,"total",0);
                }
            });
            incomeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    alertDioSure("警告！", "確認要刪除本項目嗎？\n", "income", position, "total", 0,"");
                    return true;
                }
            });
        }
        if (Integer.parseInt(read("expense/doWhat", "knowLine",actionBar.getTitle().toString()+"/"))==0){
        }
        else {
            String[] expenseDoWhatAfterSplit = read("expense/doWhat","money",actionBar.getTitle().toString()+"/").split(",");
            String[] expenseMoneyAfterSplit = read("expense/money","money",actionBar.getTitle().toString()+"/").split(",");
            String[] expenseDateAfterSplit = read("expense/date","money",actionBar.getTitle().toString()+"/").split(",");
            expenseList= new ArrayList<>();
            for (int i = 0; i < Integer.parseInt(read("expense/doWhat", "knowLine",actionBar.getTitle().toString()+"/")); i++) {
                Log.d("expenseAfterSplit", expenseDoWhatAfterSplit[i] + "　　" + expenseMoneyAfterSplit[i]
                        + "　　" + expenseDateAfterSplit[i]);
                expenseList.add(expenseDoWhatAfterSplit[i] + "　　" + expenseMoneyAfterSplit[i]
                        + "　　" + expenseDateAfterSplit[i]);
                expenseT=expenseT-Integer.parseInt(expenseMoneyAfterSplit[i]);
                Log.d("expenseT",String.valueOf(expenseT));
            }
            expenseListView = (ListView)findViewById(R.id.listView2);
            listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,expenseList);
            expenseListView.setAdapter(listAdapter);
            expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    alertDio("", "編輯", "expense", "edit", position,"total",0);
                }
            });
            expenseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    alertDioSure("警告！","確認要刪除本項目嗎？\n","expense", position,"total",0,"");
                    return true;
                }
            });
        }
        nowMoneyRefresh();
        back.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                incomeT=0;
                expenseT=0;
                jumpToMain();
            }
        });
        saveBMoney.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                nowMoneyRefresh();
                if (!(sbm.getText().toString().equals(""))){
                    Log.d("nowMoneySBM", String.valueOf(Integer.parseInt(sbm.getText().toString())));
                    save(sbm.getText().toString(), "beforeMoney", false,actionBar.getTitle().toString()+"/");
                    Toast.makeText(getApplicationContext(), "儲存", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void jumpToMoneyLayout_month(int setMonth){
        setContentView(R.layout.money_layout);
        Log.d("jumpToMonth", "start");
        setMoneyItem();
        moneyCheck.setText("本月可使用金額：　");
        sbm.setText(read("monthUsedMoney", "before",actionBar.getTitle().toString()+"/"));
        selM.setVisibility(View.VISIBLE);
        mContext = this.getApplicationContext();
        monthList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, month);
        spinner.setAdapter(monthList);
        spinner.setSelection(setMonth);
        checkMDo();
        Log.d("jumpToMonth","setListenerOK");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                incomeCheck.setChecked(true);
                expenseCheck.setChecked(true);
                moneyCheck.setChecked(true);
                readMonth(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        back.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                incomeM=0;
                expenseM=0;
                jumpToMain();
            }
        });
        saveBMoney.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                nowMoneyRefresh();
                if (!(sbm.getText().toString().equals(""))) {
                    save(sbm.getText().toString(), "monthUsedMoney", false,actionBar.getTitle().toString()+"/");
                    Toast.makeText(getApplicationContext(), "儲存", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void readMonth(int month){hello_IM_ERROR
        Calendar c = Calendar.getInstance();
        incomeM=0;
        expenseM=0;
        catchSbm("monthUsedMoney");
        if (Integer.parseInt(read("income/doWhat", "knowLine",actionBar.getTitle().toString()+"/"))==0){
        }
        else {
            String[] incomesDoWhatAfterSplit = read("income/doWhat","money",actionBar.getTitle().toString()+"/").split(",");
            String[] incomesMoneyAfterSplit = read("income/money","money",actionBar.getTitle().toString()+"/").split(",");
            String[] incomesDateAfterSplit = read("income/date","money",actionBar.getTitle().toString()+"/").split(",");
            String[] monthS = read("income/date","money",actionBar.getTitle().toString()+"/").split(",|/");
            incomeList = new ArrayList<>();
            for (int i = 0; i < Integer.parseInt(read("income/doWhat", "knowLine",actionBar.getTitle().toString()+"/")); i++) {
                Log.d("incomeAfterSplit", incomesDoWhatAfterSplit[i] + "　　" + incomesMoneyAfterSplit[i]
                        + "　　" + incomesDateAfterSplit[i]);
                Log.d("helloooo",monthS[i*2]);
                if (month==0)
                    month = c.get(Calendar.MONTH)+1;
                if ((month)==Integer.valueOf(monthS[i*2])){
                    incomeList.add(incomesDoWhatAfterSplit[i] + "　　" + incomesMoneyAfterSplit[i]
                            + "　　" + incomesDateAfterSplit[i]);
                    incomeM=Integer.parseInt(incomesMoneyAfterSplit[i])+incomeM;
                    Log.d("incomeM",String.valueOf(incomeM));
                }
            }
            incomeListView = (ListView)findViewById(R.id.listView);
            listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,incomeList);
            incomeListView.setAdapter(listAdapter);
            final int finalMonth = month;
            incomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    delListM(position, finalMonth, "income","edit");
                }
            });
            incomeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    delListM(position, finalMonth,"income","del");
                    return true;
                }
            });
        }
        if (Integer.parseInt(read("expense/doWhat", "knowLine",actionBar.getTitle().toString()+"/"))==0){
        }
        else {
            String[] expenseDoWhatAfterSplit = read("expense/doWhat","money",actionBar.getTitle().toString()+"/").split(",");
            String[] expenseMoneyAfterSplit = read("expense/money","money",actionBar.getTitle().toString()+"/").split(",");
            String[] expenseDateAfterSplit = read("expense/date","money",actionBar.getTitle().toString()+"/").split(",");
            String[] monthS = read("expense/date","money",actionBar.getTitle().toString()+"/").split(",|/");
            expenseList= new ArrayList<>();
            for (int i = 0; i < Integer.parseInt(read("expense/doWhat", "knowLine",actionBar.getTitle().toString()+"/")); i++) {
                Log.d("expenseAfterSplit", expenseDoWhatAfterSplit[i] + "　　" + expenseMoneyAfterSplit[i]
                        + "　　" + expenseDateAfterSplit[i]);
                if (month==0)
                    month = c.get(Calendar.MONTH)+1;
                if ((month)==Integer.valueOf(monthS[i*2])) {
                    expenseList.add(expenseDoWhatAfterSplit[i] + "　　" + expenseMoneyAfterSplit[i]
                            + "　　" + expenseDateAfterSplit[i]);
                    expenseM = expenseM - Integer.parseInt(expenseMoneyAfterSplit[i]);
                    Log.d("expenseM", String.valueOf(expenseM));
                }
            }
            expenseListView = (ListView)findViewById(R.id.listView2);
            listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,expenseList);
            expenseListView.setAdapter(listAdapter);
            final int finalMonth = month;
            expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    delListM(position, finalMonth, "expense","edit");
                }
            });
            expenseListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    delListM(position, finalMonth, "expense","del");
                    return true;
                }
            });
        }
        nowMoneyRefresh();
    }
    public void delListM(int position,int month ,String path,String select){
        Log.d("delListM p,m,p",String.valueOf(position)+","+String.valueOf(month)+","+path);
        Calendar c = Calendar.getInstance();
        int k=0;
        String[] DoWhatAfterSplit = read(path+"/doWhat","money",actionBar.getTitle().toString()+"/").split(",");
        String[] MoneyAfterSplit = read(path+"/money","money",actionBar.getTitle().toString()+"/").split(",");
        String[] DateAfterSplit = read(path+"/date","money",actionBar.getTitle().toString()+"/").split(",");
        String[] monthS = read(path+"/date","money",actionBar.getTitle().toString()+"/").split(",|/");
        for (int i = 0; i < Integer.parseInt(read(path+"/doWhat", "knowLine",actionBar.getTitle().toString()+"/")); i++) {
            if (month==0) {
                month = c.get(Calendar.MONTH) + 1;
                Log.d("delListM m",String.valueOf(month));
            }
            Log.d("delListM i,monthS",String.valueOf(i)+","+monthS[i*2]);
            if ((month)==Integer.valueOf(monthS[i*2])) {
                if (position==k){
                    for (int j = 0; j < Integer.parseInt(read(path + "/doWhat", "knowLine",actionBar.getTitle().toString()+"/")); j++) {
                        Log.d("delListM DoWi,DoWj",DoWhatAfterSplit[i]+","+DoWhatAfterSplit[j]);
                        if (DoWhatAfterSplit[i].equals(DoWhatAfterSplit[j]) &&
                                MoneyAfterSplit[i].equals(MoneyAfterSplit[j]) &&
                                DateAfterSplit[i].equals(DateAfterSplit[j])) {
                            if (select.equals("del"))
                                alertDioSure("警告！" , "確認要刪除本項目嗎？\n", path, j, "month", month,"");
                            else if (select.equals("edit"))
                                alertDio("", "編輯", path,"edit",j, "month",month);
                        }
                    }
                }
                k=k+1;
            }
        }
    }
    public void delList(int position,String path,String user){
        String[] DoWhatAfterSplit = new String[0];
        if (Integer.parseInt(read(path, "knowLine",user))==1){
            makeDir(path + "2", true,user);
        }
        else
            DoWhatAfterSplit = read(path,"money",user).split(",");
        for (int i = 0; i < Integer.parseInt(read(path, "knowLine",user)); i++) {
            if (i==position){
            }
            else {
                save(DoWhatAfterSplit[i], path+"2", true,user);
            }
        }
        del(path, true, false,user);
        rename(path + "2", path,user);
    }
    public void editList(String path,int position,View item){
        setListEdit(item);
        String[] DoWhatAfterSplit = read(path+"/doWhat","money",actionBar.getTitle().toString()+"/").split(",");
        String[] MoneyAfterSplit = read(path+"/money","money",actionBar.getTitle().toString()+"/").split(",");
        String[] DateAfterSplit = read(path+"/date","money",actionBar.getTitle().toString()+"/").split(",");
        for (int i = 0; i < Integer.parseInt(read(path+"/doWhat", "knowLine",actionBar.getTitle().toString()+"/")); i++) {
            if (position==i){
                Log.d("editList", DoWhatAfterSplit[i] + "," + MoneyAfterSplit[i] + "," + DateAfterSplit[i]);
                what.setText(DoWhatAfterSplit[i]);
                money.setText(MoneyAfterSplit[i]);
                date.setText(DateAfterSplit[i]);
            }
        }
    }
    private CheckBox.OnCheckedChangeListener CheckT = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int nMoney=0;
            if (incomeCheck.isChecked()){
                Log.d("IncomeT,nMoney",String.valueOf(incomeT)+","+String.valueOf(nMoney));
                nMoney=incomeT+nMoney;
            }
            if (expenseCheck.isChecked()){
                Log.d("ExpenseT,nMoney",String.valueOf(expenseT)+","+String.valueOf(nMoney));
                nMoney=expenseT+nMoney;
            }
            if (moneyCheck.isChecked()&& !(sbm.getText().toString().equals(""))){
                Log.d("nMoneyT",String.valueOf(nMoney));
                nMoney=nMoney+Integer.parseInt(sbm.getText().toString());
            }
            Log.d("nMoneyTF",String.valueOf(nMoney));
            nowMoney.setText("現有金錢：" + String.valueOf(nMoney));
        }
    };

    private CheckBox.OnCheckedChangeListener CheckM =new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int nMMoney=0;
            if (incomeCheck.isChecked()){
                Log.d("IncomeM,nMMoney",String.valueOf(incomeM)+","+String.valueOf(nMMoney));
                nMMoney=incomeM+nMMoney;
            }
            if (expenseCheck.isChecked()){
                Log.d("ExpenseM,nMMoney",String.valueOf(expenseM)+","+String.valueOf(nMMoney));
                nMMoney=expenseM+nMMoney;
            }
            if (moneyCheck.isChecked() && !(sbm.getText().toString().equals(""))){
                Log.d("nMMoneyT",String.valueOf(nMMoney));
                nMMoney=nMMoney+Integer.parseInt(sbm.getText().toString());
            }
            Log.d("nMMoneyTF",String.valueOf(nMMoney));
            nowMoney.setText("本月尚餘：" + String.valueOf(nMMoney));
        }
    };
    public void jumpToMain(){
        setContentView(R.layout.activity_main);
        setBtn();
        btnDo();
    }
    public String read(String path,String sourse,String user){
        int line=0;
        try{
            Log.d("read","start");
            File mSDFile = null;
            //檢查有沒有SD卡裝置
            if(Environment.getExternalStorageState().equals( Environment.MEDIA_REMOVED))
                return "noSD";
            else    //取得SD卡儲存路徑
                mSDFile = Environment.getExternalStorageDirectory();

            //讀取文件檔路徑
            Log.d("read","checkPath");
            FileReader mFileReader = new FileReader(mSDFile.getParent() + "/" + mSDFile.getName()
                    + "/MoneyFlow/"+user + path+".txt");
            Log.d("read","pathRight");
            BufferedReader mBufferedReader = new BufferedReader(mFileReader);
            String mReadText = "";
            String mTextLine= mBufferedReader.readLine();
            if (sourse.equals("before")){
                while (mTextLine!=null){
                    mReadText+=mTextLine;
                    mTextLine=mBufferedReader.readLine();
                    Log.d("readBefore","readRight");
                }
                return mReadText;
            }
            else if (sourse.equals("money")){
                while (mTextLine!=null){
                    mReadText=mTextLine+","+mReadText;
                    mTextLine=mBufferedReader.readLine();
                    Log.d("readMoney","readRight");
                }
                Log.d("readMoney",mReadText);
                return mReadText;
            }
            else if (sourse.equals("knowLine")){
                while (mTextLine!=null){
                    mReadText+=mTextLine;
                    mTextLine=mBufferedReader.readLine();
                    Log.d("readLine","readRight");
                    line=line+1;
                }
                String sLine =String.valueOf(line);
                Log.d("readLine",sLine);
                return sLine;
            }
            else {
                return "error";
            }
        }
        catch(Exception e)
        {Log.d("read","error");
        }
        return "error";
    }
}
package com.example.admin_on_order;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin_on_order.Fragment.CalculateFragment;
import com.example.admin_on_order.Fragment.MainFragment;
import com.example.admin_on_order.Fragment.OrderFragment;
import com.example.admin_on_order.Fragment.ServiceFragment;

import com.example.admin_on_order.model.OrderModel;
import com.example.admin_on_order.model.OrderRecyclerItem;
import com.example.admin_on_order.model.PrintOrderModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sam4s.printer.Sam4sBuilder;
import com.sam4s.printer.Sam4sPrint;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnBackPressedListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    MainFragment mainFragment;
    ServiceFragment serviceFragment;
    OrderFragment orderFragment;
    CalculateFragment calculateFragment;

    boolean doubleBackPress = false;

    ImageView btnMain, btnService, btnOrder, btnCalc;

    String prevAuthNum = "";
    String prevAuthDate = "";
    String cardNo = "";
    String vanTr = "";
    String dptId = "";
    String menu = "";
    String printDate = "";
    String printTable = "";

    SharedPreferences pref;

    String storeCode;
    String date;
    String time;

    AdminApplication app = new AdminApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("pref", MODE_PRIVATE);

        mainFragment = new MainFragment();
        serviceFragment = new ServiceFragment();
        orderFragment = new OrderFragment();
        calculateFragment = new CalculateFragment();

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_frame, mainFragment, "MainFragment").commit();

        btnMain = (ImageView) findViewById(R.id.btn_bar_main);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Fragment Click Check : " , "Main Fragment Added " + mainFragment.isAdded());
                if (mainFragment.isAdded()) {
                    transaction.remove(mainFragment);
                    mainFragment = new MainFragment();
                }
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_frame, mainFragment).commit();
            }
        });

        btnService = (ImageView) findViewById(R.id.btn_bar_service);
        btnService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Fragment Click Check : " , "Service Fragment Added " + serviceFragment.isAdded());
//                transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fragment_frame, serviceFragment).commit();
            }
        });

        btnOrder = (ImageView) findViewById(R.id.btn_bar_order);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Fragment Click Check : " , "Order Fragment Added " + orderFragment.isAdded());
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_frame, orderFragment).commit();
            }
        });

        btnCalc = (ImageView) findViewById(R.id.btn_bar_calc);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Fragment Click Check : " , "Calculator Fragment Added " + calculateFragment.isAdded());
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_frame, calculateFragment).commit();
            }
        });
//        // ?????? ???????????? ?????????
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//
//        date = dateFormat.format(calendar.getTime());
//        FirebaseDatabase.getInstance().getReference().child("order").child(pref.getString("storename", "")).child(date).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot item : dataSnapshot.getChildren()) {
//                    Log.d("order", "Order Store Name : " + pref.getString("storename", ""));
//                    OrderRecyclerItem model = item.getValue(OrderRecyclerItem.class);
//                    Log.d("Firebase getKey", "onDataChange: " + item.getKey());
//                    if (model.getPrintStatus().equals("x")) {
//                        print(model);
//                        model.setPrintStatus("o");
//                        FirebaseDatabase.getInstance().getReference().child("order").child(pref.getString("storename", "")).child(date).child(item.getKey()).setValue(model);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        FirebaseDatabase.getInstance().getReference().child("service").child(pref.getString("storename", "")).child(date).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot item : dataSnapshot.getChildren()) {
//                    OrderRecyclerItem model = item.getValue(OrderRecyclerItem.class);
//                    if (model.getPrintStatus().equals("x")) {
//                        print(model);
//                        model.setPrintStatus("o");
//                        FirebaseDatabase.getInstance().getReference().child("service").child(pref.getString("storename", "")).child(date).child(item.getKey()).setValue(model);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        time = format2.format(calendar.getTime());
        initFirebase();
    }

    public void initFirebase() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());


        String time2 = format2.format(calendar.getTime());
        Log.d("daon_test", "time = "+time);
        Log.d("daon_test", "store = "+pref.getString("storename", ""));
        FirebaseDatabase.getInstance().getReference().child("order").child(pref.getString("storename", "")).child(time).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    PrintOrderModel printOrderModel = item.getValue(PrintOrderModel.class);
                    if (printOrderModel.getPrintStatus().equals("x")) {
                        printOrderModel.setPrintStatus("o");
                        FirebaseDatabase.getInstance().getReference().child("order").child(pref.getString("storename", "")).child(time).child(item.getKey()).setValue(printOrderModel);
                        if (printOrderModel.getTime().equals(printDate)) {
                            if (printOrderModel.getTable().equals(printTable)){

                            }else {
                                print(printOrderModel);
                            }
                        }else{
                            print(printOrderModel);
                        }
//                        FirebaseDatabase.getInstance().getReference().child("order").child(pref.getString("storename", "")).child(time).child(item.getKey()).setValue(printOrderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                print(printOrderModel);
//                            }
//                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        FirebaseDatabase.getInstance().getReference().child("service").child(pref.getString("storename", "")).child(time).addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot item : snapshot.getChildren()) {
//                    PrintOrderModel printOrderModel = item.getValue(PrintOrderModel.class);
//                    if (printOrderModel.getPrintStatus().equals("x")) {
//                        print(printOrderModel);
//                        printOrderModel.setPrintStatus("o");
//                        FirebaseDatabase.getInstance().getReference().child("service").child(pref.getString("storename", "")).child(time).child(item.getKey()).setValue(printOrderModel);
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_frame);

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infos = manager.getRunningTasks(1);
        ComponentName name = infos.get(0).topActivity;
        String topActivityName = name.getShortClassName().substring(1);
        Log.d("topName", "onBackPressed: " + topActivityName);
        String fragmentName = fragment.toString().substring(0, fragment.toString().lastIndexOf("{"));
        Log.d("fragName", "onBackPressed: " + fragmentName);

        if (fragmentName.equals("MainActivity")) {
            Log.d("DPDP", "onBackPressed: ");
            return;
        } else {
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_frame, mainFragment).commit();
            Log.d("gse", "after onBackPressed: " + fragment.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infos = manager.getRunningTasks(1);
        ComponentName name = infos.get(0).topActivity;
        String topActivityName = name.getShortClassName().substring(1);
        Log.d("topName", "onResume: " + topActivityName);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infos = manager.getRunningTasks(1);
        ComponentName name = infos.get(0).topActivity;
        String topActivityName = name.getShortClassName().substring(1);
        Log.d("topName", "onPause: " + topActivityName);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infos = manager.getRunningTasks(1);
        ComponentName name = infos.get(0).topActivity;
        String topActivityName = name.getShortClassName().substring(1);
        Log.d("topName", "onStop: " + topActivityName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infos = manager.getRunningTasks(1);
        ComponentName name = infos.get(0).topActivity;
        String topActivityName = name.getShortClassName().substring(1);
        Log.d("topName", "onDestroy: " + topActivityName);
    }

    public void setPayment(String amount, String type, String vanTr, String cardNo, String prevAuthNum, String prevAuthDate) {
        HashMap<String, byte[]> paymentHash = new HashMap<String, byte[]>();
        Log.d("daon_test", "amount = "+amount);
        // ?????? ????????????
        paymentHash.put("TelegramType", "0200".getBytes());                                 // ?????? ?????? ,  ??????(0200) ??????(0420)
        paymentHash.put("DPTID", "AT0298903A".getBytes());                                  // ??????????????? , ????????????????????? DPT0TEST03
        paymentHash.put("PosEntry", "S".getBytes());                                        // Pos Entry Mode , ??????????????? ?????? ??? ?????????????????? 'K'??????
        paymentHash.put("PayType", "00".getBytes());                                        // [??????]???????????????(default '00') [??????]???????????????
        paymentHash.put("TotalAmount", getStrMoneyAmount(amount));                          // ?????????
        paymentHash.put("Amount", getStrMoneyAmount(amount));                               // ???????????? = ????????? - ????????? - ?????????
        paymentHash.put("ServiceAmount" ,getStrMoneyAmount(amount));                        // ?????????
        paymentHash.put("TaxAmount", getStrMoneyAmount("0"));                               // ?????????
        paymentHash.put("FreeAmount", getStrMoneyAmount("0"));                              // ?????? 0??????  / ?????? 1004?????? ?????? ????????? 1004??? ?????????(ServiceAmount),?????????(TaxAmount) 0??? ???????????? 1004???/ ??????(FreeAmount)  1004???
        paymentHash.put("AuthNum", "".getBytes());                                          // ????????? ???????????? , ??????????????? ??????
        paymentHash.put("Authdate", "".getBytes());                                         // ????????? ???????????? , ??????????????? ??????
        paymentHash.put("Filler", "".getBytes());                                           // ???????????? - ????????? ??????????????? ????????????
        paymentHash.put("SignTrans", "N".getBytes());                                       // ???????????? ??????, ?????????(N) 50000??? ????????? ?????? "N" => "S"?????? ??????
        if (Long.parseLong(amount) > 50000) {
            paymentHash.put("SignTrans", "S".getBytes());                                   // ???????????? ??????, ?????????(N) 50000??? ????????? ?????? "N" => "S"?????? ??????
        }

        paymentHash.put("PlayType", "D".getBytes());                                        // ????????????,  ??????????????? ?????????(D)
        paymentHash.put("CardType", "".getBytes());                                         // ???????????? ???????????? (?????? ????????????), "" ??????
        paymentHash.put("BranchNM", "".getBytes());                                         // ???????????? ,?????? ?????? ?????????????????? ?????? , ????????? "" ??????
        paymentHash.put("BIZNO", "135-88-01055".getBytes());                                // ??????????????? ,KSNET ?????? ????????? ????????????????????? ??????, ?????? ???"" ??????
        paymentHash.put("TransType", "".getBytes());                                        // "" ??????
        paymentHash.put("AutoClose_Time", "30".getBytes());                                 // ????????? ?????? ?????? ??? ?????? ?????? ex)30??? ??? ??????

        //?????? ????????????
//        paymentHash.put("SubBIZNO", "".getBytes());                                         // ?????? ??????????????? ,??????????????? ??????????????? ?????? ??? ????????? ??????
//        paymentHash.put("Device_PortName", "/dev/bus/usb/001/002".getBytes());              //????????? ?????? ?????? ?????? ??? UsbDevice ??????????????? getDeviceName() ??????????????? , ?????????????????? ????????????
//        paymentHash.put("EncryptSign", "A!B@C#D4".getBytes());                              // SignTrans "T"????????? KSCIC?????? ?????? ???????????? ?????? ?????????????????? ????????????, ??????????????????

        ComponentName compName = new ComponentName("ks.kscic_ksr01", " ks.kscic_ksr01.PaymentDlg");
//        Intent intent = new Intent(Intent.ACTION_MAIN);

        if (type.equals("credit")) {
            paymentHash.put("ReceiptNo", "X".getBytes());                                   // ??????????????? ????????????, ???????????? ??? "X", ??????????????? ??????????????? "", Key-In????????? "??????????????? ??? ??????" -> Pos Entry Mode 'K;
        } else if (type.equals("cancel")) {
            paymentHash.put("TelegramType", "0420".getBytes());                             // ?????? ?????? ,  ??????(0200) ??????(0420)
            paymentHash.put("ReceiptNo", "X".getBytes());                                   // ??????????????? ????????????, ???????????? ??? "X", ??????????????? ??????????????? "", Key-In????????? "??????????????? ??? ??????" -> Pos Entry Mode 'K;
            paymentHash.put("AuthNum", prevAuthDate.getBytes());
            paymentHash.put("AuthDate", prevAuthDate.getBytes());
        } else if (type.equals("cancelNoCard")) {
            paymentHash.put("TelegramType", "0420".getBytes());                             // ?????? ?????? ,  ??????(0200) ??????(0420)
            paymentHash.put("ReceiptNo", "X".getBytes());                                   // ??????????????? ????????????, ???????????? ??? "X", ??????????????? ??????????????? "", Key-In????????? "??????????????? ??? ??????" -> Pos Entry Mode 'K;
            paymentHash.put("VanTr", vanTr.getBytes());                                     // ?????????????????? , ????????? ????????? ?????? ?????? ??????
            paymentHash.put("CardBin", cardNo.getBytes());
            paymentHash.put("AuthNum", prevAuthNum.getBytes());
            paymentHash.put("AuthDate", prevAuthDate.getBytes());
            Log.d("HASH", "VanTr: " + paymentHash.get("VanTr").toString());
            Log.d("HASH", "CardBin: " + paymentHash.get("CardBin").toString());
            Log.d("HASH", "AuthNum: " + paymentHash.get("AuthNum").toString());
            Log.d("HASH", "AuthDate: " + paymentHash.get("AuthDate").toString());

        }
//        isPrinter isPrinter = new isPrinter();
//        Sam4sPrint sam4sPrint = new Sam4sPrint();
//
//        sam4sPrint = isPrinter.setPrinter2();
//
//
//        Sam4sBuilder builder = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);
//        try {
//            // top
//            builder.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
//            builder.addFeedLine(2);
//            builder.addTextBold(true);
//            builder.addTextSize(2,1);
//            builder.addText("????????????");
//            builder.addFeedLine(1);
//            builder.addTextBold(false);
//            builder.addTextSize(1,1);
//            builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
//            builder.addText("[?????????]");
//            builder.addFeedLine(1);
//            builder.addText(prevAuthDate);
//            builder.addFeedLine(1);
//            builder.addText("???????????? ?????????");
//            builder.addFeedLine(1);
//            builder.addText(" ?????????\t");
//            builder.addText("651-81-00773 \t");
//            builder.addText("Tel : 064-764-2334");
//            builder.addFeedLine(1);
//            builder.addText("????????????????????? ???????????? ????????? ?????????????????? 191");
//            builder.addFeedLine(1);
//            // body
//            builder.addText("------------------------------------------");
//            builder.addFeedLine(1);
//            builder.addText("TID:\t");
//            builder.addText("AT0296742A \t");
//            builder.addText("A-0000 \t");
//            builder.addText("0017");
//            builder.addFeedLine(1);
////            builder.addText("????????????: ");
////            builder.addTextSize(2,1);
////            builder.addTextBold(true);
////            builder.addText(prevCardNo);
//            builder.addTextSize(1,1);
//            builder.addTextBold(false);
//            builder.addFeedLine(1);
//            builder.addText("????????????: ");
//            builder.addText(paymentHash.get("CardBin").toString());
//            builder.addFeedLine(1);
//            builder.addTextPosition(0);
//            builder.addText("????????????: ");
//            builder.addText(prevAuthDate);
//            builder.addTextPosition(65535);
//            builder.addText("(?????????)");
//            builder.addFeedLine(1);
//            builder.addText("------------------------------------------");
//            builder.addFeedLine(2);
//            //menu
//            DecimalFormat myFormatter = new DecimalFormat("###,###");
//
//            builder.addText("------------------------------------------");
//            builder.addFeedLine(1);
//            // footer
//            builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
//            builder.addText("IC??????");
//            builder.addTextPosition(120);
////                builder.addText("???  ??? : ");
////                //builder.addTextPosition(400);
////                int a = (Integer.parseInt(price))/10;
////                builder.addText(myFormatter.format(a*9)+"???");
////                builder.addFeedLine(1);
////                builder.addText("DDC?????????");
////                builder.addTextPosition(120);
////                builder.addText("????????? : ");
////                builder.addText(myFormatter.format(a*1)+"???");
////                builder.addFeedLine(1);
////                builder.addTextPosition(120);
//            builder.addText("???  ??? : ");
//            builder.addTextSize(2,1);
//            builder.addTextBold(true);
//            builder.addText(myFormatter.format(Integer.parseInt(amount))+"???");
//            builder.addFeedLine(1);
//            builder.addTextSize(1,1);
//            builder.addTextPosition(120);
//            builder.addText("??????No : ");
//            builder.addTextBold(true);
//            builder.addTextSize(2,1);
//            builder.addText(prevAuthNum);
//            builder.addFeedLine(1);
//            builder.addTextBold(false);
//            builder.addTextSize(1,1);
////            builder.addText("???????????? : ");
////            builder.addText(prevCardNo);
//            builder.addFeedLine(1);
//            builder.addText("??????????????? : ");
//            builder.addText("AT0296742A");
//            builder.addFeedLine(1);
//            builder.addText("?????????????????? : ");
//            builder.addText(vanTr);
//            builder.addFeedLine(1);
//            builder.addText("------------------------------------------");
//            builder.addFeedLine(1);
//            builder.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
//            builder.addText("???????????????.");
//            builder.addCut(Sam4sBuilder.CUT_FEED);
//            Thread.sleep(200);
//            sam4sPrint.sendData(builder);
//            isPrinter.closePrint1(sam4sPrint);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(compName);
        intent.putExtra("AdminHash", paymentHash);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            HashMap<String, String> paymentHash = (HashMap<String, String>) data.getSerializableExtra("result");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (paymentHash != null) {
                prevAuthNum = paymentHash.get("AuthNum");
                prevAuthDate = paymentHash.get("Authdate");

                vanTr = paymentHash.get("VanTr");
                cardNo = paymentHash.get("cardNo");

            }
        } else if (resultCode == RESULT_FIRST_USER && data != null) {
            //??????????????????IC ???????????? ?????? ????????? ???????????? ?????? ?????? ??????
            //Toast.makeText(this, "??????????????????IC ?????? ????????? ???????????? ??? ??????????????? ????????????", Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(this, "????????? ?????? ??????", Toast.LENGTH_LONG).show();
        }
        // ????????? ????????? ?????? ?????? ??????
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "??? ?????? ??????", Toast.LENGTH_LONG).show();
        }
    }

    public byte[] getStrMoneyAmount(String money) {
        byte[] amount = null;
        if (money.length() == 0) {
            return "000000001004".getBytes();
        } else {
            Long longMoney = Long.parseLong(money.replace(",", ""));
            money = String.format("%012d", longMoney);
            amount = money.getBytes();
            return amount;
        }
    }
    public void print(PrintOrderModel printOrderModel) {
        isPrinter isPrinter = new isPrinter();
        Sam4sPrint sam4sPrint = new Sam4sPrint();
        Sam4sPrint sam4sPrint2 = new Sam4sPrint();
        Sam4sPrint sam4sPrint3 = new Sam4sPrint();
        Sam4sPrint sam4sPrint4 = new Sam4sPrint();
        Sam4sPrint sam4sPrint5 = new Sam4sPrint();
        Sam4sPrint sam4sPrint6 = new Sam4sPrint();
        Sam4sPrint sam4sPrint7 = new Sam4sPrint();
        sam4sPrint = isPrinter.setPrinter1();
        sam4sPrint2 = isPrinter.setPrinter2();
        sam4sPrint3 = isPrinter.setPrinter3();
        sam4sPrint4 = isPrinter.setPrinter4();
        sam4sPrint5 = isPrinter.setPrinter5();
        sam4sPrint6 = isPrinter.setPrinter6();
        sam4sPrint7 = isPrinter.setPrinter7();
        try {
            if (!sam4sPrint.getPrinterStatus().equals("Printer Ready")){
                for (int i = 0; i < 3; i++){
                    Thread.sleep(30);
                    if (sam4sPrint.getPrinterStatus().equals("Printer Ready")){
                        break;
                    }
                }
            }
            if (!sam4sPrint2.getPrinterStatus().equals("Printer Ready")){
                for (int i = 0; i < 3; i++){
                    Thread.sleep(30);
                    if (sam4sPrint2.getPrinterStatus().equals("Printer Ready")){
                        break;
                    }
                }
            }
            if (!sam4sPrint3.getPrinterStatus().equals("Printer Ready")){
                for (int i = 0; i < 3; i++){
                    Thread.sleep(30);
                    if (sam4sPrint3.getPrinterStatus().equals("Printer Ready")){
                        break;
                    }
                }
            }
            if (!sam4sPrint4.getPrinterStatus().equals("Printer Ready")){
                for (int i = 0; i < 3; i++){
                    Thread.sleep(30);
                    if (sam4sPrint4.getPrinterStatus().equals("Printer Ready")){
                        break;
                    }
                }
            }
            if (!sam4sPrint5.getPrinterStatus().equals("Printer Ready")){
                for (int i = 0; i < 3; i++){
                    Thread.sleep(30);
                    if (sam4sPrint5.getPrinterStatus().equals("Printer Ready")){
                        break;
                    }
                }
            }
            if (!sam4sPrint6.getPrinterStatus().equals("Printer Ready")){
                for (int i = 0; i < 3; i++){
                    Thread.sleep(30);
                    if (sam4sPrint6.getPrinterStatus().equals("Printer Ready")){
                        break;
                    }
                }
            }
            if (!sam4sPrint7.getPrinterStatus().equals("Printer Ready")){
                for (int i = 0; i < 3; i++){
                    Thread.sleep(30);
                    if (sam4sPrint7.getPrinterStatus().equals("Printer Ready")){
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int i_count1 = 0;
        int i_count2 = 0;
        int i_count3 = 0;
        int i_count4 = 0;

        String str_table = printOrderModel.getTable();
        printDate = printOrderModel.getTime();
        printTable = printOrderModel.getTable();
        Sam4sBuilder builder = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);
        Sam4sBuilder builder2 = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);
        Sam4sBuilder builder3 = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);
        Sam4sBuilder builder4 = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);
        Sam4sBuilder builder5 = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);
        Sam4sBuilder builder6 = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);
        Sam4sBuilder builder7 = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);
        ArrayList<OrderModel> order = printOrderModel.getOrder();
        if (printOrderModel.getType().equals("service")){
            str_table = str_table + "??? ??????";
        }else {
            if (printOrderModel.getOrderType().equals("cash")) {
                str_table = str_table + "??? ??????";
            } else {
                str_table = str_table + "??? ??????";
            }
        }


        try {
            builder.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
            builder.addFeedLine(1);
            builder.addTextSize(2,2);
            builder.addText("[?????????]");
            builder.addFeedLine(2);
            builder.addTextSize(2,2);
            builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder.addText("[?????????] ");
            builder.addText(str_table);
            builder.addFeedLine(1);
            builder.addTextSize(1,1);
            builder.addText("==========================================");
            builder.addFeedLine(1);
// body
            builder.addTextSize(2,2);
            builder.addTextPosition(0);
            builder.addTextBold(true);

            builder.addTextPosition(400);
            builder.addTextBold(false);
            builder.addFeedLine(1);
            if (printOrderModel.getType().equals("service")){
                builder.addTextSize(2, 2);
                builder.addText(order.get(0).getName());
                builder.addFeedLine(1);
                builder.addTextSize(1, 1);
                builder.addText("------------------------------------------");
                builder.addFeedLine(1);
                builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            }else {
                for (int i = 0; i < order.size(); i++) {

                    if (Integer.parseInt(order.get(i).getMenuNo()) > 7059 && Integer.parseInt(order.get(i).getMenuNo()) < 7085) {
                        builder.addTextSize(2, 2);
                        String strOpt = "";
                        if (order.get(i).getName().contains("??????????????? A")){
                            strOpt = "\n  ????????? ??????\n  ???????????????";
                        }else if (order.get(i).getName().contains("??????????????? B")){
                            strOpt = "\n  LA????????????(4???)\n  ???????????????\n  ??????";
                        }else if (order.get(i).getName().contains("??????????????? C")){
                            strOpt = "\n  ?????????????????????\n  ????????????\n  LA????????????(4???)\n  ???????????????";
                        }
                        builder.addText(order.get(i).getName()+strOpt);
                        builder.addFeedLine(1);
                        builder.addText("                 " + order.get(i).getCount() + "???");
                        builder.addFeedLine(1);
                        builder.addTextSize(1, 1);
                        builder.addText("------------------------------------------");
                        builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
                        i_count1++;
                    }

                }
            }
            builder.addFeedLine(1);
            builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder.addTextSize(1,1);
// footer
            builder.addText("==========================================");
            builder.addFeedLine(1);
            builder.addText("[????????????]");
            builder.addText(printOrderModel.getTime());
            builder.addFeedLine(2);
            builder.addCut(Sam4sBuilder.CUT_FEED);
            ///////////////////
            builder2.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
            builder2.addFeedLine(1);
            builder2.addTextSize(2,2);
            builder2.addText("[?????????]");
            builder2.addFeedLine(2);
            builder2.addTextSize(2,2);
            builder2.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder2.addText("[?????????] ");
            builder2.addText(str_table);
            builder2.addFeedLine(1);
            builder2.addTextSize(1,1);
            builder2.addText("==========================================");
            builder2.addFeedLine(1);

// body
            builder2.addTextSize(2,2);
            builder2.addTextPosition(0);
            builder2.addTextBold(true);

            builder2.addTextPosition(400);
            builder2.addTextBold(false);
            builder2.addFeedLine(1);
            if (printOrderModel.getType().equals("service")){
                builder2.addTextSize(2, 2);
                builder2.addText(order.get(0).getName());
                builder2.addFeedLine(1);
                builder2.addTextSize(1, 1);
                builder2.addText("------------------------------------------");
                builder2.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
                builder2.addFeedLine(1);
            }else {
                for (int i = 0; i < order.size(); i++) {
                    if (printOrderModel.getType().equals("order")) {
                        String strOpt = "";
                        if (order.get(i).getName().contains("??????????????? A")){
                            strOpt = "\n  ????????? ??????\n  ???????????????";
                        }else if (order.get(i).getName().contains("??????????????? B")){
                            strOpt = "\n  LA????????????(4???)\n  ???????????????\n  ??????";
                        }else if (order.get(i).getName().contains("??????????????? C")){
                            strOpt = "\n  ?????????????????????\n  ????????????\n  LA????????????(4???)\n  ???????????????";
                        }
                        builder2.addTextSize(2, 2);
                        builder2.addText(order.get(i).getName()+strOpt);
                        builder2.addFeedLine(1);
                        builder2.addText("                 " + order.get(i).getCount() + "???");
                        builder2.addFeedLine(1);
                        builder2.addTextSize(1, 1);
                        builder2.addText("------------------------------------------");
                        builder2.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
                    }
                }
            }
            builder2.addFeedLine(1);
            builder2.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder2.addTextSize(1,1);
// footer
            builder2.addText("==========================================");
            builder2.addFeedLine(1);
            builder2.addText("[????????????]");
            builder2.addText(printOrderModel.getTime());
            builder2.addFeedLine(2);
            builder2.addCut(Sam4sBuilder.CUT_FEED);

            ////////////////////////////

            builder3.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
            builder3.addFeedLine(1);
            builder3.addTextSize(2,2);
            builder3.addText("[?????????]");
            builder3.addFeedLine(2);
            builder3.addTextSize(2,2);
            builder3.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder3.addText("[?????????] ");
            builder3.addText(str_table);
            builder3.addFeedLine(1);
            builder3.addTextSize(1,1);
            builder3.addText("==========================================");
            builder3.addFeedLine(1);
// body
            builder3.addTextSize(2,2);
            builder3.addTextPosition(0);
            builder3.addTextBold(true);

            builder3.addTextPosition(400);
            builder3.addTextBold(false);
            builder3.addFeedLine(1);

                for (int i = 0; i < order.size(); i++) {
                    if (printOrderModel.getType().equals("order")) {

                        if ((Integer.parseInt(order.get(i).getMenuNo()) > 7000 && Integer.parseInt(order.get(i).getMenuNo()) < 7060)) {
                            if (!order.get(i).getName().equals("?????????")) {
                                String strOpt = "";
                                if (order.get(i).getName().contains("??????????????? A")){
                                    strOpt = "\n  ????????? ??????\n  ???????????????";
                                }else if (order.get(i).getName().contains("??????????????? B")){
                                    strOpt = "\n  LA????????????(4???)\n  ???????????????\n  ??????";
                                }else if (order.get(i).getName().contains("??????????????? C")){
                                    strOpt = "\n  ?????????????????????\n  ????????????\n  LA????????????(4???)\n  ???????????????";
                                }
                                builder3.addTextSize(2, 2);
                                builder3.addText(order.get(i).getName()+strOpt);
                                builder3.addFeedLine(1);
                                builder3.addText("                 " + order.get(i).getCount() + "???");
                                builder3.addFeedLine(1);
                                builder3.addTextSize(1, 1);
                                builder3.addText("------------------------------------------");
                                builder3.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
                                i_count2++;
                            }
                        }
                    }

                }

            builder3.addFeedLine(1);
            builder3.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder3.addTextSize(1,1);
// footer
            builder3.addText("==========================================");
            builder3.addFeedLine(1);
            builder3.addText("[????????????]");
            builder3.addText(printOrderModel.getTime());
            builder3.addFeedLine(2);
            builder3.addCut(Sam4sBuilder.CUT_FEED);
            /////////////////////////////////////////
            builder4.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
            builder4.addFeedLine(1);
            builder4.addTextSize(2,2);
            builder4.addText("[?????????]");
            builder4.addFeedLine(2);
            builder4.addTextSize(2,2);
            builder4.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder4.addText("[?????????] ");
            builder4.addText(str_table);
            builder4.addFeedLine(1);
            builder4.addTextSize(1,1);
            builder4.addText("==========================================");
            builder4.addFeedLine(1);

// body
            builder4.addTextSize(2,2);
            builder4.addTextPosition(0);
            builder4.addTextBold(true);

            builder4.addTextPosition(400);
            builder4.addTextBold(false);
            builder4.addFeedLine(2);

            for (int i = 0; i < order.size(); i++) {
                if (printOrderModel.getType().equals("order")) {

                    if (Integer.parseInt(order.get(i).getMenuNo()) == 7004 || Integer.parseInt(order.get(i).getMenuNo()) == 7005
                    || Integer.parseInt(order.get(i).getMenuNo()) == 7058 || Integer.parseInt(order.get(i).getMenuNo()) == 7043
                            || Integer.parseInt(order.get(i).getMenuNo()) == 7059 || Integer.parseInt(order.get(i).getMenuNo()) == 7044) {
                        builder4.addTextSize(2, 2);
                        String strOpt = "";
                        if (order.get(i).getName().contains("??????????????? A")){
                            strOpt = "\n  ????????? ??????\n  ???????????????";
                        }else if (order.get(i).getName().contains("??????????????? B")){
                            strOpt = "\n  LA????????????(4???)\n  ???????????????\n  ??????";
                        }else if (order.get(i).getName().contains("??????????????? C")){
                            strOpt = "\n  ?????????????????????\n  ????????????\n  LA????????????(4???)\n  ???????????????";
                        }
                        builder4.addText(order.get(i).getName()+strOpt);
                        builder4.addFeedLine(1);
                        builder4.addText("                 " + order.get(i).getCount() + "???");
                        builder4.addFeedLine(1);
                        builder4.addTextSize(1, 1);
                        builder4.addText("------------------------------------------");
                        builder4.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
                        i_count3++;
                    }
                }

            }

            builder4.addFeedLine(1);
            builder4.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder4.addTextSize(1,1);
// footer
            builder4.addText("==========================================");
            builder4.addFeedLine(1);
            builder4.addText("[????????????]");
            builder4.addText(printOrderModel.getTime());
            builder4.addFeedLine(2);
            builder4.addCut(Sam4sBuilder.CUT_FEED);


            ////////////////////////////////////////////////////////
            builder5.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
            builder5.addFeedLine(1);
            builder5.addTextSize(2,2);
            builder5.addText("[?????????]");
            builder5.addFeedLine(2);
            builder5.addTextSize(2,2);
            builder5.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder5.addText("[?????????] ");
            builder5.addText(str_table);
            builder5.addFeedLine(1);
            builder5.addTextSize(1,1);
            builder5.addText("==========================================");
            builder5.addFeedLine(1);
// body
            builder5.addTextSize(2,2);
            builder5.addTextPosition(0);
            builder5.addTextBold(true);

            builder5.addTextPosition(400);
            builder5.addTextBold(false);
            builder5.addFeedLine(1);
            if (printOrderModel.getType().equals("service")){
                builder5.addTextSize(2, 2);
                builder5.addText(order.get(0).getName());
                builder5.addFeedLine(1);
                builder5.addTextSize(1, 1);
                builder5.addText("------------------------------------------");
                builder5.addFeedLine(1);
                builder5.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            }else {
                for (int i = 0; i < order.size(); i++) {

                    if (Integer.parseInt(order.get(i).getMenuNo()) > 7084 && Integer.parseInt(order.get(i).getMenuNo()) < 7110 ) {
                        builder5.addTextSize(2, 2);
                        String strOpt = "";
                        if (order.get(i).getName().contains("??????????????? A")){
                            strOpt = "\n  ????????? ??????\n  ???????????????";
                        }else if (order.get(i).getName().contains("??????????????? B")){
                            strOpt = "\n  LA????????????(4???)\n  ???????????????\n  ??????";
                        }else if (order.get(i).getName().contains("??????????????? C")){
                            strOpt = "\n  ?????????????????????\n  ????????????\n  LA????????????(4???)\n  ???????????????";
                        }
                        builder5.addText(order.get(i).getName()+strOpt);
                        builder5.addFeedLine(1);
                        builder5.addText("                 " + order.get(i).getCount() + "???");
                        builder5.addFeedLine(1);
                        builder5.addTextSize(1, 1);
                        builder5.addText("------------------------------------------");
                        builder5.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
                        i_count4++;
                    }

                }
            }
            builder5.addFeedLine(1);
            builder5.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder5.addTextSize(1,1);
// footer
            builder5.addText("==========================================");
            builder5.addFeedLine(1);
            builder5.addText("[????????????]");
            builder5.addText(printOrderModel.getTime());
            builder5.addFeedLine(2);
            builder5.addCut(Sam4sBuilder.CUT_FEED);

            if (printOrderModel.getType().equals("order")) {
                if (i_count1 > 0) {
                    sam4sPrint.sendData(builder);
                    sam4sPrint2.sendData(builder);
                    sam4sPrint3.sendData(builder);
                }
                if (i_count2 > 0){
                    sam4sPrint5.sendData(builder3);
                    sam4sPrint6.sendData(builder3);
                    sam4sPrint4.sendData(builder3);
                }
                if (i_count3 > 0){
                    sam4sPrint4.sendData(builder4);
                }
                if (i_count4 > 0){
                    sam4sPrint4.sendData(builder5);
                    sam4sPrint5.sendData(builder5);
                }
                sam4sPrint7.sendData(builder2);
//                sam4sPrint5.sendData(builder);
//                sam4sPrint6.sendData(builder);
//                sam4sPrint7.sendData(builder);
//                if (i_count2 > 0) {
//                    sam4sPrint4.sendData(builder2);
//
//                }
//                if (i_count3 > 0) {
//                    sam4sPrint3.sendData(builder3);
//                }

            } else {
                sam4sPrint.sendData(builder);
                sam4sPrint2.sendData(builder);
                sam4sPrint3.sendData(builder);
                sam4sPrint7.sendData(builder);

            }
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.bell);
            mp.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
        builder.clearCommandBuffer();
        builder2.clearCommandBuffer();
        builder3.clearCommandBuffer();
        builder4.clearCommandBuffer();
        isPrinter.closePrint1(sam4sPrint);
        isPrinter.closePrint2(sam4sPrint2);
        isPrinter.closePrint3(sam4sPrint3);
        isPrinter.closePrint4(sam4sPrint4);
        isPrinter.closePrint5(sam4sPrint5);
        isPrinter.closePrint6(sam4sPrint6);
        isPrinter.closePrint7(sam4sPrint7);


    }


    public void rePrint(String orderTime, String authdate, String authnum, String orderbody, String table, String price, String vanTr, String cardbin){
        isPrinter isPrinter = new isPrinter();
        Sam4sPrint sam4sPrint = new Sam4sPrint();

        sam4sPrint = isPrinter.setPrinter7();
        try {
            if (!sam4sPrint.getPrinterStatus().equals("Printer Ready")){
                for (int i = 0; i < 3; i++){
                    Thread.sleep(30);
                    if (sam4sPrint.getPrinterStatus().equals("Printer Ready")){
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Sam4sBuilder builder = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);

        try {
            builder.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
            builder.addFeedLine(2);
            builder.addTextBold(true);
            builder.addTextSize(2, 1);
            builder.addText("????????????");
            builder.addFeedLine(1);
            builder.addTextBold(false);
            builder.addTextSize(1, 1);
            builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder.addText("[?????????]");
            builder.addFeedLine(1);
            builder.addText(prevAuthDate);
            builder.addFeedLine(1);
            builder.addText("?????????");
            builder.addFeedLine(1);
            builder.addText("????????? \t");
            builder.addText("595-45-00339 \t");
            builder.addText("Tel : 064-909-2203");
            builder.addFeedLine(1);
            builder.addText("????????????????????? ???????????? ????????? ???????????????304?????? 98, F004???");
            builder.addFeedLine(1);

            builder.addText("------------------------------------------");
            builder.addFeedLine(1);
            builder.addText("TID:\t");
            builder.addText("AT0298903A \t");
            builder.addText("A-0000 \t");
            builder.addText("0017");
            builder.addFeedLine(1);
//            builder.addText("????????????: ");
            builder.addTextSize(2, 1);
            builder.addTextBold(true);
//            builder.addText(printOrderModel.getCardname());
            builder.addTextSize(1, 1);
            builder.addTextBold(false);
            builder.addFeedLine(1);
            builder.addText("????????????: ");
            builder.addText(cardbin);
            builder.addFeedLine(1);
            builder.addTextPosition(0);
            builder.addText("????????????: ");
            builder.addText(authdate);
            builder.addTextPosition(65535);
            builder.addText("(?????????)");
            builder.addFeedLine(1);
            builder.addText("------------------------------------------");
            builder.addFeedLine(2);
            //menu
            DecimalFormat myFormatter = new DecimalFormat("###,###");

            builder.addText(orderbody);
            builder.addFeedLine(1);
//            for (int i = 0; i < orderArr.length; i++) {
//                String arrOrder = orderArr[i];
//                String[] subOrder = arrOrder.split("##");
//                builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
//                builder.addText(subOrder[0]);
////                    builder.addText(subOrder[1]);
//                builder.addFeedLine(1);
//                builder.addTextAlign(Sam4sBuilder.ALIGN_RIGHT);
////                    builder.addText(subOrder[2]);
////                builder.addFeedLine(2);
//            }
            builder.addText("------------------------------------------");
            builder.addFeedLine(1);
            // footer
            builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder.addText("IC??????");
            builder.addTextPosition(120);
            builder.addText("???  ??? : ");
            //builder.addTextPosition(400);
            int a = (Integer.parseInt(price)) / 10;
            builder.addText(myFormatter.format(a * 9) + "???");
            builder.addFeedLine(1);
            builder.addText("DDC?????????");
            builder.addTextPosition(120);
            builder.addText("????????? : ");
            builder.addText(myFormatter.format(a * 1) + "???");
            builder.addFeedLine(1);
            builder.addTextPosition(120);
            builder.addText("???  ??? : ");
            builder.addTextSize(2, 1);
            builder.addTextBold(true);
            builder.addText(myFormatter.format(Integer.parseInt(price)) + "???");
            builder.addFeedLine(1);
            builder.addTextSize(1, 1);
            builder.addTextPosition(120);
            builder.addText("??????No : ");
            builder.addTextBold(true);
            builder.addTextSize(2, 1);
            builder.addText(authnum);
            builder.addFeedLine(1);
            builder.addTextBold(false);
            builder.addTextSize(1, 1);
//            builder.addText("???????????? : ");
//            builder.addText(printOrderModel.getNotice());
            builder.addFeedLine(1);
            builder.addText("??????????????? : ");
            builder.addText("AT0298903A");
            builder.addFeedLine(1);
            builder.addText("?????????????????? : ");
            builder.addText(vanTr);
            builder.addFeedLine(1);
            builder.addText("------------------------------------------");
            builder.addFeedLine(1);
            builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
            builder.addTextSize(2, 2);
            builder.addText("??????????????? : "+table);
            builder.addFeedLine(2);
            builder.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
            builder.addTextSize(1, 1);
            builder.addText("???????????????.");
            builder.addCut(Sam4sBuilder.CUT_FEED);
            new MyThread(builder, sam4sPrint).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
//        builder.clearCommandBuffer();
//        isPrinter.closePrint7(sam4sPrint);

    }
    public void test(){
        Intent intent = new Intent(MainActivity.this, testActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    class MyThread extends Thread {

        private Sam4sBuilder builder;
        private Sam4sPrint sam4sPrint;

        public MyThread(Sam4sBuilder builder, Sam4sPrint printer) {
            this.builder = builder;
            this.sam4sPrint = printer;
        }

        @Override
        public void run() {
            try {
                sam4sPrint.sendData(builder);
                sleep(1000);
                sam4sPrint.closePrinter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class PrintThread extends Thread {

        private Sam4sBuilder builder;
        private Sam4sPrint sam4sPrint;

        public PrintThread(Sam4sBuilder builder, Sam4sPrint printer) {
            this.builder = builder;
            this.sam4sPrint = printer;
        }

        @Override
        public void run() {
            try {
                sam4sPrint.sendData(builder);
                sleep(1000);
                sam4sPrint.closePrinter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
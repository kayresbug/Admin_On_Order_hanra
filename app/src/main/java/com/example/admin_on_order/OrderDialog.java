package com.example.admin_on_order;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.admin_on_order.Fragment.OrderFragment;
import com.example.admin_on_order.model.OrderModel;
import com.google.gson.JsonObject;
import com.sam4s.printer.Sam4sBuilder;
import com.sam4s.printer.Sam4sPrint;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderDialog extends Dialog {
    private static String BASE_URL = "http://15.164.232.164:5000/";
    private TextView orderBody;
    private TextView orderTableNo;
    private TextView orderTotalPrice;
    private Button orderClose;
    private TextView orderOk, orderCancel, orderReissue, orderCancelPayment;
    private String paymentStatus = "";
    private String paymentType = "";
    private String orderTime = "";
    private String authNum = "";
    private String authDate = "";
    private String vanTr = "";
    private String cardBin = "";
    private String dptId = "";
    MainActivity mainActivity;
    Context context;


    public OrderDialog(@NonNull Context context, String body, String tableNo, String totalPrice, String paymentStatus,
                       String paymentType, String orderTime, String authNum, String authDate, String vanTr, String cardBin, String dptId) {
        super(context);
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
        this.orderTime = orderTime;
        this.authNum = authNum;
        this.authDate = authDate;
        this.vanTr = vanTr;
        this.cardBin = cardBin;
        this.dptId = dptId;
        this.body = body;
        this.tableNo = tableNo;
        this.totalPrice = totalPrice;
        this.context = context;
    }

    String body;
    String tableNo;
    String totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = layoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.order_dialog);

        orderBody = findViewById(R.id.order_dialog_body);
        orderBody.setText(body);

        orderTableNo = findViewById(R.id.order_dialog_table_no);
        orderTableNo.setText(tableNo + " 번 테이블");

        orderTotalPrice = findViewById(R.id.order_dialog_total_price);
        DecimalFormat decimalFormat = new DecimalFormat("###,###");

        orderTotalPrice.setText(decimalFormat.format(Integer.parseInt(totalPrice)) + "원");

        orderClose = findViewById(R.id.order_dialog_close);
        orderClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        orderOk = findViewById(R.id.btn_order_dialog_ok);
        orderOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        orderCancel = findViewById(R.id.btn_order_dialog_cancel);
        orderCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        orderReissue = findViewById(R.id.btn_order_dialog_reissue);
        orderReissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity)getContext()).rePrint(orderTime, authDate, authNum, body, tableNo, totalPrice, vanTr);
//                ((MainActivity)getContext()).test();
                mainActivity = new MainActivity();
                mainActivity.rePrint(orderTime, authDate, authNum, body, tableNo, totalPrice, vanTr, cardBin);
                dismiss();

            }
        });

        orderCancelPayment = findViewById(R.id.btn_order_dialog_pay_cancel);
        orderCancelPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity)getContext()).setPayment(orderTotalPrice.toString(), "cancelNoCard", vanTr, cardBin, authNum, authDate);
//                mainActivity = new MainActivity();
//                OrderFragment orderFragment = new OrderFragment();
//                mainActivity.setPayment(totalPrice, "cancelNoCard", vanTr, cardBin, authNum, authDate);
//                orderFragment.test();
                Long tsLong = System.currentTimeMillis();
                String ts = tsLong.toString();
                if (cardBin.equals("Cash")) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(new NullOnEmptyConverterFactory())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    InterfaceAPI interfaceAPI = retrofit.create(InterfaceAPI.class);
                    interfaceAPI.postOrder("shhl", tableNo, "", totalPrice, "1",
                            totalPrice, "nCash", body, ts,
                            "c_"+authNum, "  ", "  ", "  ").enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {

                        }
                    });
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(new NullOnEmptyConverterFactory())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    interfaceAPI = retrofit.create(InterfaceAPI.class);
                    interfaceAPI.payment("shhl", "",
                            "", "", "",
                            "", "O", authDate, "",
                            "", "c_"+authNum, "",
                            "", "", "", "",
                            "", "", "", "",
                            "", "", "",
                            "nCash", "", "", "",
                            "", String.valueOf(totalPrice)).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }else if(cardBin.equals("nCash")) {
                    dismiss();
                }else {
                    isPrinter isPrinter = new isPrinter();
                    Sam4sPrint sam4sPrint4 = new Sam4sPrint();
                    Sam4sPrint sam4sPrint5 = new Sam4sPrint();
                    sam4sPrint4 = isPrinter.setPrinter4();
                    sam4sPrint5 = isPrinter.setPrinter5();
                    try {

                        if (!sam4sPrint4.getPrinterStatus().equals("Printer Ready")) {
                            for (int i = 0; i < 3; i++) {
                                Thread.sleep(100);
                                if (sam4sPrint4.getPrinterStatus().equals("Printer Ready")) {
                                    break;
                                }
                            }
                        }
                        if (!sam4sPrint5.getPrinterStatus().equals("Printer Ready")) {
                            for (int i = 0; i < 3; i++) {
                                Thread.sleep(100);
                                if (sam4sPrint5.getPrinterStatus().equals("Printer Ready")) {
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


                    Sam4sBuilder builder = new Sam4sBuilder("ELLIX30", Sam4sBuilder.LANG_KO);


                    try {
                        builder.addTextAlign(Sam4sBuilder.ALIGN_CENTER);
                        builder.addFeedLine(1);
                        builder.addTextSize(2, 2);
                        builder.addText("[취소주문서]");
                        builder.addFeedLine(2);
                        builder.addTextSize(2, 2);
                        builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
                        builder.addText("[테이블] ");
                        builder.addText(tableNo + "번 취소");
                        builder.addFeedLine(1);
                        builder.addTextSize(1, 1);
                        builder.addText("==========================================");
// body
                        builder.addTextSize(2, 2);
                        builder.addTextPosition(0);
                        builder.addTextBold(true);

                        builder.addTextPosition(400);
                        builder.addTextBold(false);
                        builder.addFeedLine(1);
                        builder.addText(orderBody.getText().toString());
//                    if (printOrderModel.getType().equals("service")){
//                        builder.addTextSize(2, 2);
//                        builder.addText(order.get(0).getName());
//                        builder.addFeedLine(1);
//                        builder.addTextSize(1, 1);
//                        builder.addText("------------------------------------------");
//                        builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
//                    }else {
//                        for (int i = 0; i < order.size(); i++) {
//                            if (Integer.parseInt(order.get(i).getMenuNo()) > 7059) {
//                                builder.addTextSize(2, 2);
//                                builder.addText(order.get(i).getName());
//                                builder.addFeedLine(1);
//                                builder.addText("                 " + order.get(i).getCount() + "개");
//                                builder.addFeedLine(1);
//                                builder.addTextSize(1, 1);
//                                builder.addText("------------------------------------------");
//                                builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
//                                i_count1++;
//                            }
//
//                        }
//                    }
                        builder.addFeedLine(1);
                        builder.addTextAlign(Sam4sBuilder.ALIGN_LEFT);
                        builder.addTextSize(1, 1);
// footer
                        builder.addText("==========================================");
                        builder.addFeedLine(1);
                        builder.addText("[주문시간]");
                        builder.addText(orderTime);
                        builder.addFeedLine(2);
                        builder.addCut(Sam4sBuilder.CUT_FEED);
                        new MyThread(builder, sam4sPrint4).start();
                        new MyThread(builder, sam4sPrint5).start();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(context, testActivity.class);
                    intent.putExtra("totalprice", totalPrice);
                    intent.putExtra("type", "cancelNoCard");
                    intent.putExtra("vantr", vanTr);
                    intent.putExtra("cardbin", cardBin);
                    intent.putExtra("authnum", authNum);
                    intent.putExtra("authdate", authDate);
                    context.startActivity(intent);
                }
            }
        });

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
}
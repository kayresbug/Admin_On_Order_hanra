package com.example.admin_on_order;


import android.util.Log;

import com.sam4s.printer.Sam4sPrint;

public class isPrinter {
    public Sam4sPrint setPrinter1(){
        Sam4sPrint sam4sPrint = new Sam4sPrint();
        try {
            sam4sPrint.openPrinter(Sam4sPrint.DEVTYPE_ETHERNET, "192.168.20.130", 9100);
            sam4sPrint.resetPrinter();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return sam4sPrint;
    }
    public Sam4sPrint setPrinter2(){
        Sam4sPrint sam4sPrint = new Sam4sPrint();
        try {
            sam4sPrint.openPrinter(Sam4sPrint.DEVTYPE_ETHERNET, "192.168.20.131", 9100);
            sam4sPrint.resetPrinter();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return sam4sPrint;
    }

    public Sam4sPrint setPrinter3(){
        Sam4sPrint sam4sPrint = new Sam4sPrint();
        try {
            sam4sPrint.openPrinter(Sam4sPrint.DEVTYPE_ETHERNET, "192.168.20.132", 9100);
            sam4sPrint.resetPrinter();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return sam4sPrint;
    }

    public Sam4sPrint setPrinter4(){
        Sam4sPrint sam4sPrint = new Sam4sPrint();
        try {
            sam4sPrint.openPrinter(Sam4sPrint.DEVTYPE_ETHERNET, "192.168.20.133", 9100);
            sam4sPrint.resetPrinter();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return sam4sPrint;
    }
    public Sam4sPrint setPrinter5(){
        Sam4sPrint sam4sPrint = new Sam4sPrint();
        try {
            sam4sPrint.openPrinter(Sam4sPrint.DEVTYPE_ETHERNET, "192.168.20.134", 9100);
            sam4sPrint.resetPrinter();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return sam4sPrint;
    }
    public Sam4sPrint setPrinter6(){
        Sam4sPrint sam4sPrint = new Sam4sPrint();
        try {
            sam4sPrint.openPrinter(Sam4sPrint.DEVTYPE_ETHERNET, "192.168.20.135", 9100);
            sam4sPrint.resetPrinter();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return sam4sPrint;
    }
    public Sam4sPrint setPrinter7(){
        Sam4sPrint sam4sPrint = new Sam4sPrint();
        try {
            sam4sPrint.openPrinter(Sam4sPrint.DEVTYPE_ETHERNET, "192.168.20.136", 9100);
            sam4sPrint.resetPrinter();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return sam4sPrint;
    }

    public void closePrint1(Sam4sPrint sam4sPrint){
//        try {
//            Thread.sleep(600);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        sam4sPrint.closePrinter();

    }
    public void closePrint2(Sam4sPrint sam4sPrint){
//        try {
//            Thread.sleep(600);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        sam4sPrint.closePrinter();

    }
    public void closePrint3(Sam4sPrint sam4sPrint){
//        try {
//            Thread.sleep(600);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        sam4sPrint.closePrinter();

    }
    public void closePrint4(Sam4sPrint sam4sPrint){
//        try {
//            Thread.sleep(600);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        sam4sPrint.closePrinter();

    }
    public void closePrint5(Sam4sPrint sam4sPrint){
//        try {
//            Thread.sleep(600);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        sam4sPrint.closePrinter();

    }
    public void closePrint6(Sam4sPrint sam4sPrint){
//        try {
//            Thread.sleep(600);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        sam4sPrint.closePrinter();

    }
    public void closePrint7(Sam4sPrint sam4sPrint){
//        try {
//            Thread.sleep(600);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        sam4sPrint.closePrinter();

    }
}

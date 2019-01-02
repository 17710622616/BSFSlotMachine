package com.bs.john_li.bsfmerchantsversionapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfmerchantsversionapp.Model.CommonModel;
import com.bs.john_li.bsfmerchantsversionapp.Model.QrcodeReturnModel;
import com.bs.john_li.bsfmerchantsversionapp.Model.SellerOrderDetialOutModel;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFCommonUtils;
import com.bs.john_li.bsfmerchantsversionapp.Utils.BSFMerchantConfigtor;
import com.bs.john_li.bsfmerchantsversionapp.Utils.SPUtils;
import com.google.gson.Gson;
import com.sunmi.peripheral.printer.ICallback;
import com.sunmi.peripheral.printer.ILcdCallback;
import com.sunmi.peripheral.printer.ITax;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallbcak;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.TransBean;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by John on 18/11/2018.
 */

public class OrderCheckActivity extends FragmentActivity implements View.OnClickListener{
    private QrcodeReturnModel qrcodeReturnModel;
    private ImageView leftIv;
    private TextView orderNoTv, couponNoTv, checkOrderTv;

    private SellerOrderDetialOutModel.DataBean.SellerOrderBean mSellerOrderDetialModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_check);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        leftIv = findViewById(R.id.order_check_left_iv);
        orderNoTv = findViewById(R.id.order_check_orderno);
        couponNoTv = findViewById(R.id.order_check_couponno);
        checkOrderTv = findViewById(R.id.check_order_tv);
    }

    private void setListener() {
        leftIv.setOnClickListener(this);
        checkOrderTv.setOnClickListener(this);
    }

    private void initData() {
        Binding();
        try {
            InnerPrinterManager.getInstance().bindService(OrderCheckActivity.this, innerPrinterCallback);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }

        qrcodeReturnModel = new Gson().fromJson(getIntent().getStringExtra("scanReturnStr"), QrcodeReturnModel.class);
        orderNoTv.setText("訂單編號：" + qrcodeReturnModel.getOrderNo());
        couponNoTv.setText("券        碼：" + qrcodeReturnModel.getCouponCode());
        callNetGetOrderDetial();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_check_left_iv:
                finish();
                break;
            case R.id.check_order_tv:
                submitCheckOrder();
                break;
        }
    }

    private void callNetGetOrderDetial() {
        RequestParams params = new RequestParams(BSFMerchantConfigtor.BASE_URL + BSFMerchantConfigtor.SELLER_ORDER_DETIAL);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("orderNo",qrcodeReturnModel.getOrderNo());
            jsonObj.put("sellerToken", SPUtils.get(this, "SellerUserToken", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SellerOrderDetialOutModel model = new Gson().fromJson(result.toString(), SellerOrderDetialOutModel.class);
                if (model.getCode() == 200) {
                    mSellerOrderDetialModel = model.getData().getSellerOrder();
                } else {
                    Toast.makeText(OrderCheckActivity.this, "獲取訂單詳情失敗！" + String.valueOf(model.getMsg()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof java.net.SocketTimeoutException) {
                    Toast.makeText(OrderCheckActivity.this, "網絡連接超時，請重試", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderCheckActivity.this, "獲取訂單詳情失敗，請重新提交", Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {

            }
        });
    }


    private void submitCheckOrder() {
        RequestParams params = new RequestParams(BSFMerchantConfigtor.BASE_URL + BSFMerchantConfigtor.SELLER_FINISH_ORDER);
        params.setAsJsonContent(true);
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("orderNo",qrcodeReturnModel.getOrderNo());
            jsonObj.put("sellerToken", SPUtils.get(this, "SellerUserToken", ""));
            jsonObj.put("couponCode", qrcodeReturnModel.getCouponCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String urlJson = jsonObj.toString();
        params.setBodyContent(urlJson);
        params.setConnectTimeout(30 * 1000);
        x.http().request(HttpMethod.POST ,params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonModel model = new Gson().fromJson(result.toString(), CommonModel.class);
                if (model.getCode() == 200) {
                    try {
                        String printDetial = null;
                        if (mSellerOrderDetialModel != null) {
                            printDetial = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + mSellerOrderDetialModel.getSellerName() + "\n訂單編號：" + qrcodeReturnModel.getOrderNo()  + "\n套餐名稱：" + mSellerOrderDetialModel.getChargeRemark()+ "\n券      碼：" + qrcodeReturnModel.getCouponCode() + "\n支付金額：" + mSellerOrderDetialModel.getPayAmount() + "\n折扣金額：" + mSellerOrderDetialModel.getMoneyBack() + "\n創建時間：" + BSFCommonUtils.stampToDate(String.valueOf(mSellerOrderDetialModel.getCreateTime()))+ "\n\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t掌泊寶平台"+ "\n\n";
                        } else {
                            printDetial = "訂單內容\n" + "訂單編號：" + qrcodeReturnModel.getOrderNo() + "\n券        碼：" + qrcodeReturnModel.getCouponCode()+ "\n\n\n\n\n";
                        }
                        sunmiPrinterService.printText(printDetial + "\n\n\n", new InnerResultCallbcak() {
                            @Override
                            public void onRunResult(boolean isSuccess) throws RemoteException {
                                //返回接⼝执⾏的情况 成功/失败
                                Toast.makeText(OrderCheckActivity.this, "返回接⼝执⾏的情况 成功/失败", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onReturnString(String result) throws RemoteException {
                                //部分接⼝会异步返回查询数据
                                Toast.makeText(OrderCheckActivity.this, "部分接⼝会异步返回查询数据", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onRaiseException(int code, String msg) throws RemoteException {
                                //接⼝执⾏失败时，返回的异常状态
                                Toast.makeText(OrderCheckActivity.this, "接⼝执⾏失败时，返回的异常状态", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onPrintResult(int code, String msg) throws RemoteException {
                                //事务模式下的打印结果返回
                                Toast.makeText(OrderCheckActivity.this, "事务模式下的打印结果返回", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        //接⼝调⽤异常
                    }
                    Toast.makeText(OrderCheckActivity.this, "訂單消費成功！" + String.valueOf(model.getMsg().toString()), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(OrderCheckActivity.this, "訂單消費失敗！" + String.valueOf(model.getMsg().toString()), Toast.LENGTH_SHORT).show();
                }
            }
            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof java.net.SocketTimeoutException) {
                    Toast.makeText(OrderCheckActivity.this, "網絡連接超時，請重試", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderCheckActivity.this, "訂單消費失敗，請重新提交", Toast.LENGTH_SHORT).show();
                }
            }
            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {

            }
        });
    }

    //innerPrinterCallback即服务绑定回调
    InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback(){
        @Override
        protected void onConnected(SunmiPrinterService service) {

        }

        @Override
        protected void onDisconnected() {

        }
    };

    private void Binding(){
        Intent intent=new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            sunmiPrinterService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sunmiPrinterService = SunmiPrinterService.Stub.asInterface(service);
        }
    };


    SunmiPrinterService sunmiPrinterService = new SunmiPrinterService() {
        @Override
        public void updateFirmware() throws RemoteException {

        }

        @Override
        public int getFirmwareStatus() throws RemoteException {
            return 0;
        }

        @Override
        public String getServiceVersion() throws RemoteException {
            return null;
        }

        @Override
        public void printerInit(ICallback callback) throws RemoteException {

        }

        @Override
        public void printerSelfChecking(ICallback callback) throws RemoteException {

        }

        @Override
        public String getPrinterSerialNo() throws RemoteException {
            return null;
        }

        @Override
        public String getPrinterVersion() throws RemoteException {
            return null;
        }

        @Override
        public String getPrinterModal() throws RemoteException {
            return null;
        }

        @Override
        public int getPrintedLength(ICallback callback) throws RemoteException {
            return 0;
        }

        @Override
        public void lineWrap(int n, ICallback callback) throws RemoteException {

        }

        @Override
        public void sendRAWData(byte[] data, ICallback callback) throws RemoteException {

        }

        @Override
        public void setAlignment(int alignment, ICallback callback) throws RemoteException {

        }

        @Override
        public void setFontName(String typeface, ICallback callback) throws RemoteException {

        }

        @Override
        public void setFontSize(float fontsize, ICallback callback) throws RemoteException {

        }

        @Override
        public void printText(String text, ICallback callback) throws RemoteException {

        }

        @Override
        public void printTextWithFont(String text, String typeface, float fontsize, ICallback callback) throws RemoteException {

        }

        @Override
        public void printColumnsText(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, ICallback callback) throws RemoteException {

        }

        @Override
        public void printBitmap(Bitmap bitmap, ICallback callback) throws RemoteException {

        }

        @Override
        public void printBarCode(String data, int symbology, int height, int width, int textposition, ICallback callback) throws RemoteException {

        }

        @Override
        public void printQRCode(String data, int modulesize, int errorlevel, ICallback callback) throws RemoteException {

        }

        @Override
        public void printOriginalText(String text, ICallback callback) throws RemoteException {

        }

        @Override
        public void commitPrint(TransBean[] transbean, ICallback callback) throws RemoteException {

        }

        @Override
        public void commitPrinterBuffer() throws RemoteException {

        }

        @Override
        public void cutPaper(ICallback callback) throws RemoteException {

        }

        @Override
        public int getCutPaperTimes() throws RemoteException {
            return 0;
        }

        @Override
        public void openDrawer(ICallback callback) throws RemoteException {

        }

        @Override
        public int getOpenDrawerTimes() throws RemoteException {
            return 0;
        }

        @Override
        public void enterPrinterBuffer(boolean clean) throws RemoteException {

        }

        @Override
        public void exitPrinterBuffer(boolean commit) throws RemoteException {

        }

        @Override
        public void tax(byte[] data, ITax callback) throws RemoteException {

        }

        @Override
        public void getPrinterFactory(ICallback callback) throws RemoteException {

        }

        @Override
        public void clearBuffer() throws RemoteException {

        }

        @Override
        public void commitPrinterBufferWithCallback(ICallback callback) throws RemoteException {

        }

        @Override
        public void exitPrinterBufferWithCallback(boolean commit, ICallback callback) throws RemoteException {

        }

        @Override
        public void printColumnsString(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, ICallback callback) throws RemoteException {

        }

        @Override
        public int updatePrinterState() throws RemoteException {
            return 0;
        }

        @Override
        public void sendLCDCommand(int flag) throws RemoteException {

        }

        @Override
        public void sendLCDString(String string, ILcdCallback callback) throws RemoteException {

        }

        @Override
        public void sendLCDBitmap(Bitmap bitmap, ILcdCallback callback) throws RemoteException {

        }

        @Override
        public int getPrinterMode() throws RemoteException {
            return 0;
        }

        @Override
        public int getPrinterBBMDistance() throws RemoteException {
            return 0;
        }

        @Override
        public void printBitmapCustom(Bitmap bitmap, int type, ICallback callback) throws RemoteException {

        }

        @Override
        public int getForcedDouble() throws RemoteException {
            return 0;
        }

        @Override
        public boolean isForcedAntiWhite() throws RemoteException {
            return false;
        }

        @Override
        public boolean isForcedBold() throws RemoteException {
            return false;
        }

        @Override
        public boolean isForcedUnderline() throws RemoteException {
            return false;
        }

        @Override
        public int getForcedRowHeight() throws RemoteException {
            return 0;
        }

        @Override
        public int getFontName() throws RemoteException {
            return 0;
        }

        @Override
        public void sendLCDDoubleString(String topText, String bottomText, ILcdCallback callback) throws RemoteException {

        }

        @Override
        public int getPrinterPaper() throws RemoteException {
            return 0;
        }

        @Override
        public boolean getDrawerStatus() throws RemoteException {
            return false;
        }

        @Override
        public void sendLCDFillString(String string, int size, boolean fill, ILcdCallback callback) throws RemoteException {

        }

        @Override
        public void sendLCDMultiString(String[] text, int[] align, ILcdCallback callback) throws RemoteException {

        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };
}

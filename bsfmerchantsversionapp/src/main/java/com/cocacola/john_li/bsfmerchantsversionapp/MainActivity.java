package com.cocacola.john_li.bsfmerchantsversionapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.cocacola.john_li.bsfmerchantsversionapp.Adapter.MyMenuAdapter;
import com.cocacola.john_li.bsfmerchantsversionapp.Utils.PrinterCallback;
import com.sunmi.peripheral.printer.ICallback;
import com.sunmi.peripheral.printer.ILcdCallback;
import com.sunmi.peripheral.printer.ITax;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.InnerResultCallbcak;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.TransBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView mGv;

    private static final int START_SCAN = 0x0001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
        initData();
    }

    private void initView() {
        mGv = (GridView) findViewById(R.id.menu_gridView);
    }

    private void setListener() {
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 为外部调用提供
                        try {
                            Intent intent = new Intent("com.summi.scan");

                            intent.setPackage("com.sunmi.sunmiqrcodescanner");
                             //扫码模块有一些功能选项，开发者可以通过传递参数控制这些参数，
                             //所有参数都有一个默认值，开发者只要在需要的时候添加这些配置就可以。
                             intent.putExtra("CURRENT_PPI", 0X0003);//当前分辨率
                             //M1和V1的最佳是800*480,PPI_1920_1080 = 0X0001;PPI_1280_720 =
                             //0X0002;PPI_BEST = 0X0003;
                             intent.putExtra("PLAY_SOUND", true);// 扫描完成声音提示  默认true
                             intent.putExtra("PLAY_VIBRATE", false);
                             //扫描完成震动,默认false，目前M1硬件支持震动可用该配置，V1不支持
                             intent.putExtra("IDENTIFY_INVERSE_QR_CODE", true);// 识别反色二维码，默认true
                             intent.putExtra("IDENTIFY_MORE_CODE", false);// 识别画面中多个二维码，默认false
                             intent.putExtra("IS_SHOW_SETTING", true);// 是否显示右上角设置按钮，默认true
                             intent.putExtra("IS_SHOW_ALBUM", true);// 是否显示从相册选择图片按钮，默认true
                            startActivityForResult(intent, START_SCAN);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "item", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MainActivity.this, "item", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MainActivity.this, "item", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void initData() {
        Binding();
        try {
            InnerPrinterManager.getInstance().bindService(MainActivity.this, innerPrinterCallback);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }

        mGv.setAdapter(new MyMenuAdapter(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_SCAN && data != null) {
            Bundle bundle = data.getExtras();
            ArrayList<HashMap<String, String>> result = (ArrayList<HashMap<String, String>>) bundle .getSerializable("data");
            Iterator<HashMap<String, String>> it = result.iterator();
            String qrcodeStr = "";
            while (it.hasNext()) {
                HashMap<String, String> hashMap = it.next();
                qrcodeStr = qrcodeStr + hashMap.get("VALUE");
            }
            Toast.makeText(MainActivity.this, qrcodeStr, Toast.LENGTH_SHORT).show();
            try {
                sunmiPrinterService.printText("訂單內容\n" + qrcodeStr + "\n\n\n\n\n\n\n", new InnerResultCallbcak() {
                    @Override
                    public void onRunResult(boolean isSuccess) throws RemoteException {
                        //返回接⼝执⾏的情况 成功/失败
                        Toast.makeText(MainActivity.this, "返回接⼝执⾏的情况 成功/失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onReturnString(String result) throws RemoteException {
                        //部分接⼝会异步返回查询数据
                        Toast.makeText(MainActivity.this, "部分接⼝会异步返回查询数据", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onRaiseException(int code, String msg) throws RemoteException {
                        //接⼝执⾏失败时，返回的异常状态
                        Toast.makeText(MainActivity.this, "接⼝执⾏失败时，返回的异常状态", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onPrintResult(int code, String msg) throws RemoteException {
                        //事务模式下的打印结果返回
                        Toast.makeText(MainActivity.this, "事务模式下的打印结果返回", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
                //接⼝调⽤异常
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}
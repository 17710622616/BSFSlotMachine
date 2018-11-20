package com.cocacola.john_li.bsfmerchantsversionapp.Utils;

import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;

import com.sunmi.peripheral.printer.ICallback;
import com.sunmi.peripheral.printer.ILcdCallback;
import com.sunmi.peripheral.printer.ITax;
import com.sunmi.peripheral.printer.SunmiPrinterService;
import com.sunmi.peripheral.printer.TransBean;

/**
 * Created by John on 18/11/2018.
 */

public class MySunmiPrinterService implements SunmiPrinterService {
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
}

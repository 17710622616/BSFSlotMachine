package com.cocacola.john_li.designateddriver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by John_Li on 14/3/2019.
 */

public class DesignatedDriverActivity extends AppCompatActivity {
    /**
     * 主 变量
     */

    // 主线程Handler
    // 用于将从服务器获取的消息显示出来
    private Handler mMainHandler;

    // Socket变量
    private Socket socket;

    // 线程池
    // 为了方便展示,此处直接采用线程池进行线程管理,而没有一个个开线程
    private ExecutorService mThreadPool;

    /**
     * 接收服务器消息 变量
     */
    // 输入流对象
    InputStream is;

    // 输入流读取器对象
    InputStreamReader isr ;
    BufferedReader br ;

    // 接收服务器发送过来的消息
    String response;


    /**
     * 发送消息到服务器 变量
     */
    // 输出流对象
    OutputStream outputStream;

    /**
     * 按钮 变量
     */

    // 输入需要发送的消息 输入框
    private EditText mEdit;
    private TextView sendTv;
    private TextView receiveTv;

    private WebSocektModel mWebSocektModel;
    private String token = "6E09267B54F3AB6C175C591162A951BD5C10C22033D5795D4334D6F4865A06CC1185FB361E315E797B0DC1FF8A67523E55F2284B8184E72FC2DAEECFDC421DD0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designated_driver);

        mEdit = (EditText) findViewById(R.id.client_send_et);
        sendTv = (TextView) findViewById(R.id.client_send_tv);
        receiveTv = (TextView) findViewById(R.id.client_receiver_tv);

        /**
         * 接收 服务器消息
         */
        receiveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 利用线程池直接开启一个线程 & 执行该线程
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            // 步骤1：创建输入流对象InputStream
                            is = socket.getInputStream();
                            response = readInfoStream(is);

                            // 步骤2：创建输入流读取器对象 并传入输入流对象
                            // 该对象作用：获取服务器返回的数据
                            /*isr = new InputStreamReader(is);
                            br = new BufferedReader(isr);*/
                            // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
                            /*response = br.readLine();
                            System.out.print("response= " + response);*/
                            // 步骤4:通知主线程,将接收的消息显示到界面
                            Message msg = Message.obtain();
                            msg.what = 0;
                            mMainHandler.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        /**
         * 发送消息 给 服务器
         */
        sendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 利用线程池直接开启一个线程 & 执行该线程
                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            // 步骤1：从Socket 获得输出流对象OutputStream
                            // 该对象作用：发送数据
                            outputStream = socket.getOutputStream();

                            // 步骤2：写入需要发送的数据到输出流对象中
                            //outputStream.write((mEdit.getText().toString()+"\n").getBytes("utf-8"));
                            String str = mEdit.getText().toString();
                            if (str.equals("")) {
                                outputStream.write(("{\"type\":\"USER_REGISTER\",\"token\":\""+token+"\",\"fromUser \":\"user65999631\"}").getBytes("utf-8"));
                            } else {
                                outputStream.write((str+"\n").getBytes("utf-8"));
                            }
                            // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞

                            // 步骤3：发送数据到服务端
                            outputStream.flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }


    private final String DEFAULT_ENCODING = "GBK";//编码
    private final int PROTECTED_LENGTH = 51200;// 输入流保护 50KB

    public String readInfoStream(InputStream input) throws Exception {
        if (input == null) {
            throw new Exception("输入流为null");
        }
        //字节数组
        byte[] bcache = new byte[2048];
        int readSize = 0;//每次读取的字节长度
        int totalSize = 0;//总字节长度
        ByteArrayOutputStream infoStream = new ByteArrayOutputStream();
        try {
            //一次性读取2048字节
            while ((readSize = input.read(bcache)) > 0) {
                totalSize += readSize;
                if (totalSize > PROTECTED_LENGTH) {
                    throw new Exception("输入流超出50K大小限制");
                }
                //将bcache中读取的input数据写入infoStream
                infoStream.write(bcache, 0, readSize);
            }
        } catch (IOException e1) {
            throw new Exception("输入流读取异常");
        } finally {
            try {
                //输入流关闭
                input.close();
            } catch (IOException e) {
                throw new Exception("输入流关闭异常");
            }
        }

        try {
            return infoStream.toString(DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new Exception("输出异常");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callNetGetWebSocketData();
    }

    private void callNetGetWebSocketData() {
        RequestParams params = new RequestParams("https://www.bosoftmacao.cn/parkingman-web/systemProfile/webSocketInfo?token=" + token);
        params.setConnectTimeout(10 * 1000);
        String uri = params.getUri();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                final WebSocektModel model = new Gson().fromJson(result, WebSocektModel.class);
                if (model.getCode() == 200) {
                    // 初始化线程池
                    mThreadPool = Executors.newCachedThreadPool();

                    // 实例化主线程,用于更新接收过来的消息
                    mMainHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case 0:
                                    receiveTv.setText(response);
                                    break;
                            }
                        }
                    };
                    // 利用线程池直接开启一个线程 & 执行该线程
                    mThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                // 创建Socket对象 & 指定服务端的IP 及 端口号
                                String ip = model.getData().getFdesc();
                                socket = new Socket("112.74.52.98", 3333);

                                // 判断客户端和服务器是否连接成功
                                System.out.println( "连线状态："+socket.isConnected());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else {
                    Toast.makeText(DesignatedDriverActivity.this, "網絡連接超時，請重試", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DesignatedDriverActivity.this, "網絡連接超時，請重試", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();try {
            // 断开 客户端发送到服务器 的连接，即关闭输出流对象OutputStream
            outputStream.close();

            // 断开 服务器发送到客户端 的连接，即关闭输入流读取器对象BufferedReader
            br.close();

            // 最终关闭整个Socket连接
            socket.close();

            // 判断客户端和服务器是否已经断开连接
            System.out.println(socket.isConnected());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

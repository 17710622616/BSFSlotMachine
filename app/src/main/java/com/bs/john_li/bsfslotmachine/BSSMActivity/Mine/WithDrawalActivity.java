package com.bs.john_li.bsfslotmachine.BSSMActivity.Mine;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.BSSMView.LoadDialog;
import com.bs.john_li.bsfslotmachine.R;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

/**
 * Created by John_Li on 28/3/2018.
 */

public class WithDrawalActivity extends BaseActivity implements View.OnClickListener{
    private BSSMHeadView headView;
    private EditText withdrawNum;
    private CheckBox isagreeCb;
    private TextView withdrawWay, withdrawAgreement, withdrawSubmit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_withdraw);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.wallet_withdraw_head);
        isagreeCb = findViewById(R.id.wallet_withdraw_isagree);
        withdrawNum = findViewById(R.id.wallet_withdraw_num);
        withdrawWay = findViewById(R.id.wallet_withdraw_way);
        withdrawAgreement = findViewById(R.id.wallet_withdraw_agreement);
        withdrawSubmit = findViewById(R.id.wallet_withdraw_submit);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            headView.setHeadHight();
        }
    }

    @Override
    public void setListener() {
        withdrawWay.setOnClickListener(this);
        withdrawAgreement.setOnClickListener(this);
        withdrawSubmit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("餘額提現");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.wallet_withdraw_num:
                finish();
                break;
            case R.id.wallet_withdraw_way:
                chooseWithdrawWay();
                break;
            case R.id.wallet_withdraw_agreement:
                finish();
                break;
            case R.id.wallet_withdraw_submit:
                if (isagreeCb.isChecked() && withdrawNum.getText() != null) {
                    LoadDialog loadDialog = new LoadDialog(this, false, "請求中......");
                    loadDialog.show();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    loadDialog.dismiss();
                    Toast.makeText(WithDrawalActivity.this, "提現已提交！", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(WithDrawalActivity.this, "請選填寫金額及勾選支付服務協議！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void chooseWithdrawWay() {
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_wallet_way)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                        final CheckBox alipayCb = holder.getView(R.id.wallet_way_alipay_cb);
                        final CheckBox wechatpayCb = holder.getView(R.id.wallet_way_wechatpay_cb);
                        holder.setOnClickListener(R.id.wallet_way_submit, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (alipayCb.isChecked()) {
                                    withdrawWay.setText("支付寶");
                                } else if (wechatpayCb.isChecked()) {
                                    withdrawWay.setText("微信");
                                } else {
                                    Toast.makeText(WithDrawalActivity.this, "請選擇提現方式！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setShowBottom(false)
                .setWidth(260)
                .show(getSupportFragmentManager());
    }
}

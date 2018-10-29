package com.bs.john_li.bsfslotmachine.BSSMActivity.Parking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.john_li.bsfslotmachine.BSSMActivity.BaseActivity;
import com.bs.john_li.bsfslotmachine.BSSMModel.SlotUnknowOrderModel;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMCommonUtils;
import com.bs.john_li.bsfslotmachine.BSSMUtils.BSSMConfigtor;
import com.bs.john_li.bsfslotmachine.BSSMView.BSSMHeadView;
import com.bs.john_li.bsfslotmachine.R;
import com.google.gson.Gson;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.NiceDialog;
import com.othershe.nicedialog.ViewConvertListener;
import com.othershe.nicedialog.ViewHolder;

/**
 * Created by John_Li on 29/10/2018.
 */

public class InputUnknowSlotMachineActivity extends BaseActivity implements View.OnClickListener {
    private BSSMHeadView headView;
    private TextView noTv, colorTv, areaTv, addressTv;

    private SlotUnknowOrderModel mSlotUnknowOrderModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_unknow_slotmachine);
        initView();
        setListener();
        initData();
    }

    @Override
    public void initView() {
        headView = findViewById(R.id.input_unknow_sm_head);
        noTv = findViewById(R.id.input_unknow_sm_no);
        colorTv = findViewById(R.id.input_unknow_sm_color);
        areaTv = findViewById(R.id.input_unknow_sm_area);
        addressTv = findViewById(R.id.input_unknow_sm_address);
    }

    @Override
    public void setListener() {
        noTv.setOnClickListener(this);
        colorTv.setOnClickListener(this);
        areaTv.setOnClickListener(this);
        addressTv.setOnClickListener(this);
    }

    @Override
    public void initData() {
        headView.setLeft(this);
        headView.setTitle("填寫咪錶信息");
        headView.setRightText("下一步", this);

        mSlotUnknowOrderModel = new SlotUnknowOrderModel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                finish();
                break;
            case R.id.head_right_tv:
                if (mSlotUnknowOrderModel.getUnknowMachineno() != null && mSlotUnknowOrderModel.getPillarColor() != null && mSlotUnknowOrderModel.getAreaCode() != null && mSlotUnknowOrderModel.getRemark() != null) {
                    Intent intent = new Intent(this, ChooseCarActivity.class);
                    intent.putExtra("way", BSSMConfigtor.SLOT_MACHINE_NOT_EXIST);
                    intent.putExtra("unknowSlotOrder", new Gson().toJson(mSlotUnknowOrderModel));
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "請填寫全訂單信息~", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.input_unknow_sm_no:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText editText = holder.getView(R.id.car_edit);
                                editText.setHint("請填寫六位數咪錶編號(例如：咪錶號2225，車位06則填寫222506！)");
                                BSSMCommonUtils.showKeyboard(editText);
                                holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (editText.getText().toString().length() == 6) {
                                            mSlotUnknowOrderModel.setUnknowMachineno(editText.getText().toString());
                                            noTv.setText("咪錶編號：" + editText.getText().toString());
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(InputUnknowSlotMachineActivity.this, "請填寫六位數咪錶編號(例如：咪錶號2225，車位06則填寫222506！)", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.input_unknow_sm_color:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_meter_color)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                viewHolder.setOnClickListener(R.id.meter_gray, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        colorTv.setText("咪錶顏色：" + "灰色");
                                        mSlotUnknowOrderModel.setPillarColor("gray");
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.meter_green, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        colorTv.setText("咪錶顏色：" + "綠色");
                                        mSlotUnknowOrderModel.setPillarColor("green");
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.meter_red, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        colorTv.setText("咪錶顏色：" + "紅色");
                                        mSlotUnknowOrderModel.setPillarColor("red");
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.meter_yellow, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        colorTv.setText("咪錶顏色：" + "黃色");
                                        mSlotUnknowOrderModel.setPillarColor("yellow");
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.meter_cancel, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        baseNiceDialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.input_unknow_sm_area:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_order_area)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            protected void convertView(ViewHolder viewHolder, final BaseNiceDialog baseNiceDialog) {
                                viewHolder.setOnClickListener(R.id.order_area_datang_tv, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        areaTv.setText("地      區：" + "大堂區");
                                        mSlotUnknowOrderModel.setAreaCode("DT");
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.order_area_wangde_tv, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        areaTv.setText("地      區：" + "望德堂區");
                                        mSlotUnknowOrderModel.setAreaCode("WDT");
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.order_area_fengshun_tv, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        areaTv.setText("地      區：" + "風順堂區");
                                        mSlotUnknowOrderModel.setAreaCode("FST");
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.order_area_huawang_tv, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        areaTv.setText("地      區：" + "花王堂區");
                                        mSlotUnknowOrderModel.setAreaCode("HWT");
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.order_area_huadima_tv, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        areaTv.setText("地      區：" + "花地瑪堂區");
                                        mSlotUnknowOrderModel.setAreaCode("HDMT");
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.order_area_jiamo_tv, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        areaTv.setText("地      區：" + "嘉模堂區");
                                        mSlotUnknowOrderModel.setAreaCode("JMT");
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.order_area_other_tv, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        areaTv.setText("地      區：" + "其他");
                                        mSlotUnknowOrderModel.setAreaCode("QT");
                                        baseNiceDialog.dismiss();
                                    }
                                });
                                viewHolder.setOnClickListener(R.id.order_area_cancel_tv, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        baseNiceDialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
            case R.id.input_unknow_sm_address:
                NiceDialog.init()
                        .setLayoutId(R.layout.dialog_car_edit)
                        .setConvertListener(new ViewConvertListener() {
                            @Override
                            public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {
                                final EditText editText = holder.getView(R.id.car_edit);
                                editText.setHint("請填寫地址(備註內容中請務必包含詳細地址！)");
                                BSSMCommonUtils.showKeyboard(editText);
                                holder.setOnClickListener(R.id.car_edit_submit, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mSlotUnknowOrderModel.setRemark(editText.getText().toString());
                                        addressTv.setText("地      址：" + editText.getText().toString());
                                        dialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setShowBottom(true)
                        .show(getSupportFragmentManager());
                break;
        }
    }
}

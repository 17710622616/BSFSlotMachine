package com.bs.john_li.bsfslotmachine.BSSMView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bs.john_li.bsfslotmachine.R;

import org.xutils.image.AsyncDrawable;

/**
 * 自定義圓形imageview
 * Created by John_Li on 24/11/2017.
 */

public class CycleImageView extends ImageView {
    //  外圓顏色
    private int outCycleColor;
    // 外圓寬度
    private int outCycleWidth;
    // 外圓畫筆
    private Paint paintBorder;

    // 控件寬
    private int viewWidth;
    // 控件高
    private int viewHeigth;
    // 要繪製的圖片
    private Bitmap image;
    public CycleImageView(Context context) {
        super(context);
        init(context, null);
    }

    public CycleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CycleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CycleImageViewAttrs);

            int len = array.length();
            for (int i = 0; i < len; i++) {
                int attr = array.getIndex(i);
                switch (attr) {
                    case R.styleable.CycleImageViewAttrs_outCycleColor:
                        this.outCycleColor = array.getColor(attr, Color.WHITE);
                        break;
                    case R.styleable.CycleImageViewAttrs_outCycleWidth:
                        this.outCycleWidth = (int) array.getDimension(attr, 2);
                        break;
                }
            }
        }

        paintBorder = new Paint();
        paintBorder.setColor(outCycleColor);
        paintBorder.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        viewWidth = width - (outCycleWidth * 2);
        viewHeigth = height - (outCycleWidth * 2);

        setMeasuredDimension(width, height);
    }

    /**
     *
     * @param widthMeasureSpec
     * @return
     */
    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = viewWidth;
        }
        return result;
    }

    /**
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = viewHeigth;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        loadImage();

        if (image != null) {
            // 以防超出取最小的一個數
            int min = Math.min(viewWidth, viewHeigth);
            int cycleCenter = min / 2;
            image = Bitmap.createScaledBitmap(image, min, min, false);

            canvas.drawCircle(cycleCenter + outCycleWidth,cycleCenter + outCycleWidth,cycleCenter + outCycleWidth, paintBorder);
            canvas.drawBitmap(createCycleBitmap(image, min), outCycleWidth, outCycleWidth, null);
        }
    }

    private Bitmap createCycleBitmap(Bitmap image, int min) {
        Bitmap bitmap = null;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        bitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(min/2, min/2, min/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(image, 0, 0, paint);
        return bitmap;
    }

    private void loadImage() {
        Drawable drawable = this.getDrawable();
        if (drawable != null) {
            if(drawable instanceof AsyncDrawable){
                image = Bitmap
                        .createBitmap(
                                getWidth(),
                                getHeight(),
                                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                        : Bitmap.Config.RGB_565);
                Canvas canvas1 = new Canvas(image);
                // canvas.setBitmap(bitmap);
                drawable.setBounds(0, 0, getWidth(),
                        getHeight());
                drawable.draw(canvas1);
            } else if(drawable instanceof BitmapDrawable){
                image =  ((BitmapDrawable)drawable).getBitmap() ;
            }
        }

        /*BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
        if (bitmapDrawable != null) {
            image = bitmapDrawable.getBitmap();
        }*/
    }
}

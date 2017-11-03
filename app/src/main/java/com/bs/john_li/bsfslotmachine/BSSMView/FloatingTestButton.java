package com.bs.john_li.bsfslotmachine.BSSMView;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by John_Li on 2/11/2017.
 */

public class FloatingTestButton extends FloatingActionButton {
    public FloatingTestButton(Context context) {
        super(context);
    }

    public FloatingTestButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingTestButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setMaxImageSize();
    }

    /**
     * 利用反射重新定义fab图片的大小，默认充满整个fab
     */
    public void setMaxImageSize() {
        try {
            Class clazz = getClass().getSuperclass();
            Method sizeMethod = clazz.getDeclaredMethod("getSizeDimension");
            sizeMethod.setAccessible(true);
            int size = (Integer) sizeMethod.invoke(this);
            Field field = clazz.getDeclaredField("mMaxImageSize");
            field.setAccessible(true);
            field.set(this, size);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        postInvalidate();
    }
}

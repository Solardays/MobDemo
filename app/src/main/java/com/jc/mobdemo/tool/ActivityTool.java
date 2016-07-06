package com.jc.mobdemo.tool;

import android.content.Context;
import android.content.Intent;

/**
 * Created by solar on 2016/6/23.
 */
public class ActivityTool {

    public static void startAct(Context context, Class clazz){
        context.startActivity(new Intent(context,clazz));
    }
}

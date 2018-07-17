package com.example.map3dtest.activitycollector;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by Administrator on 2017/12/18.
 *
 * @author Administrator
 *         Activity管理类
 */
public class ActivityCollector {
    /**
     * 使用java.util.Stack来保存所有已打开的Activity。
     * 也可考虑使用双端队列java.util.Deque。
     */
    private static Stack<Activity> activityStack;
    //private static List<Activity> activityStack;

    private static volatile ActivityCollector instance;

    private ActivityCollector() {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
    }

    /**
     * 单一实例
     */
    public static ActivityCollector getActivityCollector() {
        if (null == instance) {
            synchronized (ActivityCollector.class) {
                if (null == instance) {
                    instance = new ActivityCollector();
                }
            }
        }
        return instance;
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        //activityStack.add(activity);
        activityStack.push(activity);
    }

    /**
     * 移除Activity出堆栈
     */
    public void removeActivity(Activity activity) {
        if (activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getActivity() {
        return activityStack.get(activityStack.size() - 1);
    }

    /**
     * 获取指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }


    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.get(activityStack.size() - 1);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }


    /**
     * 关闭所有Activity
     */
    public void finishAll() {
        for (Activity activity : activityStack) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 关闭所有(前台、后台)Activity，按先进先出顺序关闭
     */
    public void finishAllFirstInFirstOut() {
        int len = activityStack.size();
        for (int i = 0; i < len; i++) {
            Activity activity = activityStack.pop();
            activity.finish();
        }
        activityStack.clear();
    }

    /**
     * 关闭所有(前台、后台)Activity，按先进后出顺序关闭
     */
    public void finishAllLastInFirstOut() {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activityStack.clear();
    }


    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            finishAllLastInFirstOut();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

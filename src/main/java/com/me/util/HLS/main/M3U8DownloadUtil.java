package com.me.util.HLS.main;

import com.me.util.HLS.download.M3u8DownloadFactory;
import com.me.util.HLS.listener.DownloadListener;
import com.me.util.HLS.utils.Constant;
import com.me.util.HLS.utils.Log;

/**
 * @author yuejingshuai
 * @date 2021/2/25
 */
public class M3U8DownloadUtil {

    private String M3U8URL;
    private String DIR;
    private String FILENAME;
    private int THREADCOUNT;
    private int RETRYCOUNT;
    private long TIMEOUTMILLISECOND;
    private int LOGLEVEL;
    private long INTERVAL;

    /**
     * @param m3U8URL 设置下载地址
     */
    public M3U8DownloadUtil setM3U8URL(String m3U8URL) {
        this.M3U8URL = m3U8URL;
        return this;
    }

    /**
     * @param DIR 设置生成目录
     */
    public M3U8DownloadUtil setDIR(String DIR) {
        this.DIR = DIR;
        return this;
    }

    /**
     * @param FILENAME 设置视频名称
     */
    public M3U8DownloadUtil setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
        return this;
    }

    /**
     * @param THREADCOUNT 设置线程数 默认100
     */
    public M3U8DownloadUtil setTHREADCOUNT(int THREADCOUNT) {
        this.THREADCOUNT = THREADCOUNT;
        return this;
    }

    /**
     * @param RETRYCOUNT 设置重试次数 默认100
     */
    public M3U8DownloadUtil setRETRYCOUNT(int RETRYCOUNT) {
        this.RETRYCOUNT = RETRYCOUNT;
        return this;
    }

    /**
     * @param TIMEOUTMILLISECOND 设置连接超时时间（单位：毫秒） 默认10000L
     */
    public M3U8DownloadUtil setTIMEOUTMILLISECOND(long TIMEOUTMILLISECOND) {
        this.TIMEOUTMILLISECOND = TIMEOUTMILLISECOND;
        return this;
    }

    /**
     * @param LOGLEVEL 设置日志级别 可选值：NONE INFO DEBUG ERROR EG:Constant.INFO
     */
    public M3U8DownloadUtil setLOGLEVEL(int LOGLEVEL) {
        this.LOGLEVEL = LOGLEVEL;
        return this;
    }

    /**
     * @param INTERVAL 设置监听器间隔（单位：毫秒） 默认500
     */
    public M3U8DownloadUtil setINTERVAL(long INTERVAL) {
        this.INTERVAL = INTERVAL;
        return this;
    }

    /**
     * 初始化
     *
     * @param m3U8URL  下载地址
     * @param dir      生成目录
     * @param fileName 视频名称
     * @Author: yjs
     * @Date: 14:34 2021/2/25
     */
    M3U8DownloadUtil(String m3U8URL, String dir, String fileName) {
        M3U8URL = m3U8URL;
        DIR = dir;
        FILENAME = fileName;
    }

    M3U8DownloadUtil() {

    }

    /**
     * 根据m3u8地址下载
     */
    public void start() {
        M3u8DownloadFactory.M3u8Download m3u8Download = M3u8DownloadFactory.getInstance(M3U8URL);
        //设置生成目录
        m3u8Download.setDir(DIR);
        //设置视频名称
        m3u8Download.setFileName(FILENAME);
        //设置线程数
        m3u8Download.setThreadCount(THREADCOUNT == 0 ? 100 : THREADCOUNT);
        //设置重试次数
        m3u8Download.setRetryCount(RETRYCOUNT == 0 ? 100 : RETRYCOUNT);
        //设置连接超时时间（单位：毫秒）
        m3u8Download.setTimeoutMillisecond(TIMEOUTMILLISECOND == 0 ? 10000L : TIMEOUTMILLISECOND);
        /*
        设置日志级别
        可选值：NONE INFO DEBUG ERROR
        */
        m3u8Download.setLogLevel(LOGLEVEL == 0 ? Constant.INFO : LOGLEVEL);
        //设置监听器间隔（单位：毫秒）
        m3u8Download.setInterval(INTERVAL == 0 ? 500L : INTERVAL);
        //添加额外请求头
        /*Map<String, Object> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "text/html;charset=utf-8");
        m3u8Download.addRequestHeaderMap(headersMap);*/
        //添加监听器
        m3u8Download.addListener(new DownloadListener() {
            @Override
            public void start() {
                Log.i("开始下载！");
            }

            @Override
            public void process(String downloadUrl, int finished, int sum, float percent) {
                Log.i("下载网址：" + downloadUrl + "\t已下载" + finished + "个\t一共" + sum + "个\t已完成" + percent + "%");
            }

            @Override
            public void speed(String speedPerSecond) {
                Log.i("下载速度：" + speedPerSecond);
            }

            @Override
            public void end() {
                Log.i("下载完毕");
            }
        });
        //开始下载
        m3u8Download.start();
    }



}

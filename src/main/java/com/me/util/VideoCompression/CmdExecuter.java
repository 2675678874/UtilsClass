package com.me.util.VideoCompression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;


/**
 * ClassName: CmdExecuter 封装对操作系统命令行发送指令相关操作
 * Created by yuejingshuai 2020/6/11 0011 下午 6:12
 */
public class CmdExecuter {

    public static String exec(List<String> cmd) {
        String converted_time = null;
        Process proc = null;
        BufferedReader stdout = null;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(cmd);
            builder.redirectErrorStream(true);
            proc = builder.start();
            stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            int lineNumber = 1;
            List<String> returnStringList = new LinkedList<String>();
            while ((line = stdout.readLine()) != null) {
                System.out.println("第" + lineNumber + "行:" + line);
                lineNumber = lineNumber + 1;
                returnStringList.add(FFmpegUtil.dealString(line));
            }
            String info = "";
            for (int i = returnStringList.size() - 1; i >= 0; i--) {
                if (null != returnStringList.get(i) && returnStringList.get(i).startsWith("frame=")) {
                    info = returnStringList.get(i);
                    break;
                }
            }
            if (null != info) {
                converted_time = info.split("time=")[1].split("bitrate=")[0].trim();
            }
        } catch (IndexOutOfBoundsException ex) {
            converted_time = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dealStream(proc);
                proc.waitFor();
                stdout.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return converted_time;
    }

    /**
     *  * 处理process输出流和错误流，防止进程阻塞
     *  * 在process.waitFor();前调用
     *  * @param process
     *  
     */
    private static void dealStream(Process process) {
        if (process == null) {
            return;
        }
// 处理InputStream的线程
        new Thread() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while ((line = in.readLine()) != null) {
                        System.out.println("output:" + line);
                    }
                } catch (IOException e) {
                    System.out.println("============================处理InputStream的线程异常！==============================");
//                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
        // 处理ErrorStream的线程
        new Thread() {
            @Override
            public void run() {
                BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = null;
                try {
                    while ((line = err.readLine()) != null) {
                        System.out.println("err: " + line);
                    }
                } catch (IOException e) {
                    System.out.println("============================处理ErrorStream的线程异常！==============================");
//                    e.printStackTrace();
                } finally {
                    try {
                        err.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
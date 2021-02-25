package com.me.util.VideoCompression;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ClassName: processFfmpegWatermkByFont
 * Created by yuejingshuai on 2020年06月17日 下午 5:51
 * 说明：使用该工具类前提电脑上要安装ffmpeg插件
 */
public class FFmpegUtil {
    public static Logger logger = LoggerFactory.getLogger(FFmpegUtil.class);

    public static String dealString(String str) {
        Matcher m = Pattern.compile("^frame=.*").matcher(str);
        String msg = "";
        while (m.find()) {
            msg = m.group();
        }
        return msg;
    }

    /**
     * 如果是数字就是成功的时间(秒数)
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 如果返回不是null的值就是成功(值为转换用时单位:秒)
     *
     * @param instr
     * @return
     */
    public static String returnSecond(String instr) {
        String returnValue = null;
        if (null != instr) {
            String[] a = instr.split("\\.");
            String[] b = a[0].split(":");
            int returnNumber = 0;
            if (null != instr && b[0].length() != 0) {
                returnNumber = Integer.valueOf(b[0]) * 60 * 60 + Integer.valueOf(b[1]) * 60 + Integer.valueOf(b[2]);
                returnValue = String.valueOf(returnNumber);
            } else {
                returnValue = null;
            }
        }
        return returnValue;
    }

    /**
     * 获取视频格式(转码前的格式和转码后的格式都可以调用)
     *
     * @param outputPath
     * @return
     */
    public static String returnVideoFormat(String outputPath) {
        return outputPath.substring(outputPath.lastIndexOf(".") + 1);
    }


    /**
     * @param ffmpegPath    插件文件：插件路径；（必填）
     * @param targetFile    目标文件：文件路径；（必填）
     * @param watermarkText 水印文字 （可选null）
     * @param compressLevel 视频压缩级别 0：无压缩 1： 压缩约55%   2、压缩约75%  3、压缩约85%（可填）
     * @return 返回新文件路径
     * @Description 视频添加水印、压缩处理
     * @author yuejingshuai
     * @date 2020/6/23
     */
    public static Map<String, Object> videoHandle(String ffmpegPath, String targetFile, String watermarkText, Integer compressLevel) throws EncoderException {
        boolean flag = true;
        boolean leveFlag = false;
        int videoHeight = 0;
        int videoWidth = 0;
        String ar = "44100";
        String crf = "18";
        if (compressLevel != null && compressLevel == 1) {//一级
            ar = "44100";
            crf = "35";
            leveFlag = true;
        } else if (compressLevel != null && compressLevel == 2) {//二级
            ar = "22050";
            crf = "40";
            leveFlag = true;
        } else if (compressLevel != null && compressLevel == 3) {//三级
            ar = "11025";
            crf = "45";
            leveFlag = true;
        } else if (compressLevel != null && compressLevel == 0) {
            ar = "44100";
            crf = "18";
            leveFlag = true;
        }
        if (targetFile == null || targetFile == "") {
            return null;
        }
        String path = null;
        String inagePath = null;
        if (watermarkText != null && watermarkText != "") {
            //获取视频宽高
            File source = new File(targetFile);
            Encoder encoder = new Encoder();
            MultimediaInfo info = encoder.getInfo(source);
            videoHeight = info.getVideo().getSize().getHeight();
            videoWidth = info.getVideo().getSize().getWidth();
            path = System.getProperty("resources") + "fileupload/logo/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            inagePath = path + "logo.png";
            Font font = new Font("微软雅黑",Font.PLAIN, 29);//设置字体
            BufferedImage bi1 = ImageUtil.waterMarkByText(watermarkText, Color.getHSBColor(33, 33, 33), font, 0d, 0.2f, videoWidth);//给图片添加文字水印
            try {
                flag = ImageIO.write(bi1, "png", new File(inagePath));//写入文件
                boolean windows = System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
                if (windows) {
                    String substring = inagePath.substring(0, 1);
                    String substring1 = inagePath.substring(1, inagePath.length());
                    String s = substring1.replaceAll("\\\\", "/");
                    inagePath = substring + "\\\\" + s;
                }
            } catch (IOException e) {
                logger.error("生成水印图片，写入文件失败！", e);
                System.out.println();
                e.printStackTrace();
            }
        }
        //添加水印、压缩
        String newSyFilePath = null;
        String filePath = null;
        String fileName = null;
        if (flag) {
            filePath = targetFile.substring(0, targetFile.lastIndexOf(File.separator));
            fileName = targetFile.substring(targetFile.lastIndexOf(File.separator) + 1, targetFile.length());
            String name = fileName.substring(0, fileName.lastIndexOf("."));
            newSyFilePath = filePath + File.separator + name + ".flv";
            HashMap<String, String> dto = new HashMap<String, String>();
            dto.put("ffmpeg_path", ffmpegPath);// 必填
            dto.put("input_path", targetFile);// 必填
            dto.put("video_converted_path", newSyFilePath);// 必填
            /*
            顶部左边overlay=10:10
            顶部右边overlay=main_w-overlay_w-10:10
            底部左边overlay=10:main_h-overlay_h-10
            底部右边overlay=main_w-overlay_w-10:main_h-overlay_h-10
            */
            if (inagePath != null && inagePath != "") {
                dto.put("xaxis", "main_w-overlay_w-0");
                dto.put("yaxis", "0");
                // 可选(注意windows下面的logo地址前面要写4个反斜杠,如果用浏览器里面调用servlet并传参只用两个,如 d:\\:/ffmpeg/input/logo.png)
                dto.put("logo", inagePath);
            }
            if (leveFlag) {
                dto.put("ar", ar);
                dto.put("crf", crf);
            }
            String s = videoTransfer(dto, leveFlag);
            if (s != null) {
//                new File(targetFile).delete();
            } else {
                flag = false;
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (flag) {
            map.put("path", newSyFilePath);
            map.put("videoHeight", videoHeight);
            map.put("videoWidth", videoWidth);
        }
        return map;
    }

    /**
     * @ HashMap<String,String> dto 参数传递对象<br>
     * dto中包含的参数<br>
     * (必填)1.ffmpeg_path:ffmpeg执行文件地址,如 d:\\ffmpeg\\ffmpeg.exe
     * Linux下直接调用ffmpeg命令(当然你事先已经有这个程序了)<br>
     * (必填)2.input_path:原视频路径<br>
     * (必填)3.video_converted_path:转换后视频输出路径<br>
     * (可选)4.screen_size:视频尺寸 长度乘宽度 乘号用英文小写"x"如 512x480<br>
     * (可选)5.logo:水印地址(其实在ffmpeg中有一个专门的watermark参数,logo跟它有何不同,我还没看,不过对我来说效果一样
     * 貌似需要png图片才行)<br>
     * (可选,如果填写必须有logo才行,默认为0)6.xaxis:水印logo的横坐标(只有logo参数为一个正确路径才行) 比如0<br>
     * (可选,如果填写必须有logo才行,默认为0)6.yaxis:水印logo的纵坐标(只有logo参数为一个正确路径才行) 比如0<br>
     * (可选)vb:视频比特率,传入一个数值,单位在程序里面拼接了k (可选)ab:音频比特率,传入一个数值,单位在程序里面拼接了k
     */
    public static String videoTransfer(HashMap<String, String> dto, boolean isCompress) {
        // String ffmpeg_path,String input_path,String video_converted_path,String
        // logo,String screen_size,String xaxis,String yaxis,String vb,String ab
        List<String> cmd = new ArrayList<String>();
        cmd.add(dto.get("ffmpeg_path"));
        cmd.add("-y");
        cmd.add("-i");
        cmd.add(dto.get("input_path"));
        if (null != dto.get("screen_size")) {
            cmd.add("-s");
            cmd.add(dto.get("screen_size"));
        }
        if (isCompress) {
            cmd.add("-c:v");
            cmd.add("libx264");
            cmd.add("-ar");
//        cmd.add("11025");
            cmd.add(dto.get("ar"));
            cmd.add("-crf");
//        cmd.add("40");
            cmd.add(dto.get("crf"));
        }
        if (null != dto.get("logo")) {
            //给视频全部添加水印
            String logo = dto.get("logo");
            cmd.add("-vf");
            String xaxis = dto.get("xaxis");
            String yaxis = dto.get("yaxis");
            xaxis = xaxis != null && !xaxis.equals("") ? xaxis : "0";
            yaxis = yaxis != null && !yaxis.equals("") ? yaxis : "0";
            //当时间小于等于5秒时，水印x位置为100，反之不显示水印。（或者你也可以用gte来判断“大于或等于”）
            String x = "\'if(lte(t,10)," + xaxis + ",NAN)\'";
            String y = "\'if(lte(t,10)," + yaxis + ",NAN)\'";
//            String logoString = "movie=" + logo + "[logo],[in][logo]overlay=x=" + xaxis + ":y=" + yaxis + "[out]";
            String logoString = "movie=" + logo + "[logo],[in][logo]overlay=x=" + x + ":y=" + y + "[out]";
            cmd.add(logoString);
        }
        cmd.add("-strict");
        cmd.add("-2");
        if (null != dto.get("vb") && !dto.get("vb").equals("")) {
            cmd.add("-vb");
            cmd.add(dto.get("vb") + "k");
        }
        if (null != dto.get("ab") && !dto.get("ab").equals("")) {
            cmd.add("-ab");
            cmd.add(dto.get("ab") + "k");
        }
        cmd.add("-q:v");
        cmd.add("10");
        cmd.add(dto.get("video_converted_path"));
        String converted_time = CmdExecuter.exec(cmd);
        // 获取转换时间
        return returnSecond(converted_time);
    }

    public String videoCompression(String ffmpegPath, String inputPath, String outputPath, String ar, String crf) {
        List<String> cmd = new ArrayList<String>();
        cmd.add(ffmpegPath);
        cmd.add("-y");
        cmd.add("-i");
        cmd.add(inputPath);
//        -crf 很重要，是控制转码后视频的质量，质量越高，文件也就越大。
//        此值的范围是 0 到 51：0 表示高清无损；23 是默认值（如果没有指定此参数）；51 虽然文件最小，但效果是最差的。
//        值越小，质量越高，但文件也越大，建议的值范围是 18 到 28。而值 18 是视觉上看起来无损或接近无损的，当然不代表是数据（技术上）的转码无损。
//        -ar 22050用于调整音频采样范围（音频质量）。您可以选择11025、22050或44100。
        cmd.add("-c:v");
        cmd.add("libx264");
        cmd.add("-ar");
//        cmd.add("11025");
        cmd.add(ar);
        cmd.add("-crf");
//        cmd.add("40");
        cmd.add(crf);
        cmd.add(outputPath);
        String converted_time = CmdExecuter.exec(cmd);
        // 获取转换时间
        return returnSecond(converted_time);
    }


}

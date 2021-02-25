package com.me.util.VideoCompression;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * ClassName: demo
 * Created by yuejingshuai on 2020年07月03日 上午 10:21
 */
public class demo {

    public static void textToImage(String str) {
        try {
            int width = 50;
            int height = 1400;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Font font = new Font("楷体", Font.PLAIN, 14);
            Graphics2D g = image.createGraphics();
            image = g.getDeviceConfiguration()
                    .createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g.dispose();
            g = image.createGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);
            g.setFont(font);
            Color c = new Color(106, 106, 106);
            g.setColor(c);
            int fontHeight = (int) font.getSize2D();

            // 得到当前的font metrics
            FontMetrics metrics = g.getFontMetrics();
            int StrPixelWidth = metrics.stringWidth(str); // 字符串长度（像素） str要打印的字符串
            int lineSize = (int) Math.ceil(StrPixelWidth * 1.0 / width);// 算出行数
            System.out.println(StrPixelWidth + "---:");
            if (width < StrPixelWidth) {// 页面宽度（width）小于 字符串长度
                StringBuilder sb = new StringBuilder();// 存储每一行的字符串
                int j = 0;
                int tempStart = 0;
                String tempStrs[] = new String[lineSize];// 存储换行之后每一行的字符串
                System.out.println(str.length());
                for (int i1 = 0; i1 < str.length(); i1++) {
                    char ch = str.charAt(i1);
                    sb.append(ch);
                    Rectangle2D bounds2 = metrics.getStringBounds(sb.toString(), null);
                    int tempStrPi1exlWi1dth = (int) bounds2.getWidth();
                    if (tempStrPi1exlWi1dth > width) {
                        tempStrs[j++] = str.substring(tempStart, i1);
                        tempStart = i1;
                        sb.delete(0, sb.length());
                        sb.append(ch);
                    }
                    if (i1 == str.length() - 1) {// 最后一行
                        tempStrs[j] = str.substring(tempStart);
                    }
                }
                for (int i = 0; i < tempStrs.length; i++) {
                    g.drawString(tempStrs[i], 5, (fontHeight + 5) * (i + 1));
                }
            } else {
                g.drawString(str, 5, fontHeight);
            }

            File outputfile = new File("D:\\686.png");
            ImageIO.write(image, "png", outputfile);
            g.dispose();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        textToImage("不只是美国害怕中国崛起，哪个国家都怕。能把将近14亿人按在家里一个月，不准出门，还能有吃有喝，保证基本生存物资的供应，不出乱子，没人闹腾游行示威，一心抗疫。这背后需要多么强大的国力，凝聚力，指挥力和控制力。这是一个什么国家？这是一群什么人？是有理想，有文化，有道德，有纪律的14亿社会主义新人。");
        String aa = "不只是美国害怕中国崛起，哪个国家都怕";
        FFmpegUtil.videoHandle("D:\\Apply\\ffmpeg\\ffmpeg-20191201-637742b-win64-static\\bin\\ffmpeg.exe","D:\\22\\222.mp4",aa,2);
    }
}

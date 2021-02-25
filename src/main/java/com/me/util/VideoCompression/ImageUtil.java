package com.me.util.VideoCompression;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * ClassName: ImageUtil
 * Created by yuejingshuai on 2020年06月17日 下午 6:06
 */
public class ImageUtil {
    /**
     * 生成背景透明的 文字水印，文字位于透明区域正中央，可设置旋转角度
     *
     * @param width  生成图片宽度
     * @param heigth 生成图片高度
     * @param text   水印文字
     * @param color  颜色对象
     * @param font   awt字体
     * @param degree 水印文字旋转角度
     * @param alpha  水印不透明度0f-1.0f
     */
    public static BufferedImage waterMarkByText(String text, Color color,
                                                Font font, Double degree, float alpha, int vWidth) {
        JComponent j1 = new JLabel();
        FontMetrics fm = j1.getFontMetrics(font);
        double StrPixelWidth = fm.stringWidth(text) + 10; // 字符串长度（像素） text要打印的字符串
        int lineSize = (int) Math.ceil(StrPixelWidth / vWidth);// 算出行数
        BufferedImage buffImg = new BufferedImage(lineSize > 1 ? (int) vWidth : (int) StrPixelWidth, lineSize <= 1 ? font.getSize() + 7 : (font.getSize() * lineSize + (7 * lineSize)), BufferedImage.TYPE_INT_RGB);
        /**2、得到画笔对象*/
        Graphics2D g2d = buffImg.createGraphics();
        // ----------  增加下面的代码使得背景透明  -----------------
        buffImg = g2d.getDeviceConfiguration()
                .createCompatibleImage(lineSize > 1 ? (int) vWidth : (int) StrPixelWidth, lineSize <= 1 ? font.getSize() + 7 : (font.getSize() * lineSize + (7 * lineSize)), Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = buffImg.createGraphics();
        // ----------  背景透明代码结束  -----------------
        // 设置对线段的锯齿状边缘处理
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // 设置水印旋转
        if (null != degree) {
            //注意rotate函数参数theta，为弧度制，故需用Math.toRadians转换一下
            //以矩形区域中央为圆心旋转
            g2d.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2,
                    (double) buffImg.getHeight() / 2);
        }
        // 设置颜色
        g2d.setColor(color);
        // 设置 Font
        g2d.setFont(font);
        //设置透明度:1.0f为透明度 ，值从0-1.0，依次变得不透明
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        FontMetrics metrics = g2d.getFontMetrics();
        int fontHeight = (int) font.getSize2D();
        if (vWidth < StrPixelWidth) {// 页面宽度（width）小于 字符串长度
            StringBuilder sb = new StringBuilder();// 存储每一行的字符串
            int j = 0;
            int tempStart = 0;
            String tempStrs[] = new String[lineSize];// 存储换行之后每一行的字符串
            for (int i1 = 0; i1 < text.length(); i1++) {
                char ch = text.charAt(i1);
                sb.append(ch);
                Rectangle2D bounds2 = metrics.getStringBounds(sb.toString(), null);
                int tempStrPi1exlWi1dth = (int) bounds2.getWidth();
                if (tempStrPi1exlWi1dth > vWidth) {
                    tempStrs[j++] = text.substring(tempStart, i1);
                    tempStart = i1;
                    sb.delete(0, sb.length());
                    sb.append(ch);
                }
                if (i1 == text.length() - 1) {// 最后一行
                    tempStrs[j] = text.substring(tempStart);
                }
            }
            for (int i = 0; i < tempStrs.length; i++) {
                g2d.drawString(tempStrs[i], 5, (fontHeight + 5) * (i + 1));
            }
        } else {
            g2d.drawString(text, 5, fontHeight);
        }
        //释放资源
        g2d.dispose();
        return buffImg;

    }


    /**
     * 获取真实字符串宽度，ascii字符占用0.5，中文字符占用1.0
     */
    private static float getRealFontWidth(String text) {
        int len = text.length();
        float width = 0f;
        for (int i = 0; i < len; i++) {
            if (text.charAt(i) < 256) {
                width += 0.5f;
            } else {
                width += 1.0f;
            }
        }
        return width;
    }


    public static void main(String[] args) {
        int width = 200;
        int heigth = 150;
//        String text = "不只是美国害怕中国崛起，哪个国家都怕。能把将近14亿人按在家里一个月，不准出门，还能有吃有喝，保证基本生存物资的供应，不出乱子，没人闹腾游行示威，一心抗疫。这背后需要多么强大的国力，凝聚力，指挥力和控制力。这是一个什么国家？这是一群什么人？是有理想，有文化，有道德，有纪律的14亿社会主义新人。";
        String text = "我们的dd";
        Font font = new Font("微软雅黑", Font.ROMAN_BASELINE, 33);//字体
        BufferedImage bi1 = waterMarkByText(text, Color.getHSBColor(33, 33, 33), font, 0d, 0.2f, 500);//给图片添加文字水印
//        BufferedImage bi = waterMarkByText(width, heigth, "测试aB~,", Color.GRAY, -30d, 0.2f);//给图片添加文字水印
//        BufferedImage bi2 = waterMarkByText(width, heigth, "测试aB~,");//给图片添加文字水印
//        BufferedImage bi3 = waterMarkByText("科委");
        try {
            ImageIO.write(bi1, "png", new File("D:/22/test.png"));//写入文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

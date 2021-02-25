# 整合工具类

###########################################################################

- ## m3u8视频下载---如何使用

```java
public static void main(String[] args) {
    M3U8DownloadUtil m3U8DownloadUtil = new M3U8DownloadUtil("m3u8下载地址", "下载至哪里", "下载文件名");
    m3U8DownloadUtil.setINTERVAL(500L)//设置监听器间隔（单位：毫秒） 默认500
    .setLOGLEVEL(Constant.INFO)//日志等级  NONE INFO DEBUG ERROR 默认Constant.INFO
    .setRETRYCOUNT(100)//设置重试次数 默认100
    .setTIMEOUTMILLISECOND()//设置连接超时时间（单位：毫秒） 默认10000L
    .setTHREADCOUNT()//设置线程数 默认100
    .start();//开始下载
}
```

- ## 根据ftl模板生成word文档---如何使用

```java
public static void main(String[] args) {
    FreeMarkerUtil freeMarkerUtil = new FreeMarkerUtil("设置模板所在文件夹");
    FreeMarkerUtil.crateFile("map", "模板名称", "输出地址");
}
```

- ## 转换为pdf---如何使用

```java
public static void main(String[] args) {
    WordToPDF.savePdf("需转换文件路径","pdf路径");
}
```


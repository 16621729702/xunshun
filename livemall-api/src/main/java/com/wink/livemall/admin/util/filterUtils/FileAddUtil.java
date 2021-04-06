package com.wink.livemall.admin.util.filterUtils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileAddUtil {
    public static void markPic(BufferedImage bufImg, Image img, Image markImg, int width, int height, int x, int y) {
        //取到画笔
        Graphics2D g = bufImg.createGraphics();
        //画底片
        g.drawImage(img, 0, 0, bufImg.getWidth(), bufImg.getHeight(), null);
        //画水印位置
        g.drawImage(markImg, x, y, width, height, null);
        g.dispose();
    }
    /**
     * 直接给multipartFile加上图片水印再进行保存图片的操作方便省事
     *
     * @param multipartFile
     *            文件上传的对象
     * @param
     *            水印文件的路径 如果是相对路径请使用相对路径new Image的方法,此处用的是url
     * @return
     * @throws IOException
     * @author 高永强
     * @version 2018年11月30日 上午11:15:56
     */
    public static MultipartFile addPicMarkToMutipartFile(MultipartFile multipartFile) throws IOException {
        // 获取图片文件名 xxx.png xxx
        String originFileName = multipartFile.getOriginalFilename();
        // 获取原图片后缀 png
        int lastSplit = originFileName.lastIndexOf(".");
        String suffix = originFileName.substring(lastSplit + 1);
        // 获取图片原始信息
        String dOriginFileName = multipartFile.getOriginalFilename();
        String dContentType = multipartFile.getContentType();
        // 是图片且不是gif才加水印
        if (!suffix.equalsIgnoreCase("gif") && dContentType.contains("image")) {
            // 获取水印图片
            InputStream inputImg = multipartFile.getInputStream();
            Image img = ImageIO.read(inputImg);
            URL url = multipartFile.getResource().getURL();
            // 创建url连接;
            HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
            urlconn.connect();
            InputStream inputStream = urlconn.getInputStream();
            Image mark = ImageIO.read(inputStream);

            // 加图片水印
            int imgWidth = img.getWidth(null);
            int imgHeight = img.getHeight(null);

            int markWidth = mark.getWidth(null);
            int markHeight = mark.getHeight(null);

            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight,
                    BufferedImage.TYPE_INT_RGB);
            //水印的相对位置  ps：这里是右下角  水印宽为底片的四分之一  位置自己可以调整
            markPic(bufImg, img, mark, imgWidth / 4, (imgWidth * markHeight) / (4 * markWidth),
                    imgWidth - imgWidth / 4, imgHeight - (imgWidth * markHeight) / (4 * markWidth));
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bufImg, suffix, imOut);
            InputStream is = new ByteArrayInputStream(bs.toByteArray());

            // 加水印后的文件上传
            multipartFile = new MockMultipartFile(dOriginFileName, dOriginFileName, dContentType,
                    is);
        }
        //返回加了水印的上传对象
        return multipartFile;
    }

    public int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }
    public static void main(String[] args) {
         /*FileAddUtil.addWatermark();*/
    }

}

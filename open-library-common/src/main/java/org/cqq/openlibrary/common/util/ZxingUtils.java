package org.cqq.openlibrary.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.exception.server.IORuntimeException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.constant.Constable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Zxing utils
 *
 * @author Qingquan
 */
@Slf4j
public class ZxingUtils {
    
    private ZxingUtils() {
    }
    
    // ======================================================== Common ========================================================
    
    public static Result decode(InputStream inputStream) {
        try {
            // 解码为二进制矩阵
            BinaryBitmap bitmap = new BinaryBitmap(
                    new HybridBinarizer(
                            new BufferedImageLuminanceSource(
                                    ImageIO.read(inputStream)
                            )
                    )
            );
            // 解析内容
            Map<DecodeHintType, String> hints = Map.of(DecodeHintType.CHARACTER_SET, Charset.defaultCharset().name());
            return new MultiFormatReader().decode(bitmap, hints);
        } catch (IOException | NotFoundException exception) {
            throw new IORuntimeException("Decode error", exception);
        }
    }
    
    private static void write(BufferedImage image, String imageFormat, OutputStream writeOutputStream, int logoRate, InputStream logoInputStream) {
        try {
            // 1. 原图宽高
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            
            // 2. logo
            BufferedImage logoImage = ImageIO.read(logoInputStream);
            // 设置logo的大小
            int logoWidth = imageWidth * logoRate / 100;
            int logoHeight = imageHeight * logoRate / 100;
            // SCALE_SMOOTH 算法缩放 logo
            Image scaledLogoImage = logoImage.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
            
            // 填充与logo大小类似的扁平化圆角矩形背景
            Graphics2D graphics2D = image.createGraphics();
            
            // 计算图片放置位置 logo放在中心
            int x = (imageWidth - logoWidth) / 2;
            int y = (imageHeight - logoHeight) / 2;
            
            // 开始绘制logo图片
            graphics2D.drawImage(scaledLogoImage, x, y, null);
            
            // 创建一个具有指定位置、宽度、高度和圆角半径的圆角矩形。这个圆角矩形是用来绘制边框的。
            Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 10, 10);
            // 使用一个宽度为4像素的基本笔触
            graphics2D.setStroke(new BasicStroke(4f));
            // 给logo画圆角矩形
            graphics2D.draw(shape);
            // 释放画笔
            graphics2D.dispose();
            
            // 3. 写入
            ImageIO.write(image, imageFormat, writeOutputStream);
            logoImage.flush();
            image.flush();
        } catch (IOException throwable) {
            throw new IORuntimeException("Write log error", throwable);
        }
    }
    
    // ======================================================== API ========================================================
    
    /**
     * 绘制二维码
     *
     * @param contents        内容
     * @param width           宽度
     * @param height          高度
     * @param imageFormat     图片格式
     * @param logoInputStream logo
     * @param logoRate        logo 大小比例 (建议为二维码的 [20%, 25%]，过大会使得二维码无法被正确扫描)
     */
    public static ByteArrayOutputStream drawQRCode(String contents,
                                                   int width,
                                                   int height,
                                                   String imageFormat,
                                                   InputStream logoInputStream,
                                                   int logoRate) {
        
        Map<EncodeHintType, ? extends Constable> hints = Map.of(
                // 指定纠错等级
                EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H,
                // 指定编码格式
                EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name(),
                // 设置二维码内容到边框的距离
                EncodeHintType.MARGIN, 1
        );
        
        try {
            // 1. 生成二维码矩阵
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BitMatrix qrCodeBitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
            
            // 2. 矩阵转图片
            int qrCodeBitMatrixWidth = qrCodeBitMatrix.getWidth();
            int qrCodeBitMatrixHeight = qrCodeBitMatrix.getHeight();
            BufferedImage qrCodeBitMatrixImage = new BufferedImage(qrCodeBitMatrixWidth, qrCodeBitMatrixHeight, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < qrCodeBitMatrixWidth; x++) {
                for (int y = 0; y < qrCodeBitMatrixHeight; y++) {
                    // BLACK 0xFF000000 / WHITE 0xFFFFFFFF
                    qrCodeBitMatrixImage.setRGB(x, y, qrCodeBitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            
            // 3. 写入
            write(qrCodeBitMatrixImage, imageFormat, byteArrayOutputStream, logoRate, logoInputStream);
            
            // 4. 返回
            return byteArrayOutputStream;
        } catch (WriterException throwable) {
            throw new IORuntimeException("Draw QR code error", throwable);
        }
    }
}
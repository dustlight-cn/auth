package cn.dustlight.validator.sender;

import cn.dustlight.validator.core.Code;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

public class SimpleImageCodeSender<T> implements CodeSender<T> {

    private ImageHandler imageHandler;
    private int width, height;

    public SimpleImageCodeSender(ImageHandler imageHandler, int width, int height) {
        this.imageHandler = imageHandler;
        this.width = width;
        this.height = height;
    }

    public SimpleImageCodeSender() {
        this(new DefaultImageHandler(), 200, 100);
    }

    public void send(Code<T> code, Map<String, Object> parameters) throws SendCodeException {
        if (code == null || code.getValue() == null)
            throw new SendCodeException("Code is null!");
        BufferedImage image = null;
        try {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = requestAttributes.getResponse();
            image = imageHandler.getImage(code.getValue().toString(), width, height, parameters);
            response.setStatus(200);
            response.setContentType("image/jpeg");
            ImageIO.write(image, "jpeg", new BufferedOutputStream(response.getOutputStream()));
        } catch (Exception e) {
            throw new SendCodeException("Fail to send code: " + code, e);
        } finally {
            try {
                if (image != null)
                    image.getGraphics().dispose();
            } catch (Exception e1) {
                throw new SendCodeException("Dispose image fail", e1);
            }
        }
    }

    public ImageHandler getImageHandler() {
        return imageHandler;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setImageHandler(ImageHandler imageHandler) {
        this.imageHandler = imageHandler;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public interface ImageHandler {
        BufferedImage getImage(String code, int width, int height, Map<String, Object> parameters);
    }

    public static class DefaultImageHandler implements ImageHandler {

        private SecureRandom secureRandom;
        private String[] fonts;

        public DefaultImageHandler(String... fontNames) {
            this.secureRandom = new SecureRandom();
            Collection<String> fonts = new LinkedHashSet<>();
            if (fontNames != null) {
                for (String fn : fontNames) {
                    if (fn != null && fn.trim().length() > 0)
                        fonts.add(fn);
                }
            }
            if (fonts.isEmpty())
                fonts.add("Georgia");
            this.fonts = fonts.toArray(new String[0]);
        }

        public BufferedImage getImage(String code, int width, int height, Map<String, Object> parameters) {
            /**
             * 创建背景
             */
            int verifySize = code.length();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            SecureRandom rand = secureRandom;
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color[] colors = new Color[5];
            Color[] colorSpaces = new Color[]{Color.WHITE, Color.CYAN,
                    Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
                    Color.PINK, Color.YELLOW};
            float[] fractions = new float[colors.length];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
                fractions[i] = rand.nextFloat();
            }
            Arrays.sort(fractions);

            g2.setColor(Color.GRAY);// 设置边框色
            g2.fillRect(0, 0, width, height);

            Color c = getRandColor(200, 250);
            g2.setColor(c);// 设置背景色
            g2.fillRect(0, 2, width, height - 4);

            //绘制干扰线
            SecureRandom random = secureRandom;
            g2.setColor(getRandColor(160, 200));// 设置线条的颜色
            for (int i = 0; i < 20; i++) {
                int x = random.nextInt(width - 1);
                int y = random.nextInt(height - 1);
                int xl = random.nextInt(6) + 1;
                int yl = random.nextInt(12) + 1;
                g2.drawLine(x, y, x + xl + 40, y + yl + 20);
            }

            // 添加噪点
            float yawpRate = 0.05f;// 噪声率
            int area = (int) (yawpRate * width * height);
            for (int i = 0; i < area; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int rgb = getRandomIntColor();
                image.setRGB(x, y, rgb);
            }

            shear(g2, width, height, c);// 使图片扭曲

            g2.setColor(getRandColor(100, 160));
            int fontSize = height - 4;
            Font font = new Font("Algerian", Font.ITALIC, fontSize);
            g2.setFont(font);
            char[] chars = code.toCharArray();
            for (int i = 0; i < verifySize; i++) {
                AffineTransform affine = new AffineTransform();
                affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (width / verifySize) * i + fontSize / 2, height / 2);
                g2.setTransform(affine);
                g2.drawChars(chars, i, 1, ((width - 10) / verifySize) * i + 5, height / 2 + fontSize / 2 - 10);
            }

            g2.dispose();
            return image;
        }

        private Color getRandColor(int fc, int bc) {
            if (fc > 255)
                fc = 255;
            if (bc > 255)
                bc = 255;
            int r = fc + secureRandom.nextInt(bc - fc);
            int g = fc + secureRandom.nextInt(bc - fc);
            int b = fc + secureRandom.nextInt(bc - fc);
            return new Color(r, g, b);
        }

        private int getRandomIntColor() {
            int[] rgb = getRandomRgb();
            int color = 0;
            for (int c : rgb) {
                color = color << 8;
                color = color | c;
            }
            return color;
        }

        private int[] getRandomRgb() {
            int[] rgb = new int[3];
            for (int i = 0; i < 3; i++) {
                rgb[i] = secureRandom.nextInt(255);
            }
            return rgb;
        }

        private void shear(Graphics g, int w1, int h1, Color color) {
            shearX(g, w1, h1, color);
            shearY(g, w1, h1, color);
        }

        private void shearX(Graphics g, int w1, int h1, Color color) {

            int period = secureRandom.nextInt(2);

            boolean borderGap = true;
            int frames = 1;
            int phase = secureRandom.nextInt(2);

            for (int i = 0; i < h1; i++) {
                double d = (double) (period >> 1)
                        * Math.sin((double) i / (double) period
                        + (6.2831853071795862D * (double) phase)
                        / (double) frames);
                g.copyArea(0, i, w1, 1, (int) d, 0);
                if (borderGap) {
                    g.setColor(color);
                    g.drawLine((int) d, i, 0, i);
                    g.drawLine((int) d + w1, i, w1, i);
                }
            }

        }

        private void shearY(Graphics g, int w1, int h1, Color color) {

            int period = secureRandom.nextInt(40) + 10; // 50;

            boolean borderGap = true;
            int frames = 20;
            int phase = 7;
            for (int i = 0; i < w1; i++) {
                double d = (double) (period >> 1)
                        * Math.sin((double) i / (double) period
                        + (6.2831853071795862D * (double) phase)
                        / (double) frames);
                g.copyArea(i, 0, 1, h1, 0, (int) d);
                if (borderGap) {
                    g.setColor(color);
                    g.drawLine(i, (int) d, i, 0);
                    g.drawLine(i, (int) d + h1, i, h1);
                }

            }

        }
    }
}

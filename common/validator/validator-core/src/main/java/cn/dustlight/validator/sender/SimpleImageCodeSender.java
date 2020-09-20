package cn.dustlight.validator.sender;

import cn.dustlight.validator.core.Code;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.security.SecureRandom;
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
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) image.getGraphics();

            g.setColor(new Color(255, 255, 255));
            g.fillRect(0, 0, width, height);
            int min = Math.min(width, height), MIN = min / 3, MAX = (int) (min / 1.5f), D = MAX - MIN;

            for (int i = 0, len = code.length(); i < len; i++) {
                String c = code.substring(i, i + 1);
                float x = i * 1.0F * width / len;

                /**
                 * 随机字体
                 */
                int index = secureRandom.nextInt(fonts.length);
                String fontName = fonts[index];
                int style = secureRandom.nextInt(4);
                int size = secureRandom.nextInt(D) + MIN;
                Font font = new Font(fontName, style, size);

                g.setFont(font);
                g.setColor(randomColor(secureRandom));
                g.drawString(c, x, height / 2 + size / 4 + (height - size) / 2 - secureRandom.nextInt(height - size));
//                int num = len; //定义干扰线的数量
//                for (int j = 0; j < num; j++) {
//                    int x1 = secureRandom.nextInt(width);
//                    int y1 = secureRandom.nextInt(height);
//                    int x2 = secureRandom.nextInt(width);
//                    int y2 = secureRandom.nextInt(height);
//                    g.setColor(randomColor(secureRandom));
//                    g.drawLine(x1, y1, x2, y2);
//                }
            }
            return image;
        }

        private static final Color randomColor(SecureRandom secureRandom) {
            int R = secureRandom.nextInt(225);
            int G = secureRandom.nextInt(225);
            int B = secureRandom.nextInt(225);
            return new Color(R, G, B);
        }
    }
}

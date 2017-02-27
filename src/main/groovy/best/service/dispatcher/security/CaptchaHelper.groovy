package best.service.dispatcher.security

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

/**
 * Created by Farzin on 4/13/2016.
 */
class CaptchaHelper {
    static def yekan = ["صفر", "یک", "دو", "سه", "چهار", "پنج", "شش", "هفت", "هشت", "نه"]
    static def dahgan = ["", "", "بیست", "سی", "چهل", "پنجاه", "شصت", "هفتاد", "هشتاد", "نود"]
    static def dahyek = ["ده", "یازده", "دوازده", "سیزده", "چهارده", "پانزده", "شانزده", "هفده", "هجده", "نوزده"]
    static def sadgan = ["", "یکصد", "دویست", "سیصد", "چهارصد", "پانصد", "ششصد", "هفتصد", "هشتصد", "نهصد"]
    static def basex = ["", "هزار", "میلیون", "میلیارد", "تریلیون"]

    private static Font font = null

    private static Font getFont() {
        if (font == null) {
            def fontSource = CaptchaHelper.classLoader.getResourceAsStream('fonts/BMehrBd.ttf')
            def fontFactory = Font.createFont(Font.TRUETYPE_FONT, fontSource)
            font = fontFactory.deriveFont(48f)
        }
        font
    }

    static def randomCaptcha() {
        def num = (Math.random() * 1000) as int
        def str = num2str(num)
        def img = new BufferedImage(600, 100, BufferedImage.TYPE_INT_ARGB)
        def g = img.createGraphics()
        g.setFont(getFont())
        g.setColor(new Color(0, 161, 202))
        g.fillRect(0, 0, 600, 100)
        g.setColor(new Color(255, 255, 255))
        def width = g.getFontMetrics().stringWidth(str)
        g.drawString(str, ((600 - width) / 2).toInteger(), 62)
        img = resize(img, 6 * 35, 35)
        def output = new ByteArrayOutputStream()
        ImageIO.write(img, 'PNG', output)
        [output.toByteArray(), num]
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        def tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        def g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    static def num2str(d) {
        try {
            if (d instanceof Number)
                d = "${d}"
            d = deleteNumberDelimiter(d);
            def a = "";
            def c = "";
            def b;
            if (d == "") {
                return "----"
            }
            if (d.split(".").size() > 1) {
                a = calculateNumber(d.split(".")[0]);
                b = d.split(".")[1].size() <= 2 ? d.split(".")[1] : d.split(".")[1].substring(0, 2);
                c = calculateNumber(b)
            } else {
                a = calculateNumber(d)
            }
            if (c != "") {
                a = a + " مميز " + c
            }
            return a
        } catch (ignored) {
            return ''
        }
    };

    private static def calculateNumber(f) {
        def d = "";
        if (f == "0") {
            return yekan.getAt(0)
        } else {
            for (def e = f.size(); e < (parseInt((f.size() - 1) / 3) + 1) * 3; e++) {
                f = "0" + f
            }
            def c = parseInt(f.size() / 3) - 1;
            for (def e = 0; e <= c; e++) {
                def a = (f.substring(e * 3, (e + 1) * 3)) as Float;
                if (a != 0) {
                    d = d + getNumber(a) + " " + basex.getAt(c - e) + " و "
                }
            }
            d = d.substring(0, d.size() - 3)
        }
        return d
    };

    private static def getNumber(d) {
        def b = "";
        int a, f;
        f = d % 100;
        a = parseInt(d / 100);
        if (a != 0) {
            b = sadgan.getAt(a) + " و "
        }
        if ((f >= 10) && (f <= 19)) {
            b = b + dahyek.getAt(f - 10)
        } else {
            def c = parseInt(f / 10);
            if (c != 0) {
                b = b + dahgan.getAt(c) + " و "
            }
            def e = f % 10;
            if (e != 0) {
                b = b + yekan.getAt(e) + " و "
            }
            b = b.substring(0, b.size() - 3)
        }
        return b
    };

    private static def deleteNumberDelimiter(a) {

        a = a?.replaceAll("[_ / ,  -]", "");
        return a?.trim()
    };

    static def toNumber(a) {
        (deleteNumberDelimiter(a) as Double)
    }

    private static def parseInt(a, b = 0) {
        a as Integer
    }

}


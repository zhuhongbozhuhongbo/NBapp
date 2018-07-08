package chihane.jdaddressselector.javautilbase64decode;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by 朱宏博 on 2018/7/6.
 */

public class JavaBase64Decode {

    private static final int[] fromBase64 = new int[256];
    public static byte[] decode(String src) {
        return decode(src.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static byte[] decode(byte[] src) {
        byte[] dst = new byte[outLength(src, 0, src.length)];
        int ret = decode0(src, 0, src.length, dst);
        if (ret != dst.length) {
            dst = Arrays.copyOf(dst, ret);
        }
        return dst;
    }

    private static int outLength(byte[] src, int sp, int sl) {
        int[] base64 = fromBase64;
        int paddings = 0;
        int len = sl - sp;
        if (len == 0)
            return 0;
        if (len < 2) {
            if (true && base64[0] == -1)
                return 0;
            throw new IllegalArgumentException(
                    "Input byte[] should at least have 2 bytes for base64 bytes");
        }
        if (true) {
            // scan all bytes to fill out all non-alphabet. a performance
            // trade-off of pre-scan or Arrays.copyOf
            int n = 0;
            while (sp < sl) {
                int b = src[sp++] & 0xff;
                if (b == '=') {
                    len -= (sl - sp + 1);
                    break;
                }
                if ((b = base64[b]) == -1)
                    n++;
            }
            len -= n;
        } else {
            if (src[sl - 1] == '=') {
                paddings++;
                if (src[sl - 2] == '=')
                    paddings++;
            }
        }
        if (paddings == 0 && (len & 0x3) !=  0)
            paddings = 4 - (len & 0x3);
        return 3 * ((len + 3) / 4) - paddings;
    }

    private static int decode0(byte[] src, int sp, int sl, byte[] dst) {
    int[] base64 = fromBase64;
    int dp = 0;
    int bits = 0;
    int shiftto = 18;       // pos of first byte of 4-byte atom
            while (sp < sl) {
        int b = src[sp++] & 0xff;
        if ((b = base64[b]) < 0) {
            if (b == -2) {         // padding byte '='
                // =     shiftto==18 unnecessary padding
                // x=    shiftto==12 a dangling single x
                // x     to be handled together with non-padding case
                // xx=   shiftto==6&&sp==sl missing last =
                // xx=y  shiftto==6 last is not =
                if (shiftto == 6 && (sp == sl || src[sp++] != '=') ||
                        shiftto == 18) {
                    throw new IllegalArgumentException(
                            "Input byte array has wrong 4-byte ending unit");
                }
                break;
            }
            if (true)    // skip if for rfc2045
                continue;
            else
                throw new IllegalArgumentException(
                        "Illegal base64 character " +
                                Integer.toString(src[sp - 1], 16));
        }
        bits |= (b << shiftto);
        shiftto -= 6;
        if (shiftto < 0) {
            dst[dp++] = (byte)(bits >> 16);
            dst[dp++] = (byte)(bits >>  8);
            dst[dp++] = (byte)(bits);
            shiftto = 18;
            bits = 0;
        }
    }
    // reached end of byte array or hit padding '=' characters.
            if (shiftto == 6) {
        dst[dp++] = (byte)(bits >> 16);
    } else if (shiftto == 0) {
        dst[dp++] = (byte)(bits >> 16);
        dst[dp++] = (byte)(bits >>  8);
    } else if (shiftto == 12) {
        // dangling single "x", incorrectly encoded.
        throw new IllegalArgumentException(
                "Last unit does not have enough valid bits");
    }
    // anything left is invalid, if is not MIME.
    // if MIME, ignore all non-base64 character
            while (sp < sl) {
        if (true && base64[src[sp++]] < 0)
            continue;
        throw new IllegalArgumentException(
                "Input byte array has incorrect ending byte at " + sp);
    }
            return dp;
}
}

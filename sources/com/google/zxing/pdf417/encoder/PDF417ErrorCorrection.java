package com.google.zxing.pdf417.encoder;

import android.support.v4.view.InputDeviceCompat;
import com.alibaba.wireless.security.SecExceptionCode;
import com.google.zxing.WriterException;
import com.google.zxing.pdf417.PDF417Common;
import com.powyin.slide.BuildConfig;
import com.taobao.atlas.dexmerge.dx.io.Opcodes;
import mtopsdk.common.util.HttpHeaderConstant;
import tv.danmaku.ijk.media.player.IMediaPlayer;

final class PDF417ErrorCorrection {
    private static final int[][] EC_COEFFICIENTS = {new int[]{27, 917}, new int[]{522, 568, 723, 809}, new int[]{237, 308, 436, 284, 646, 653, 428, 379}, new int[]{274, 562, 232, 755, SecExceptionCode.SEC_ERROR_DYN_STORE_UNKNOWN_ERROR, 524, 801, Opcodes.LONG_TO_INT, 295, 116, 442, 428, 295, 42, 176, 65}, new int[]{361, 575, 922, 525, 176, 586, 640, 321, 536, 742, 677, 742, 687, 284, 193, 517, 273, 494, 263, Opcodes.DIV_INT, 593, 800, 571, 320, SecExceptionCode.SEC_ERROR_PKG_VALID_NO_MEMORY, Opcodes.LONG_TO_FLOAT, 231, 390, 685, 330, 63, 410}, new int[]{539, 422, 6, 93, 862, 771, 453, 106, SecExceptionCode.SEC_ERROR_SIGNATURE_INCORRECT_DATA_FILE_DATA, 287, 107, SecExceptionCode.SEC_ERROR_DYN_STORE_GET_ENCRYPT_KEY_FAILED, 733, 877, 381, SecExceptionCode.SEC_ERROR_SIGNATURE_ILLEGEL_KEY, 723, 476, 462, Opcodes.SUB_DOUBLE, 430, SecExceptionCode.SEC_ERROR_SIGNATURE_INCORRECT_DATA_FILE, 858, 822, 543, 376, 511, 400, 672, 762, 283, 184, 440, 35, 519, 31, 460, 594, Opcodes.SHR_INT_LIT8, 535, 517, 352, SecExceptionCode.SEC_ERROR_SIGNATURE_CONFUSE_FAILED, 158, 651, 201, 488, SecExceptionCode.SEC_ERROR_DYN_STORE_NO_MEMORY, 648, 733, IMediaPlayer.MEDIA_INFO_SWITCH_PATH_SYNC_FRAME, 83, 404, 97, 280, 771, 840, 629, 4, 381, 843, 623, 264, 543}, new int[]{521, SecExceptionCode.SEC_ERROR_STA_INVALID_ENCRYPTED_DATA, 864, 547, 858, 580, 296, 379, 53, 779, 897, 444, 400, 925, 749, 415, 822, 93, Opcodes.RSUB_INT_LIT8, 208, PDF417Common.MAX_CODEWORDS_IN_BARCODE, 244, 583, 620, 246, 148, 447, 631, 292, SecExceptionCode.SEC_ERROR_UMID_TIME_OUT, 490, 704, 516, 258, 457, SecExceptionCode.SEC_ERROR_UMID_NO_NETWORK_INIT, 594, 723, 674, 292, 272, 96, 684, 432, 686, SecExceptionCode.SEC_ERROR_SIGNATURE_NO_SEEDSECRET, 860, 569, 193, Opcodes.DIV_INT_LIT8, Opcodes.INT_TO_LONG, Opcodes.USHR_INT_2ADDR, 236, 287, 192, 775, 278, Opcodes.MUL_DOUBLE, 40, 379, IMediaPlayer.MEDIA_INFO_VIDEO_DECODE_ERROR, 463, 646, 776, Opcodes.ADD_DOUBLE, 491, 297, 763, Opcodes.SUB_LONG, 732, 95, 270, 447, 90, 507, 48, 228, 821, 808, 898, 784, 663, 627, 378, 382, 262, 380, SecExceptionCode.SEC_ERROR_SIGNATURE_NO_MEM, 754, 336, 89, SecExceptionCode.SEC_ERROR_SIGNATURE_BLOWFISH_FAILED, 87, 432, 670, 616, Opcodes.MUL_LONG, 374, 242, 726, 600, 269, 375, 898, 845, 454, 354, 130, 814, 587, SecExceptionCode.SEC_ERROR_PKG_VALID_NO_CONFIG_FILE, 34, Opcodes.DIV_INT_LIT16, 330, 539, 297, 827, 865, 37, 517, 834, BuildConfig.VERSION_CODE, 550, 86, 801, 4, 108, 539}, new int[]{524, 894, 75, 766, 882, 857, 74, 204, 82, 586, IMediaPlayer.MEDIA_INFO_STREAM_ABNORMAL_AVSTREAM, 250, SecExceptionCode.SEC_ERROR_UMID_SERVER_RESP_INVALID, 786, Opcodes.DOUBLE_TO_INT, IMediaPlayer.MEDIA_INFO_HTTPDNS_CONNECT_FAIL, 858, Opcodes.XOR_LONG_2ADDR, SecExceptionCode.SEC_ERROR_STA_DECRYPT_MISMATCH_KEY_DATA, 913, 275, Opcodes.DIV_LONG_2ADDR, 375, 850, 438, 733, Opcodes.XOR_LONG_2ADDR, 280, 201, 280, 828, 757, IMediaPlayer.MEDIA_INFO_NETWORK_SHAKE, 814, 919, 89, 68, 569, 11, 204, 796, SecExceptionCode.SEC_ERROR_SIGNATURE_CONFUSE_FAILED, 540, 913, 801, 700, SecExceptionCode.SEC_ERROR_STA_KEY_ENC_UNKNOWN_ERROR, Opcodes.FLOAT_TO_DOUBLE, 439, 418, 592, 668, 353, 859, 370, 694, 325, 240, Opcodes.ADD_INT_LIT8, InputDeviceCompat.SOURCE_KEYBOARD, 284, 549, 209, 884, BuildConfig.VERSION_CODE, 70, 329, 793, 490, 274, 877, 162, 749, 812, 684, 461, 334, 376, 849, 521, 307, 291, SecExceptionCode.SEC_ERROR_PKG_VALID_NO_MEMORY, IMediaPlayer.MEDIA_INFO_VIDEO_DECODE_ERROR, 19, 358, SecExceptionCode.SEC_ERROR_STA_UNKNOWN_ERROR, SecExceptionCode.SEC_ERROR_UMID_TIME_OUT, 103, 511, 51, 8, 517, Opcodes.SHR_INT_LIT8, 289, 470, 637, 731, 66, 255, 917, 269, 463, 830, 730, 433, 848, 585, Opcodes.FLOAT_TO_LONG, 538, SecExceptionCode.SEC_ERROR_UMID_ENVIRONMENT_CHANGED, 90, 2, 290, 743, 199, 655, 903, 329, 49, 802, 580, 355, 588, Opcodes.SUB_LONG_2ADDR, 462, 10, Opcodes.LONG_TO_DOUBLE, 628, 320, 479, 130, 739, 71, 263, 318, 374, SecExceptionCode.SEC_ERROR_SIGNATRUE_INVALID_INPUT, 192, SecExceptionCode.SEC_ERROR_SIGNATURE_CONFUSE_FAILED, Opcodes.INT_TO_CHAR, 673, 687, 234, 722, 384, 177, 752, SecExceptionCode.SEC_ERROR_SIGNATURE_DATA_FILE_MISMATCH, 640, 455, 193, 689, IMediaPlayer.MEDIA_INFO_STREAM_ABNORMAL_AUDIO, SecExceptionCode.SEC_ERROR_PKG_VALID_INVALID_APK_PATH, 641, 48, 60, 732, 621, 895, 544, 261, 852, 655, SecExceptionCode.SEC_ERROR_STA_NO_SUCH_INDEX, 697, 755, 756, 60, 231, 773, 434, 421, 726, 528, SecExceptionCode.SEC_ERROR_DYN_STORE_GET_SYS_PROPERTIES_FAILED, 118, 49, 795, 32, Opcodes.ADD_INT, 500, 238, 836, 394, 280, 566, 319, 9, 647, 550, 73, 914, 342, 126, 32, 681, 331, 792, 620, 60, SecExceptionCode.SEC_ERROR_SIGNATURE_INCORRECT_DATA_FILE, 441, 180, 791, 893, 754, SecExceptionCode.SEC_ERROR_SIGNATURE_CONFUSE_FAILED, 383, 228, 749, 760, Opcodes.AND_INT_LIT16, 54, 297, Opcodes.LONG_TO_DOUBLE, 54, 834, SecExceptionCode.SEC_ERROR_STA_STORE_UNKNOWN_ERROR, 922, Opcodes.REM_LONG_2ADDR, 910, 532, SecExceptionCode.SEC_ERROR_SIGNATURE_INCORRECT_DATA_FILE, 829, Opcodes.MUL_LONG_2ADDR, 20, 167, 29, 872, 449, 83, 402, 41, 656, SecExceptionCode.SEC_ERROR_DYN_STORE_GET_ENCRYPT_KEY_FAILED, 579, 481, Opcodes.MUL_DOUBLE, 404, 251, 688, 95, 497, 555, 642, 543, 307, 159, 924, 558, 648, 55, 497, 10}, new int[]{352, 77, 373, SecExceptionCode.SEC_ERROR_DYN_STORE_GET_DATA_FILE_KEY_FAILED, 35, SecExceptionCode.SEC_ERROR_DYN_STORE_UNKNOWN_ERROR, 428, 207, 409, 574, 118, 498, 285, 380, 350, 492, Opcodes.USHR_LONG_2ADDR, 265, 920, Opcodes.ADD_LONG, 914, SecExceptionCode.SEC_ERROR_STA_STORE_UNKNOWN_ERROR, 229, 643, 294, 871, SecExceptionCode.SEC_ERROR_STA_KEY_NOT_EXISTED, 88, 87, 193, 352, 781, 846, 75, 327, 520, 435, 543, 203, 666, 249, 346, 781, 621, 640, 268, 794, 534, 539, 781, 408, 390, 644, 102, 476, SecExceptionCode.SEC_ERROR_DYN_ENC_UNKNOWN_ERROR, 290, 632, 545, 37, 858, 916, 552, 41, 542, 289, SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE, 272, 383, 800, 485, 98, 752, 472, 761, 107, 784, 860, 658, 741, 290, 204, 681, 407, 855, 85, 99, 62, 482, 180, 20, 297, 451, 593, 913, Opcodes.INT_TO_CHAR, 808, 684, 287, 536, 561, 76, 653, SecExceptionCode.SEC_ERROR_PKG_VALID_UNKNOWN_ERR, 729, 567, 744, 390, InputDeviceCompat.SOURCE_DPAD, 192, 516, 258, 240, 518, 794, 395, Opcodes.FILL_ARRAY_DATA_PAYLOAD, 848, 51, SecExceptionCode.SEC_ERROR_SIGNATURE_INCORRECT_DATA_FILE_DATA, 384, Opcodes.MUL_FLOAT, Opcodes.DIV_LONG_2ADDR, 826, com.powyin.scroll.BuildConfig.VERSION_CODE, 596, 786, SecExceptionCode.SEC_ERROR_STA_NO_DATA_FILE, 570, 381, 415, 641, Opcodes.SUB_LONG, 237, 151, 429, 531, 207, 676, IMediaPlayer.MEDIA_INFO_NETWORK_SHAKE, 89, Opcodes.MUL_FLOAT, 304, 402, 40, IMediaPlayer.MEDIA_INFO_STREAM_ABNORMAL_AVSTREAM, 575, 162, 864, 229, 65, 861, 841, 512, Opcodes.SHR_LONG, 477, Opcodes.AND_INT_LIT8, 92, 358, 785, 288, 357, 850, 836, 827, 736, IMediaPlayer.MEDIA_INFO_STREAM_ABNORMAL_AUDIO, 94, 8, 494, 114, 521, 2, SecExceptionCode.SEC_ERROR_DYN_ENC_UNKNOWN_ERROR, 851, 543, Opcodes.SHL_INT, 729, 771, 95, 248, 361, 578, 323, 856, 797, 289, 51, 684, 466, 533, IMediaPlayer.MEDIA_INFO_ARTP_END_TO_END_DELAY, 669, 45, 902, 452, 167, 342, 244, Opcodes.MUL_DOUBLE, 35, 463, 651, 51, SecExceptionCode.SEC_ERROR_SIGNATRUE_UNKNOWN, 591, 452, 578, 37, 124, 298, 332, 552, 43, 427, 119, 662, 777, 475, 850, 764, 364, 578, 911, 283, IMediaPlayer.MEDIA_INFO_AVFORMAT_TIME, 472, HttpHeaderConstant.SC_FLOW_LIMITED, 245, 288, 594, 394, 511, 327, 589, 777, SecExceptionCode.SEC_ERROR_SIGNATRUE_UNKNOWN, 688, 43, 408, 842, 383, 721, 521, 560, 644, IMediaPlayer.MEDIA_INFO_NETWORK_TRAFFIC, 559, 62, Opcodes.SUB_INT, 873, 663, IMediaPlayer.MEDIA_INFO_FRAME_QUEUE_NULL, 159, 672, 729, 624, 59, 193, 417, 158, 209, 563, 564, 343, 693, 109, SecExceptionCode.SEC_ERROR_SIGNATURE_NO_DATA_FILE, 563, 365, 181, 772, 677, SecExceptionCode.SEC_ERROR_STA_INVALID_ENCRYPTED_DATA, 248, 353, IMediaPlayer.MEDIA_INFO_STREAM_ABNORMAL_AVSTREAM, 410, 579, 870, 617, 841, 632, 860, 289, 536, 35, 777, 618, 586, 424, 833, 77, 597, 346, 269, 757, 632, 695, 751, 331, 247, 184, 45, 787, 680, 18, 66, 407, 369, 54, 492, 228, SecExceptionCode.SEC_ERROR_SIGNATURE_ATLAS_KEY_NOT_EXSITED, 830, 922, 437, 519, 644, SecExceptionCode.SEC_ERROR_UMID_SERVER_RESP_INVALID, 789, HttpHeaderConstant.SC_FLOW_LIMITED, SecExceptionCode.SEC_ERROR_STA_INCORRECT_DATA_FILE_DATA, 441, 207, 300, 892, 827, Opcodes.INT_TO_BYTE, 537, 381, 662, InputDeviceCompat.SOURCE_DPAD, 56, 252, 341, 242, 797, 838, 837, IMediaPlayer.MEDIA_INFO_HTTPDNS_CONNECT_FAIL, Opcodes.SHL_INT_LIT8, 307, 631, 61, 87, 560, SecExceptionCode.SEC_ERROR_STA_INVALID_ENCRYPTED_DATA, 756, 665, 397, 808, 851, SecExceptionCode.SEC_ERROR_STA_NO_SUCH_INDEX, 473, 795, 378, 31, 647, 915, 459, SecExceptionCode.SEC_ERROR_PKG_VALID_OPEN_APK_FAILED, 590, 731, 425, Opcodes.ADD_INT_LIT8, 548, 249, 321, 881, SecExceptionCode.SEC_ERROR_SIGNATRUE_UNKNOWN, 535, 673, 782, Opcodes.MUL_INT_LIT16, 815, SecExceptionCode.SEC_ERROR_UMID_SERVER_RESP_INVALID, SecExceptionCode.SEC_ERROR_STA_NO_DATA_FILE, 843, 922, 281, 73, 469, 791, 660, 162, 498, 308, Opcodes.ADD_LONG, 422, SecExceptionCode.SEC_ERROR_UMID_NO_NETWORK_INIT, 817, 187, 62, 16, 425, 535, 336, 286, 437, 375, 273, SecExceptionCode.SEC_ERROR_SIGNATURE_INCORRECT_DATA_FILE_DATA, 296, 183, 923, 116, 667, 751, 353, 62, 366, 691, 379, 687, 842, 37, 357, IMediaPlayer.MEDIA_INFO_HTTPDNS_CONNECT_FAIL, 742, 330, 5, 39, 923, SecExceptionCode.SEC_ERROR_STA_DECRYPT_MISMATCH_KEY_DATA, 424, 242, 749, 321, 54, 669, 316, 342, SecExceptionCode.SEC_ERROR_STA_STORE_UNKNOWN_ERROR, 534, 105, 667, 488, 640, 672, 576, 540, 316, 486, 721, SecExceptionCode.SEC_ERROR_SIGNATURE_INCORRECT_DATA_FILE_DATA, 46, 656, 447, Opcodes.ADD_DOUBLE, 616, 464, Opcodes.DIV_LONG_2ADDR, 531, 297, 321, 762, 752, 533, Opcodes.REM_DOUBLE, Opcodes.LONG_TO_DOUBLE, 14, 381, 433, IMediaPlayer.MEDIA_INFO_SWITCH_PATH_SYNC_FRAME, 45, 111, 20, 596, 284, 736, Opcodes.DOUBLE_TO_INT, 646, 411, 877, 669, Opcodes.INT_TO_BYTE, 919, 45, 780, 407, Opcodes.SHR_LONG, 332, SecExceptionCode.SEC_ERROR_PKG_VALID_UNKNOWN_ERR, 165, 726, 600, 325, 498, 655, 357, 752, Opcodes.FILL_ARRAY_DATA_PAYLOAD, Opcodes.XOR_INT_LIT8, 849, 647, 63, SecExceptionCode.SEC_ERROR_STA_INVALID_ENCRYPTED_DATA, 863, 251, 366, 304, 282, 738, 675, 410, 389, 244, 31, SecExceptionCode.SEC_ERROR_INIT_DATA_FILE_MISMATCH, SecExceptionCode.SEC_ERROR_STA_NO_DATA_FILE, 263}};

    private PDF417ErrorCorrection() {
    }

    static int getErrorCorrectionCodewordCount(int errorCorrectionLevel) {
        if (errorCorrectionLevel >= 0 && errorCorrectionLevel <= 8) {
            return 1 << (errorCorrectionLevel + 1);
        }
        throw new IllegalArgumentException("Error correction level must be between 0 and 8!");
    }

    static int getRecommendedMinimumErrorCorrectionLevel(int n) throws WriterException {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        } else if (n <= 40) {
            return 2;
        } else {
            if (n <= 160) {
                return 3;
            }
            if (n <= 320) {
                return 4;
            }
            if (n <= 863) {
                return 5;
            }
            throw new WriterException("No recommendation possible");
        }
    }

    static String generateErrorCorrection(CharSequence dataCodewords, int errorCorrectionLevel) {
        int k = getErrorCorrectionCodewordCount(errorCorrectionLevel);
        char[] e = new char[k];
        int sld = dataCodewords.length();
        for (int i = 0; i < sld; i++) {
            int t1 = (dataCodewords.charAt(i) + e[k - 1]) % PDF417Common.NUMBER_OF_CODEWORDS;
            for (int j = k - 1; j > 0; j--) {
                e[j] = (char) ((e[j - 1] + (929 - ((EC_COEFFICIENTS[errorCorrectionLevel][j] * t1) % PDF417Common.NUMBER_OF_CODEWORDS))) % PDF417Common.NUMBER_OF_CODEWORDS);
            }
            e[0] = (char) ((929 - ((EC_COEFFICIENTS[errorCorrectionLevel][0] * t1) % PDF417Common.NUMBER_OF_CODEWORDS)) % PDF417Common.NUMBER_OF_CODEWORDS);
        }
        StringBuilder sb = new StringBuilder(k);
        for (int j2 = k - 1; j2 >= 0; j2--) {
            if (e[j2] != 0) {
                e[j2] = (char) (929 - e[j2]);
            }
            sb.append(e[j2]);
        }
        return sb.toString();
    }
}

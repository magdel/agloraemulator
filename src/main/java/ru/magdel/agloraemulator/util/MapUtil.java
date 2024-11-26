package ru.magdel.agloraemulator.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Map Utilities
 */
public final class MapUtil {


    public static final byte CLATTYPE = 1;
    public static final byte CLONTYPE = 2;

    final public static byte COORDMINSECTYPE = 1;
    final public static byte COORDMINMMMTYPE = 2;
    final public static byte COORDGGGGGGTYPE = 3;
    final public static byte COORDGGGMMMMMMTYPE = 4;


    static public String coordToString(double coord, byte lontype, byte coordType) {
        int icoord = (int) (coord);
        int icoordg, icoordm, icoords;
        double dcoord, dcoordm, dcoords;
        char a;

        if (lontype == CLATTYPE) {
            if (coord > 0) {
                dcoord = coord - icoord;
                a = 'N';
            } else {
                dcoord = icoord - coord;
                a = 'S';
            }
        } else //if (lontype==CLONTYPE)
        {
            if (coord > 0) {
                dcoord = coord - icoord;
                a = 'E';
            } else {
                dcoord = icoord - coord;
                a = 'W';
            }
        }
        String s = null;
        if (coordType == COORDMINSECTYPE) {
            icoordm = (int) (dcoord * 60.);
            dcoords = ((dcoord - (icoordm / 60.)) * 3600.);
            dcoords = MapUtil.coordRound1(dcoords);
            icoordg = Math.abs(icoord);
            s = a + MapUtil.numStr(icoordg, 2) + ((char) 0xb0) + numStr(icoordm, 2) + '\'' + (dcoords) + "''";
        } else if (coordType == COORDMINMMMTYPE) {
            icoordg = Math.abs(icoord);
            icoordm = (int) (dcoord * 60.);
            icoords = (int) ((dcoord - (icoordm / 60.)) * 60000.);

            s = a + MapUtil.numStr(icoordg, 2) + ((char) 0xb0) + MapUtil.numStr(icoordm, 2) + '.' + MapUtil.numStr(icoords, 3);
        } else if (coordType == COORDGGGMMMMMMTYPE) {
            icoordg = Math.abs(icoord);
            icoordm = (int) (dcoord * 60.);
            icoords = (int) ((dcoord - (icoordm / 60.)) * 600000.);
            if (lontype == CLATTYPE) {
                s = a + MapUtil.numStr(icoordg, 2) + MapUtil.numStr(icoordm, 2) + '.' + MapUtil.numStr(icoords, 4);
            } else {
                s = a + MapUtil.numStr(icoordg, 3) + MapUtil.numStr(icoordm, 2) + '.' + MapUtil.numStr(icoords, 4);
            }
        } else if (coordType == COORDGGGGGGTYPE) {
            icoordm = ((int) (dcoord * 100000.));
            icoordg = Math.abs(icoord);
            s = a + MapUtil.numStr(icoordg, 2) + '.' + MapUtil.numStr(icoordm, 5) + ((char) 0xb0);
        }

        return s;
    }

    /**
     * Разбор координаты в указанном формате
     * RMSOption.COORDMINSECTYPE
     * RMSOption.COORDMINMMMTYPE
     * RMSOption.COORDGGGGGGTYPE
     */

    public static String numStr(int number, int digits) {
        StringBuffer s = new StringBuffer(digits);
        boolean rev = (number < 0);
        if (rev) {
            number = -number;
            digits--;
        }
        s.append(number);
        for (int i = s.length(); s.length() < digits; s.insert(0, '0')) ;

        if (rev) {
            s.insert(0, '-');
        }
        return s.toString();
    }


    final public static double coordRound1(double res) {
        if (res < 0.1) {
            return 0;
        }
        return ((int) (res * 10d)) / 10.d;
    }

    final public static double speedRound1(double res) {
        if (res < 0.1) {
            return 0;
        }
        if (res < 50) {
            return ((int) (res * 10d)) / 10.d;
        } else {
            return ((int) res);
        }
    }

    final public static double coordRound5(double res) {
        return ((int) (res * 100000.d)) / 100000.d;
    }

    final public static String[] parseString(String s, char delim) throws Exception {
        int i = 0;
        int pos = 0;
        int nextPos = 0;
        String[] result = new String[150];
        // check how big the array is.
        while (pos > -1) {
            pos = s.indexOf(delim, pos);
            if (pos < 0) {
                continue;
            }
            pos++;
            i++;
        }

        if (i > 500) {
            throw new Exception("to big:" + i);
        }

        i = 0;
        pos = 0;

        // Start splitting the string, search for each ','
        try {


            while (pos > -1) {
                pos = s.indexOf(delim, pos);
                if (pos < 0) {
                    continue;
                }

                nextPos = s.indexOf(delim, pos + 1);
                if (nextPos < 0) {
                    nextPos = s.length();
                }

                result[i] = s.substring(pos + 1, nextPos).trim();
                i++;
                if (i == result.length) {
                    return result;
                }

                if (i > result.length) {
                    break;
                }
                if (pos > -1) {
                    pos++;
                }
            }
        }
        catch (Throwable t) {
            //#mdebug
//#       if (RMSOption.debugEnabled){
//#         DebugLog.add2Log("parS:"+i+":"+pos+":"+t.toString());
//#       }
            //#enddebug
        }
        return result;
    }

    final public static String dateTime2Str(long time) {
        String res;
        try {
            Calendar cal;
            (cal = Calendar.getInstance()).setTime(new Date(time));
            res = MapUtil.numStr(cal.get(Calendar.DAY_OF_MONTH), 2) + '.' +
                    MapUtil.numStr(1 + cal.get(Calendar.MONTH), 2) + '.' +
                    MapUtil.numStr(cal.get(Calendar.YEAR), 4) + '.' +
                    ' ' +
                    MapUtil.numStr(cal.get(Calendar.HOUR_OF_DAY), 2) + ':' +
                    MapUtil.numStr(cal.get(Calendar.MINUTE), 2) + ':' + MapUtil.numStr(cal.get(Calendar.SECOND), 2);
        }
        catch (Throwable t) {
            res = String.valueOf(time);
        }
        return res;
    }

    public final static String time2String(long time) {
        long hour = time / 3600000;
        long min = (time - hour * 3600000) / 60000;
        long sec = (time - hour * 3600000 - min * 60000) / 1000;
        String s = numStr((int) hour, 2) + ':' + numStr((int) min, 2) + ':' + numStr((int) sec, 2);
        return s;
    }


}


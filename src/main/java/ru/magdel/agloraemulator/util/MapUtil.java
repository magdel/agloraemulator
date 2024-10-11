/*
 * MapUtil.java
 *
 * Created on 4 Январь 2007 г., 22:02
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package ru.magdel.agloraemulator.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Map Utilities
 */
public final class MapUtil {

  final public static int blockSize=256;
  final static public double LNdiv2=-0.69314718055994530941723212145818;
  final static public double SQRT3=1.7320508075688772935274463415059;
  final static public double PI=3.1415926535897932384626433832795;
  final static public double PI2div3=2.0943951023931954923084289221863;
  final static public double PImul2=6.283185307179586476925286766559;
  final static public double PIdiv2=1.5707963267948966192313216916398;
  final static public double PIdiv3=1.0471975511965977461542144610932;
  final static public double PIdiv4=0.78539816339744830961566084581988;
  final static public double PIdiv6=0.52359877559829887307710723054658;
  final static public double PIdiv12=0.26179938779914943653855361527329;
  final static public double PImul2_rev=0.15915494309189533576888376337251;
  final static public double E=2.7182818284590452353602874713527;
  final static public double G2R=0.017453292519943295769236907684886;
  final static public double R2G=57.295779513082320876798154814105;
  static public byte[] CRLFb={13, 10};
  static public String CRLFs="\r\n";
  static public String DBSPs="--";
  static public String emptyString="";
  static public String spaceString=" ";
  //static public byte[] UVTM2 = {117, 118, 116, 109, 50, 46, 112, 108};
  static public byte[] CGI_BIN={99, 103, 105, 45, 98, 105, 110, 47};
  static public byte[] UV={117, 118, 46, 112, 108};
  static public byte[] UV_S={117, 118, 0x73, 46, 112, 108};
  static public byte[] UV_R={117, 118, 0x72, 46, 112, 108};
  static public String SH_0="0";
  static public String SH_90="90";
  static public String SH_180="180";
  static public String SH_270="270";
  static public String SH_45="45";
  static public String SH_135="135";
  static public String SH_225="225";
  static public String SH_315="315";
  static public String SH_NORTH="N";
  static public String SH_EAST="E";
  static public String SH_SOUTH="S";
  static public String SH_WEST="W";
  static public String SH_NORTHEAST="NE";
  static public String SH_SOUTHEAST="SE";
  static public String SH_SOUTHWEST="SW";
  static public String SH_NORTHWEST="NW";
  static public String homeSiteURL="http://mapnav.spb.ru/";
  static public String mapServ_Google="Google ";
  static public String mapServ_VE="VE ";
  static public String mapServ_Yahoo="Yahoo ";
  static public String mapServ_Ask="Ask ";
  static public String mapServ_OpenStreet="OpenStreet ";
  static public String mapServ_Gurtam="Gurtam ";
  static public String mapServ_Online="Online ";
  static public String version="?";
  static public String SH_MAP="M";
  static public String SH_MENU="*";
  static public String S_PDOP="PDOP ";
  static public String S_HDOP="HDOP ";
  static public String S_VDOP="VDOP ";
  //static public String S_UTC = "UTC";
  //static public String S_T="T";
  static public String S_OK="OK";
  public static String MAP_MNO="/map.mno";

//  final static public int getTileCount(int level) {
//    return (level < 2) ? 1 : 2 << (level - 2);
//  }
//  final static public int getTileCount(int level) {
//    int res;
//    if (level<2) res=1;else res=2<<(level-2);
//    return res;
//  }
  
  final static int getBitmapSize(int level) {
    //return blockSize * getTileCount(level);
    return blockSize*((level<2)?1:2<<(level-2));
  }

  final static int getBitmapOrig(int level) {
    return (blockSize*((level<2)?1:2<<(level-2)))>>1;
  }

  final static double getPixelsPerLonDegree(int level) {
    return ((double) (blockSize*((level<2)?1:2<<(level-2))))*0.0027777777777777777777777777777778d;
  }

  final static double getPixelsPerLonRadian(int level) {
    return ((double) (blockSize*((level<2)?1:2<<(level-2))))*(0.15915494309189533576888376337251d);
  }
  // final static double grad2rad(double g) {return g*G2R;}
//  final static double abs(double a) {
//    if (a>=0) return a;
//    return -a;
//  }
//  final static int abs(int a) {
//    if (a>=0) return a;
//    return -a;
//  }
//  final static public double sqr(double a) {
//    return a * a;
//  }
  final static public double getLon(int X, int level) {
    return (double) (X-getBitmapOrig(level))/getPixelsPerLonDegree(level);
  }

  final static public double getLat(int Y, int level) {
    return (atan(exp((double) (Y-getBitmapOrig(level))/(-getPixelsPerLonRadian(level))))-PIdiv4)*114.59155902616464175359630962821d;

  //double z = (double)(Y-getBitmapOrig(level))/(-getPixelsPerLonRadian(level));
  //return (atan(exp(z))-PIdiv4)*114.59155902616464175359630962821d;

//return (2*atan(exp(z))-PIdiv2)*57.295779513082320876798154814105;
  }

  final static public String getGLBlockName(int NumX, int NumY, int level) {
    int d;
    StringBuffer res=new StringBuffer(20);
    if (level<2){
      d=1;
    } else {
      d=2<<(level-2);
    }
    if ((NumX<0)||(d-1<NumX)){
      NumX=NumX-(NumX/d)*d;
      if (NumX<0){
        NumX=NumX+d;
      }
    }
    res.append('t');
    for (int i=2; i<=level; i++) {
      d=d>>1;
      if (NumY<d){
        if (NumX<d){
          res.append('q');
        } else {
          res.append('r');
          NumX=NumX-d;
        }
      } else {
        if (NumX<d){
          res.append('t');
        } else {
          res.append('s');
          NumX=NumX-d;
        }
        NumY=NumY-d;
      }
    }
    return res.toString();
  }

  final static public String getYHBlockName(int NumX, int NumY, int level) {
    level=18-level;
    String s="&x="+NumX+"&y="+(((1<<17-level)>>1)-1-NumY)+"&z="+(level+1);
    return s;
  }

  final static private double _ln(double x) {
    if (!(x>0.)){
      return Double.NaN;
    }
    double f=0.0;
    int appendix=0;
    while (x>0.0&&x<=1.0) {
      x*=2.0;
      appendix++;
    }
    //x/=2.0;
    x*=0.5;
    appendix--;
    //double y1=;
    double y2=x+1.;
    double k=(x-1.)/y2;
    //double k=y;
    y2=k*k;
    for (long i=1; i<45; i+=2) {
      f+=k/i;
      k*=y2;
    }
    f*=2.0;
    for (int i=0; i<appendix; i++) {
      f+=LNdiv2;
    }
    return f;
  }

  final static public double ln(double x) {
    if (!(x>0.)){
      return Double.NaN;
    }
    if (x==1.0){
      return 0.0;
    }
    if (x>1.){
      x=1/x;
      return -_ln(x);
    }
    return _ln(x);
  }

  final static public double exp(double x) {
    if (x==0.){
      return 1.;
    }
    double f=1;
    double k;
    boolean isless=(x<0.);
    if (isless){
      x=-x;
    }
    k=x;
    //
    for (long i=2; i<45; i++) {
      f=f+k;
      k=k*x/i;
    }
    //
    if (isless){
      return 1./f;
    } else {
      return f;
    }
  }

  final static public double atan2(double y, double x) {

    if (y==0.&&x==0.){
      return 0.;
    }
    if (x>0.){
      return atan(y/x);
    }
    if (x<0.){
      if (y<0.){
        return -(Math.PI-atan(y/x));
      } else {
        return Math.PI-atan(-y/x);
      }
    }

    if (y<0.){
      return -MapUtil.PIdiv2;
    } else {
      return MapUtil.PIdiv2;
    }
  }

  final static public double atan(double x) {
    boolean signChange=false;
    boolean Invert=false;
    int sp=0;
    double x2, a;

    if (x<0.){
      x=-x;
      signChange=true;
    }

    if (x>1.){
      x=1/x;
      Invert=true;
    }

    while (x>PI/12) {
      sp++;
      a=x+SQRT3;
      a=1/a;
      x=x*SQRT3;
      x=x-1;
      x=x*a;
    }

    x2=x*x;
    a=x2+1.4087812;
    a=0.55913709/a;
    a=a+0.60310579;
    a=a-(x2*0.05160454);
    a=a*x;

    while (sp>0) {
      a=a+PIdiv6;
      sp--;
    }

    if (Invert){
      a=PIdiv2-a;
    }
    if (signChange){
      a=-a;
    }
    return a;
  }

  final static public int getXMap(double lon, int level) {
    //double X = getBitmapOrig(level) + lon * getPixelsPerLonDegree(level);
    return (int) (0.5+getBitmapOrig(level)+lon*getPixelsPerLonDegree(level));// X;
  }

  final static public int getYMap(double lat, int level) {
    double z=Math.sin(lat*G2R);
    //double Y = (double)getBitmapOrig(level) - 0.5 * ln((1.+z)/(1.-z)) * getPixelsPerLonRadian(level);
    //return (int) Y;
    return (int) (0.5+(double) getBitmapOrig(level)-0.5*ln((1.+z)/(1.-z))*getPixelsPerLonRadian(level));
  }

  final static public int getNumX(int xMap) {
//    int NumX =  (int)((double)xMap/ blockSize); //floor
    // int NumX =  (int)(xMap/ blockSize); //floor
    return xMap/blockSize;
  }

  final static public int getNumY(int yMap) {
    //int NumY =  (int)(yMap/ blockSize);
    return yMap/blockSize;
  }

  final static public String getGLBlockNameD(double lat, double lon, int level) {
    return getGLBlockName(getNumX(getXMap(lon, level)), getNumY(getYMap(lat, level)), level);
  }
  public static final byte CLATTYPE=1;
  public static final byte CLONTYPE=2;

  final public static byte COORDMINSECTYPE=1;
    final public static byte COORDMINMMMTYPE=2;
    final public static byte COORDGGGGGGTYPE=3;
    final public static byte COORDGGGMMMMMMTYPE=4;


  final static public String coordToString(double coord, byte lontype, byte coordType) {
    int icoord=(int) (coord);
    int icoordg, icoordm, icoords;
    double dcoord, dcoordm, dcoords;
    char a;

    if (lontype==CLATTYPE){
      if (coord>0){
        dcoord=coord-icoord;
        a='N';
      } else {
        dcoord=icoord-coord;
        a='S';
      }
    } else //if (lontype==CLONTYPE)
    {
      if (coord>0){
        dcoord=coord-icoord;
        a='E';
      } else {
        dcoord=icoord-coord;
        a='W';
      }
    }
    String s=null;
    if (coordType==COORDMINSECTYPE){
      icoordm=(int) (dcoord*60.);
      dcoords=((dcoord-(icoordm/60.))*3600.);
      dcoords=MapUtil.coordRound1(dcoords);
      icoordg=Math.abs(icoord);
      s=a+MapUtil.numStr(icoordg, 2)+((char)0xb0)+numStr(icoordm, 2)+'\''+(dcoords)+"''";
    } else if (coordType==COORDMINMMMTYPE){
      icoordg=Math.abs(icoord);
      icoordm=(int) (dcoord*60.);
      icoords=(int) ((dcoord-(icoordm/60.))*60000.);

      s=a+MapUtil.numStr(icoordg, 2)+((char)0xb0)+MapUtil.numStr(icoordm, 2)+'.'+MapUtil.numStr(icoords, 3);
    } else if (coordType==COORDGGGMMMMMMTYPE){
      icoordg=Math.abs(icoord);
      icoordm=(int) (dcoord*60.);
      icoords=(int) ((dcoord-(icoordm/60.))*600000.);
      if (lontype==CLATTYPE)
      s=a+MapUtil.numStr(icoordg, 2)+MapUtil.numStr(icoordm, 2)+'.'+MapUtil.numStr(icoords, 4);
      else
      s=a+MapUtil.numStr(icoordg, 3)+MapUtil.numStr(icoordm, 2)+'.'+MapUtil.numStr(icoords, 4);
    } else if (coordType==COORDGGGGGGTYPE){
      icoordm=((int) (dcoord*100000.));
      icoordg=Math.abs(icoord);
      s=a+MapUtil.numStr(icoordg, 2)+'.'+MapUtil.numStr(icoordm, 5)+((char)0xb0);
    }

    return s;
  }

//  final static public String coord2EditString(double coord, byte lontype, byte coordType) {
//    int icoord=(int) (coord);
//    int icoordg, icoordm, icoords;
//    double dcoord, dcoordm, dcoords;
//    char a;
//
//    if (lontype==CLATTYPE){
//      if (coord>0){
//        dcoord=coord-icoord;
//        a=' ';
//      } else {
//        dcoord=icoord-coord;
//        a='-';
//      }
//    } else //if (lontype==CLONTYPE)
//    {
//      if (coord>0){
//        dcoord=coord-icoord;
//        a=' ';
//      } else {
//        dcoord=icoord-coord;
//        a='-';
//      }
//    }
//    String s=null;
//    if (coordType==RMSOption.COORDMINSECTYPE){
//      icoordm=(int) (dcoord*60.);
//      icoords=(int) ((dcoord-(icoordm/60.))*3600.);
//      dcoords=MapUtil.coordRound1((dcoord-(icoordm/60.))*3600.);
//      icoordg=Math.abs(icoord);
//      s=a+MapUtil.numStr(icoordg, 2)+' '+numStr(icoordm, 2)+' '+String.valueOf(dcoords);
////      s=a+MapUtil.make2(icoordg)+' '+ make2(icoordm)+' '+ make2(icoords);
//    } else if (coordType==RMSOption.COORDMINMMMTYPE){
//      icoordg=Math.abs(icoord);
//      icoordm=(int) (dcoord*60.);
//      icoords=(int) ((dcoord-(icoordm/60.))*60000.);
//
//      s=a+MapUtil.numStr(icoordg, 2)+' '+MapUtil.numStr(icoordm, 2)+'.'+MapUtil.numStr(icoords, 3);
//    } else if (coordType==RMSOption.COORDGGGGGGTYPE){
//      icoordm=((int) (dcoord*100000.));
//      icoordg=Math.abs(icoord);
//      s=a+MapUtil.numStr(icoordg, 2)+'.'+MapUtil.numStr(icoordm, 5);
//    }
//
//    return s;
//  }

  /**
   * Разбор координаты в указанном формате
   * RMSOption.COORDMINSECTYPE
   * RMSOption.COORDMINMMMTYPE
   * RMSOption.COORDGGGGGGTYPE
   * 
   */

  public static String numStr(int number, int digits) {
    StringBuffer s=new StringBuffer(digits);
    boolean rev=(number<0);
    if (rev){
      number=-number;
      digits--;
    }
    s.append(number);
    for (int i=s.length(); s.length()<digits; s.insert(0, '0'));
    
    if (rev) {
      s.insert(0, '-');
    }
    return s.toString();
  }
//    public static String numStrCrop(int number, int digits) {
//    String s;
//    for (s=MapUtil.emptyString+number; s.length()<digits; s="0"+s);
//    if (s.length()>digits) {
//      s=s.substring(s.length()-digits);
//    }
//    return s;
//  }

//  final static public String make2(int i) {
//    if (i < 10) {
//      StringBuffer sb = new StringBuffer(3);
//      sb.append('0').append(i);
//      return sb.toString();
//    } else {
//      return String.valueOf(i);
//    }
//  }
//
//  final static public String make3(int i) {
//    if (i < 10) {
//      StringBuffer sb = new StringBuffer(4);
//      sb.append('0').append('0').append(i);
//      return sb.toString();
//    } else if (i < 100) {
//      StringBuffer sb = new StringBuffer(4);
//      sb.append('0').append(i);
//      return sb.toString();
//    } else {
//      return String.valueOf(i);
//    }
//  }
//
//  final static public String make4(int i) {
//    boolean rev = false;
//    if (i < 0) {
//      i = -i;
//      rev = true;
//    }
//    if (rev) {
//      if (i < 10) {
//        StringBuffer sb = new StringBuffer(4);
//        sb.append('-').append('0').append('0').append(i);
//        return sb.toString();
//      } else if (i < 100) {
//        StringBuffer sb = new StringBuffer(4);
//        sb.append('-').append(i);
//        return sb.toString();
//      } else //if (i<10) return '-'+'0'+'0'+String.valueOf(i);
//      //if (i<100) return '-'+'0'+String.valueOf(i);
//      {
//        return String.valueOf(i);
//      }
//    } else {
//      if (i < 10) {
//        StringBuffer sb = new StringBuffer(4);
//        sb.append('0').append('0').append('0').append(i);
//        return sb.toString();
//      } else //    if (i<10) return '0'+'0'+'0'+String.valueOf(i);
//      if (i < 100) {
//        StringBuffer sb = new StringBuffer(4);
//        sb.append('0').append('0').append(i);
//        return sb.toString();
//      } else //if (i<100) return '0'+'0'+String.valueOf(i);
//      if (i < 1000) {
//        StringBuffer sb = new StringBuffer(4);
//        sb.append('0').append(i);
//        return sb.toString();
//      } else //      if (i<1000) return '0'+String.valueOf(i);
//      {
//        return String.valueOf(i);
//      }
//    }
//  }
//
//  final static public String make5(int i) {
//    boolean rev = false;
//    if (i < 0) {
//      i = -i;
//      rev = true;
//    }
//    if (rev) {
//      if (i < 10) {
//        return "-000" + String.valueOf(i);
//      } else //if (i<100) return '-'+'0'+'0'+String.valueOf(i);
//      if (i < 100) {
//        StringBuffer sb = new StringBuffer(5);
//        sb.append('-').append('0').append('0');
//        sb.append(i);
//        return sb.toString();
//      } else if (i < 1000) {
//        StringBuffer sb = new StringBuffer(5);
//        sb.append('-').append('0').append(i);
//        return sb.toString();
//      } else //if (i<1000) return '-'+'0'+String.valueOf(i);
//      {
//        return String.valueOf(i);
//      }
//    } else {
//      if (i < 10) {
//        return "0000" + String.valueOf(i);
//      } else if (i < 100) {
//        StringBuffer sb = new StringBuffer(5);
//        sb.append('0').append('0').append('0').append(i);
//        return sb.toString();
//      } else //if (i<100) return '0'+'0'+'0'+String.valueOf(i);
//      if (i < 1000) {
//        StringBuffer sb = new StringBuffer(5);
//        sb.append('0').append('0').append(i);
//        return sb.toString();
//      } else //if (i<1000) return '0'+'0'+String.valueOf(i);
//      if (i < 10000) {
//        StringBuffer sb = new StringBuffer(5);
//        sb.append('0').append(i);
//        return sb.toString();
//      } else //if (i<10000) return '0'+String.valueOf(i);
//      {
//        return String.valueOf(i);
//      }
//    }
//  }
  final public static double distRound3(double res) {
    boolean sign=res<0;
    if (sign){
      res=-res;
    }
    if (res<0.001){
      return 0;
    }
    if (res<1){
      res=((int) (res*1000d))/1000.d;
    } else if (res<5){
      res=((int) (res*100d))/100.d;
    } else {
      res=((int) (res*10d))/10.d;
    }
    if (sign){
      return -res;
    } else {
      return res;
    }
  }

  final public static double distRound2(double res) {
    boolean sign=res<0;
    if (sign){
      res=-res;
    }
    if (res<0.001){
      res=0;
    }
    if (res<5){
      res=((int) (res*100d))/100.d;
    } else {
      res=((int) (res*10d))/10.d;
    }
    if (sign){
      return -res;
    } else {
      return res;
    }
  }

  final public static double coordRound1(double res) {
    if (res<0.1){
      return 0;
    }
    return ((int) (res*10d))/10.d;
  //else return ((int)res);
  }

    final public static double doubleRound1(double res) {
     boolean sign=res<0;
    if (sign){
      res=-res;
    }
     if (res<0.1){
      return 0;
    }
    if (sign)
    return -(((int) (res*10d))/10.d);
    else
    return ((int) (res*10d))/10.d;

  //else return ((int)res);
  }

  final public static double speedRound1(double res) {
    if (res<0.1){
      return 0;
    }
    if (res<50){
      return ((int) (res*10d))/10.d;
    } else {
      return ((int) res);
    }
  }

  final public static double coordRound5(double res) {
    return ((int) (res*100000.d))/100000.d;
  }

  final public static double coordRound3(double res) {
    if (res<0.001){
      return 0;
    }
    return ((int) (res*1000.d))/1000.d;
  }
//  final static public String makeOdometer1(float f) {
//    int km = (int)(f/1000f);
//    int m = (int)(f - km*1000f);
//    return String.valueOf(km)+'.'+make3(m);
//  }


  final public static String[] parseString(String s, char delim) throws Exception {
    int i=0;
    int pos=0;
    int nextPos=0;
    String[] result=new String[150];
    // check how big the array is.
    while (pos>-1) {
      pos=s.indexOf(delim, pos);
      if (pos<0){
        continue;
      }
      pos++;
      i++;
    }

    if (i>500){
      throw new Exception("to big:"+i);
    }

    i=0;
    pos=0;

    // Start splitting the string, search for each ','
    try {


      while (pos>-1) {
        pos=s.indexOf(delim, pos);
        if (pos<0){
          continue;
        }

        nextPos=s.indexOf(delim, pos+1);
        if (nextPos<0){
          nextPos=s.length();
        }

        result[i]=s.substring(pos+1, nextPos).trim();
        i++;
        if (i==result.length) return result;

        if (i>result.length){
          break;
        }
        if (pos>-1){
          pos++;
        }
      }
    } catch (Throwable t) {
      //#mdebug
//#       if (RMSOption.debugEnabled){
//#         DebugLog.add2Log("parS:"+i+":"+pos+":"+t.toString());
//#       }
    //#enddebug
    }
    return result;
  }

  final public static void parseString(String s, char delim, String[] result) throws Exception {
    int i=0;
    int pos=0;
    int nextPos=0;
    for (int j=result.length-1; j>=0; j--) {
      result[j]=null;    // check how big the array is.
    }
    while (pos>-1) {
      pos=s.indexOf(delim, pos);
      if (pos<0){
        continue;
      }
      pos++;
      i++;
    }

    if (i>500){
      throw new Exception("to big:"+i);
    }

    i=0;
    pos=0;

    // Start splitting the string, search for each ','
    while (pos>-1) {
      pos=s.indexOf(delim, pos);
      if (pos<0){
        continue;
      }

      nextPos=s.indexOf(delim, pos+1);
      if (nextPos<0){
        nextPos=s.length();
      }

      result[i]=s.substring(pos+1, nextPos).trim();
      i++;
      if (i==result.length) return;
      if (pos>-1){
        pos++;
      }
    }

  }


//    public final static void ARGB_Img_Scale(int[] srcRGB, int[] dstRGB, int srcW, int srcH, int dstW, int dstH) {
//    int x0, y0, x1, y1;
//    int pix;
//    double xRate = srcW/dstW;
//    double yRate = srcH/dstH;
//    
//    for (y1=dstH-1; y1>=0; y1--) {
//      for (x1=dstW-1; x1>=0; x1--) {
//        pix=0;
//        
//        dstRGB[y1*dstW+x1]=pix;
//      }
//    }
//          ARGB_Img1[y1*2*ImW+x1*2]=ARGB_Img0[(y1+yc)*ImW+x1+xc];
//  }

 
  public final static void ARGB_Img_Invert(int[] ARGB_Img0, int ImW, int ImH) {
    for (int y1=ImH-1; y1>=0; y1--) {
      for (int x1=ImW-1; x1>=0; x1--) {
        ARGB_Img0[(y1)*ImW+x1]=~(ARGB_Img0[(y1)*ImW+x1]);
      }
    }
  }

  public final static int invertColor(int color) {
    return (~color)&0x00FFFFFF;
  }
  private final static double ro=206264.8062471;// ' Число угловых секунд в радиане
//' Эллипсоид Красовского
  public final static double aP=6378245;// ' Большая полуось
  private final static double alP=1/298.3;// ' Сжатие
  private final static double e2P=2*alP-alP*alP;// ' Квадрат эксцентриситета
//' Эллипсоид WGS84 (GRS80, эти два эллипсоида сходны по большинству параметров)
  public final static double aW=6378137;//' Большая полуось
  private final static double alW=1/298.257223563;// ' Сжатие
  private final static double e2W=2*alW-alW*alW;//' Квадрат эксцентриситета
//' Вспомогательные значения для преобразования эллипсоидов
  private final static double _a=(aP+aW)/2.;
  private final static double e2=(e2P+e2W)/2.;
  private final static double da=aW-aP;
  private final static double da1=aW-aP;
  private final static double de2=e2W-e2P;//' Линейные элементы трансформирования, в метрах
  private final static double dx=24;//25
  private final static double dy=-141;//132
  private final static double dz=-81;//83
//' Угловые элементы трансформирования, в секундах
  private final static double wx=0;
  private final static double wy=-0.35;
  private final static double wz=-0.82;
//' Дифференциальное различие масштабов
  private final static double ms=(0-0.12d)*0.000001d;

  final static double power(double base, double pow) {
    //x^y = e^(y*log(x))
    return exp(pow*ln(base));
  }

  static final double dB(double Bd, double Ld, double H) {
    double B=Bd*G2R;//0.017453292519943295769236907684886;
    double L=Ld*G2R;//0.017453292519943295769236907684886;
    double M=_a*(1-e2)/power((1-e2*Math.sin(B)*Math.sin(B)), 1.5);
    double N=_a*power((1-e2*Math.sin(B)*Math.sin(B)), -0.5);
    double dBr=ro/(M+H)*(N/_a*e2*Math.sin(B)*Math.cos(B)*da+(N*N/(_a*_a)+1)*N*Math.sin(B)*Math.cos(B)*de2*0.5-(dx*Math.cos(L)+dy*Math.sin(L))*Math.sin(B)+dz*Math.cos(B))-wx*Math.sin(L)*(1+e2*Math.cos(2*B))+wy*Math.cos(L)*(1+e2*Math.cos(2*B))-ro*ms*e2*Math.sin(B)*Math.cos(B);
    return dBr;
  }

  static final double WGS84_SK42_Lat(double Bd, double Ld, double H) {
    return Bd-dB(Bd, Ld, H)*2.7777777777777777777777777777778e-4;// / 3600.0;
  }

  static final double dL(double Bd, double Ld, double H) {
    double B=Bd*G2R;//0.017453292519943295769236907684886;
    double L=Ld*G2R;//0.017453292519943295769236907684886;
    double N=_a*power((1-e2*Math.sin(B)*Math.sin(B)), -0.5);
    double dLr=ro/((N+H)*Math.cos(B))*(-dx*Math.sin(L)+dy*Math.cos(L))+Math.tan(B)*(1-e2)*(wx*Math.cos(L)+wy*Math.sin(L))-wz;
    return dLr;
  }

  static final double WGS84_SK42_Long(double Bd, double Ld, double H) {
    return Ld-dL(Bd, Ld, H)*2.7777777777777777777777777777778e-4;// / 3600.0;
  }

  static final String coordTM2String(double coord, boolean lat, boolean sign) {
    if (lat){
      if (sign){
        return String.valueOf((int) coord)+'E';
      } else {
        return String.valueOf((int) coord)+'W';
      }
    } else {
      if (sign){
        return String.valueOf((int) coord)+'N';
      } else {
        return String.valueOf((int) coord)+'S';
      }
    }
  }
  private static double[] resSK=new double[3];
  private static char[] resZone=new char[1];

  private static Calendar calendar=Calendar.getInstance(TimeZone.getDefault());
//  {
//    calendar.setTime(new Date());
//  }
  final public static String UTC2Local(long time) {
    String res;
    try {
      calendar.setTime(new Date(time));
      res=
        MapUtil.numStr(calendar.get(Calendar.HOUR_OF_DAY), 2)+':'+
        MapUtil.numStr(calendar.get(Calendar.MINUTE), 2)+':'+
        MapUtil.numStr(calendar.get(Calendar.SECOND), 2);

    } catch (Throwable t) {
      res=String.valueOf(time);
    }
    return res;
  }

  final public static String trackNameAuto() {
    return trackName(System.currentTimeMillis());
  }

  final public static String trackName(long time) {
    String res;
    try {
      Calendar cal;
      (cal=Calendar.getInstance()).setTime(new Date(time));
      res=MapUtil.numStr(cal.get(Calendar.YEAR), 4)+
        MapUtil.numStr(1+cal.get(Calendar.MONTH), 2)+
        MapUtil.numStr(cal.get(Calendar.DAY_OF_MONTH), 2)+
        '_'+
        MapUtil.numStr(cal.get(Calendar.HOUR_OF_DAY), 2)+
        MapUtil.numStr(cal.get(Calendar.MINUTE), 2);
    } catch (Throwable t) {
      res=String.valueOf(time);
    }
    return res;
  }

  final public static String trackNameSec() {
    String res;
    try {
      Calendar cal;
      (cal=Calendar.getInstance()).setTime(new Date());
      res=MapUtil.numStr(cal.get(Calendar.YEAR), 4)+
        MapUtil.numStr(1+cal.get(Calendar.MONTH), 2)+
        MapUtil.numStr(cal.get(Calendar.DAY_OF_MONTH), 2)+
        '_'+
        MapUtil.numStr(cal.get(Calendar.HOUR_OF_DAY), 2)+
        MapUtil.numStr(cal.get(Calendar.MINUTE), 2)+MapUtil.numStr(cal.get(Calendar.SECOND), 2);
    } catch (Throwable t) {
      res=String.valueOf(System.currentTimeMillis());
    }
    return res;
  }

  final public static String dateTime2Str(long time) {
    String res;
    try {
      Calendar cal;
      (cal=Calendar.getInstance()).setTime(new Date(time));
      res=MapUtil.numStr(cal.get(Calendar.DAY_OF_MONTH), 2)+'.'+
        MapUtil.numStr(1+cal.get(Calendar.MONTH), 2)+'.'+
        MapUtil.numStr(cal.get(Calendar.YEAR), 4)+'.'+
        ' '+
        MapUtil.numStr(cal.get(Calendar.HOUR_OF_DAY), 2)+':'+
        MapUtil.numStr(cal.get(Calendar.MINUTE), 2)+':'+MapUtil.numStr(cal.get(Calendar.SECOND), 2);
    } catch (Throwable t) {
      res=String.valueOf(time);
    }
    return res;
  }


  final public static String checkFilename(String fn) {
    StringBuffer sb=new StringBuffer(fn);
    for (int i=0; i<sb.length(); i++) {
      if (sb.charAt(i)==':'){
        sb.setCharAt(i, '_');
      } else if (sb.charAt(i)=='.'){
        sb.setCharAt(i, '_');
      } else if (sb.charAt(i)=='-'){
        sb.setCharAt(i, '_');
      } else if (sb.charAt(i)=='/'){
        sb.setCharAt(i, '_');
      } else if (sb.charAt(i)=='\\'){
        sb.setCharAt(i, '_');
      } else if (sb.charAt(i)==' '){
        sb.setCharAt(i, '_');
      }
    }
    return sb.toString();
  }

  public final static String time2String(long time) {
    long hour=time/3600000;
    long min=(time-hour*3600000)/60000;
    long sec=(time-hour*3600000-min*60000)/1000;
    String s=numStr((int) hour, 2)+':'+numStr((int) min, 2)+':'+numStr((int) sec, 2);
    return s;
  }


  public static int[] colors={0x000000, 0x000080, 0x008000, 0x008080, 0x800000, 0x800080, 0x808000,
    0x808080, 0xC0C0C0, 0x0000FF, 0x00FF00, 0x00FFFF, 0xFF0000, 0xFF00FF, 0xFFFF00,
    0xC0C0C0, 0x808080, 0xFFFFFF
  };
  private static String Letters="ABCDEFGHJKLMNPQRSTUVWXYZ";

  public final static int getColorIndex(int color) {
    int ind=-1;
    for (int i=MapUtil.colors.length-1; i>=0; i--) {
      if (color==MapUtil.colors[i]){
        ind=i;
        break;
      }
    }
    return ind;
  }

  
  public static String extractFilename(String furl) {
    try {
      return furl.substring(furl.lastIndexOf('/')+1);
    } catch (Throwable ttt) {
      return furl.substring(furl.length()-6);
    }
  }

  public static int checkSum(byte[] data) {
    int sum=0;
    //int bi;
    for (int i=data.length-1; i>=0; i--) {
        //bi = data[i];
        //if (bi<0)bi+=256;
        //sum+=bi;
        sum+=data[i];
    }
    return sum;
  }
}


# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

        -dontoptimize
        -dontpreverify

        -dontwarn cn.jpush.**
        -keep class cn.jpush.** { *; }
        -dontwarn cn.jiguang.**
        -keep class cn.jiguang.** { *; }

        -dontwarn cn.com.chinatelecom.**
        -keep class cn.com.chinatelecom.** { *; }
        -dontwarn com.ct.**
        -keep class com.ct.** { *; }
        -dontwarn a.a.**
        -keep class a.a.** { *; }
        -dontwarn com.cmic.**
        -keep class com.cmic.** { *; }
        -dontwarn com.unicom.**
        -keep class com.unicom.** { *; }
        -dontwarn com.sdk.**
        -keep class com.sdk.** { *; }

        -dontwarn com.sdk.**
        -keep class com.sdk.** { *; }

        -keep class com.baidu.ocr.sdk.**{*;}
        -dontwarn com.baidu.ocr.**
        -keep class com.iflytek.**{*;}
        -keepattributes Signature

        #2D地图:
        -keep class com.amap.api.maps2d.**{*;}
        -keep class com.amap.api.mapcore2d.**{*;}

        #3D地图 V5.0.0之前：
        -keep class com.amap.api.maps.**{*;}
        -keep class com.autonavi.amap.mapcore.*{*;}
        -keep class com.amap.api.trace.**{*;}

        #3D地图 V5.0.0之后：
        -keep class com.amap.api.maps.**{*;}
        -keep class com.autonavi.**{*;}
        -keep class com.amap.api.trace.**{*;}

        #定位：
        -keep class com.amap.api.location.**{*;}
        -keep class com.amap.api.fence.**{*;}
        -keep class com.autonavi.aps.amapapi.model.**{*;}
        #搜索：
        -keep class com.amap.api.services.**{*;}

        #导航 V7.3.0以前：
        -keep class com.amap.api.navi.**{*;}
        -keep class com.alibaba.idst.nls.** {*;}
        -keep class com.nlspeech.nlscodec.** {*;}
        -keep class com.google.**{*;}

        #导航 V7.3.0及以后：
        -keep class com.amap.api.navi.**{*;}
        -keep class com.alibaba.mit.alitts.*{*;}
        -keep class com.google.**{*;}

        #导航 V8.1.0及以后：
        -keep class com.amap.api.navi.**{*;}
        -keep class com.alibaba.idst.nui.* {*;}
        -keep class com.google.**{*;}
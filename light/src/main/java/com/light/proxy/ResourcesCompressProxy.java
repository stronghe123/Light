package com.light.proxy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.light.body.Light;
import com.light.body.LightConfig;
import com.light.core.LightCompressEngine;
import com.light.core.Utils.DisplayUtil;
import com.light.core.Utils.L;
import com.light.core.Utils.MatrixUtil;
import com.light.core.listener.ICompressEngine;
import com.light.core.listener.ICompressProxy;

import java.lang.ref.SoftReference;

/**
 * Created by xiaoqi on 2017/11/25.
 */

public class ResourcesCompressProxy implements ICompressProxy {
	private final static String TAG = Light.TAG + "-ResourcesCompressProxy";
	private int resId;
	private int width;
	private int height;
	private LightConfig lightConfig;
	private ICompressEngine compressEngine;

	public ResourcesCompressProxy() {
		lightConfig = Light.getInstance().getConfig();
		compressEngine = new LightCompressEngine();
	}

	@Override
	public boolean compress(String outPath) {
		int resultWidth;
		int resultHeight;
		if(width > 0 && height >0){
			resultWidth = width;
			resultHeight = height;
		}else {
			resultWidth = lightConfig.getMaxWidth();
			resultHeight = lightConfig.getMaxHeight();
		}
		L.i(TAG, "finalWidth:"+resultWidth+" finalHeight:"+resultHeight);
		Bitmap result = compress();
		return compressEngine.compress2File(result, outPath, lightConfig.getDefaultQuality(), resultWidth, resultHeight);
	}

	@Override
	public Bitmap compress() {
		int resultWidth;
		int resultHeight;
		if(width > 0 && height >0){
			resultWidth = width;
			resultHeight = height;
		}else {
			resultWidth = lightConfig.getMaxWidth();
			resultHeight = lightConfig.getMaxHeight();
		}
		L.i(TAG, "finalWidth:"+resultWidth+" finalHeight:"+resultHeight);
		Bitmap result = compressEngine.compress2Bitmap(resId, resultWidth, resultHeight);
		float scaleSize = MatrixUtil.getScale(resultWidth, resultHeight, result.getWidth(), result.getHeight());
		if(scaleSize < 1){
			return new MatrixUtil.Build().scale(scaleSize, scaleSize).bitmap(result).build();
		}
		return result;
	}

	public static class Build {
		private int resId;
		private int width;
		private int height;

		public Build resource(@DrawableRes int resId) {
			this.resId = resId;
			return this;
		}

		public Build width(int width) {
			this.width = width;
			return this;
		}

		public Build height(int height) {
			this.height = height;
			return this;
		}

		public ResourcesCompressProxy build(){
			ResourcesCompressProxy proxy = new ResourcesCompressProxy();
			proxy.width = width;
			proxy.height = height;
			proxy.resId = resId;
			return proxy;
		}
	}

}
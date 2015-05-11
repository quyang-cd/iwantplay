package jintli.iwantplay.support;

import com.sina.sae.memcached.SaeMemcache;
/**
 * 单例 sae memcache封装
 * @author lijing3
 *
 */
public class MyMemeray {
	private MyMemeray(){}
	public static SaeMemcache getInstance() {
		SaeMemcache instance = new SaeMemcache();
		instance.init();
		return instance;
	}
}

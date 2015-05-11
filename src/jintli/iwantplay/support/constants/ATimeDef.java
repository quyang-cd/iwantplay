package jintli.iwantplay.support.constants;

import java.util.HashMap;
import java.util.Map;

public enum ATimeDef {
	BASKETBALL("0", "今天下午"),
	FOOTBALL("1", "明天上午"),
	YUMAOQIU("2", "明天下午"),
	PINGPANG("3", "后天上午"),
	SWIMMING("4", "今天上午"),
	JIANSHEN("5", "后天下午");

    private String atime;
    private String name;
    public static final Map<String, String> YUNDONGMAP = new HashMap<String, String>();

    ATimeDef(String atime, String name) {
        this.atime = atime;
        this.name = name;
    }



	public String getAtime() {
		return atime;
	}

	public void setAtime(String atime) {
		this.atime = atime;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

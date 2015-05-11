package jintli.iwantplay.support.constants;

import java.util.HashMap;
import java.util.Map;

public enum ATypeDef {
	BASKETBALL("0","0", "篮球"),
	FOOTBALL("0","1", "足球"),
	YUMAOQIU("0","2", "羽毛球"),
	PINGPANG("0","3", "乒乓球"),
	SWIMMING("0","4", "游泳"),
	JIANSHEN("0","5", "健身"),
	YOGA("0","6", "瑜伽"),
	WANGQIU("0","7", "网球"),
	
	XIANGQI("1","a", "象棋"),
	WEIQI("1","b", "围棋"),
	MAJIANG("1","c", "麻将"),
	JINGHUA("1","d", "炸金花"),
    CHATTING("1","e", "龙门阵"),
    SHAREN("1","f", "杀人游戏"),
    SANGUOSHA("1","g", "三国杀"),
	TAIQIU("1","h", "台球"),
	KTV("1","i", "K歌");

    private String atype0;
    private String atype1;
    private String name;
    public static final Map<String, String> YUNDONGMAP = new HashMap<String, String>();
    public static final Map<String, String> XIUXIANMAP = new HashMap<String, String>();
    static {
    	YUNDONGMAP.put(BASKETBALL.getAtype1(), BASKETBALL.getName());
    	YUNDONGMAP.put(FOOTBALL.getAtype1(), FOOTBALL.getName());
    	YUNDONGMAP.put(YUMAOQIU.getAtype1(), YUMAOQIU.getName());
    	YUNDONGMAP.put(PINGPANG.getAtype1(), PINGPANG.getName());
    	/*YUNDONGMAP.put(SWIMMING.getAtype1(), SWIMMING.getName());
    	YUNDONGMAP.put(JIANSHEN.getAtype1(), JIANSHEN.getName());
    	YUNDONGMAP.put(YOGA.getAtype1(), YOGA.getName());*/
    	YUNDONGMAP.put(WANGQIU.getAtype1(), WANGQIU.getName());
    	
    	//XIUXIANMAP.put(XIANGQI.getAtype1(), XIANGQI.getName());
    	//XIUXIANMAP.put(WEIQI.getAtype1(), WEIQI.getName());
    	//XIUXIANMAP.put(MAJIANG.getAtype1(), MAJIANG.getName());
    	//XIUXIANMAP.put(JINGHUA.getAtype1(), JINGHUA.getName());
    	XIUXIANMAP.put(CHATTING.getAtype1(), CHATTING.getName());
    	XIUXIANMAP.put(SHAREN.getAtype1(), SHAREN.getName());
    	XIUXIANMAP.put(SANGUOSHA.getAtype1(), SANGUOSHA.getName());
    	XIUXIANMAP.put(TAIQIU.getAtype1(), TAIQIU.getName());
    	XIUXIANMAP.put(KTV.getAtype1(), KTV.getName());
    }
    public static String getNameByAtype1(String atype1) {
    	return YUNDONGMAP.get(atype1) == null ? XIUXIANMAP.get(atype1) : YUNDONGMAP.get(atype1);
    }
    ATypeDef(String atype0,String atype1, String name) {
        this.atype0 = atype0;
        this.atype1 = atype1;
        this.name = name;
    }


    public String getAtype0() {
		return atype0;
	}

	public void setAtype0(String atype0) {
		this.atype0 = atype0;
	}

	public String getAtype1() {
		return atype1;
	}

	public void setAtype1(String atype1) {
		this.atype1 = atype1;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package enums;

public enum StickType {
	GK("gk"),
	DEF("def"),
	MID("mid"),
	FWD("fwd");
	
	private String name;
	
	private StickType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}

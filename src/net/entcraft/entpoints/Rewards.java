package net.entcraft.entpoints;

public enum Rewards {
	
	RegularRank(1, "Regular Rank", "Upgrade your rank to a Regular", 3000, false, "INSERT COMMAND HERE"),
	LegendaryRank(2, "Legendary Rank", "Upgrade your rank to be Legendary", 20000, false, RegularRank, "INSERT COMMAND HERE"),
	DonatorRank(3, "Donator Rank", "Become a Donator - adds on to ordinary ranks", 500, true, "INSERT COMMAND HERE");
	
	private int ID;
	private String title;
	private String desc;
	private int pointsNeeded;
	private boolean donatedPointsOnly;
	private Rewards required = null;
	private RewardCommandSequence commandSequence;
	
	private Rewards(int id, String t, String d, int pNeeded, boolean dPointsOnly, String... cmdS) {
		ID = id;
		title = t;
		desc = d;
		pointsNeeded = pNeeded;
		donatedPointsOnly = dPointsOnly;
		commandSequence = new RewardCommandSequence(this, cmdS);
	}
	
	private Rewards(int id, String t, String d, int pNeeded, boolean dPointsOnly, Rewards requires, String... cmdS) {
		this(id, t, d, pNeeded, dPointsOnly, cmdS);
		required = requires;
	}
	
	public static Rewards getReward(int id) {
		for (int i = 0; i < Rewards.values().length; i++) {
			if (id == Rewards.values()[i].getID()) {
				return Rewards.values()[i];
			}
		}
		return null;
	}
	
	public static Rewards getReward(String t) {
		for (int i = 0; i < Rewards.values().length; i++) {
			if (t.equalsIgnoreCase(Rewards.values()[i].getTitle())) {
				return Rewards.values()[i];
			}
		}
		return null;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public boolean donatedPointsOnly() {
		return donatedPointsOnly;
	}
	
	public int getPointsNeeded() {
		return pointsNeeded;
	}
	
	public Rewards getRequired() {
		return required;
	}
	
	public RewardCommandSequence getCommandSequence() {
		return commandSequence;
	}

}

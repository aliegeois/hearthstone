package fr.ministone.message;

public class MessageHeroTargetedSpecial {
	private boolean own;
	private String targetId;

	public MessageHeroTargetedSpecial() {}

	public MessageHeroTargetedSpecial(boolean own, String targetId) {
		this.own = own;
		this.targetId = targetId;
	}

	public boolean isOwn() {
		return own;
	}

	public String getTargetId() {
		return targetId;
	}

	public void SetOwn(boolean newOwn) {
		own = newOwn;
	}

	public void setTargetId(String newTargetId) {
		targetId = newTargetId;
	}
}
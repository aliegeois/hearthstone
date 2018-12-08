package server.message;

public class MessageCastTargetedSpell extends GameMessage {

	private int source, target;

	public MessageCastTargetedSpell() {}

	public MessageCastTargetedSpell(boolean player1, int idCardSource, int idCardTarget) {
		super(player1);
		this.source = idCardSource;
		this.target = idCardTarget;
	}

	public int getSource() {
		return source;
	}

	public int getTarget() {
		return target;
	}

	public void setSource(int idCard) {
		this.source = idCard;
	}

	public void setTarget(int idCard) {
		this.target = idCard;
	}
}
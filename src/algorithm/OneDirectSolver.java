package algorithm;

public class OneDirectSolver {

	private MyListForLeaderElection<Agent> list;
	
	public OneDirectSolver(MyListForLeaderElection<Agent> list) {
		this.list = list;
	}
	
	public void solve(int step) {
		if (step == 0) {
			initiateStartState();
		} else {
			setMessages();
		}
	}

	private void setMessages() {
		Agent current;
		Agent neiborough;
		while (list.hasNext()) {
			current = list.next();
			neiborough = list.getNextNeiborough();
			if (current.getId() < current.getLeftMsg()) {
				neiborough.setNewLeftMsg(current.getLeftMsg());
			} else {
				neiborough.setNewLeftMsg(0);
			}
		}
		while (list.hasNext()) {
			list.next().updateLeftMsgs();;
		}
	}

	private void initiateStartState() {
		Agent current;
		Agent neiborough;
		while (list.hasNext()) {
			current = list.next();
			neiborough = list.getNextNeiborough();
			neiborough.setLeftMsg(current.getId());
			neiborough.setNewLeftMsg(current.getId());
		}
	}

	public String printMsgs() {
		String s = "";
		Agent next;
		while (list.hasNext()) {
			next = list.next();
			if (next.getLeftMsg() != 0) {
				s += "<font color = red>";
			} else {
				s += "<font color = balck>";
			}
			s += next.getLeftMsg();
			s += "</font> ";
		}
		return s;
	}

}

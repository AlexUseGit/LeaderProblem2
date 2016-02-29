package algorithm;

public class MyRingList {

	private int size;
	private Node head;
	private Node current;
	private Node tail;
	private int curIdx;

	private class Node {
		Agent info;
		Node next;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void add(Agent agent) {
		Node tmp = new Node();
		tmp.info = agent;
		if (size == 0) {
			head = tmp;
			current = head;
			tail = tmp;
		}
		tail.next = tmp;
		tail = tmp;
		tmp.next = head;
		size++;
	}

	public int size() {
		return size;
	}

	@Override
	public String toString() {
		String s = "(";
		while (hasNext()) {
			s += " ";
			s += next().getId();
		}
		s += ")";
		return s;
	}

	public Agent next() {
		Node tmp = current;
		current = current.next;
		curIdx++;
		return tmp.info;
	}

	public boolean hasNext() {
		if (curIdx >= size) {
			curIdx = 0;
			return false;
		}
		return true;
	}

	public Agent getNeiborough() {
		return current.info;
	}

	public void setMessages() {
		Agent next;
		while (hasNext()) {
			next = next();
			next.setMsg(next.getNewMsg());
		}
	}

	public String printMsgs() {
		String s = "";
		while (hasNext()) {
			s += next().getMsg() + " ";
		}
		return s;
	}

}

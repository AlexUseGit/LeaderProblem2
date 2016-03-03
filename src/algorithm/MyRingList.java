package algorithm;

public class MyRingList implements MyAbstractList{

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
		String s = "";
		while (hasNext()) {
			s += next().getId();
			if (curIdx < size)
				s += ", ";
		}
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
			if (next.getId() < next.getMsg()) {
				getNeiborough().setNewMsg(next.getMsg());
			} else {
				getNeiborough().setNewMsg(0);
			}
		}
		while (hasNext()) {
			next = next();
			next.setMsg(next.getNewMsg());
		}
	}

	public String printMsgs() {
		String s = "";
		Agent next;
		while (hasNext()) {
			next = next();
			if (next.getMsg() != 0) {
				s += "<font color = red>";
			} else {
				s += "<font color = balck>";
			}
			s += next.getMsg();
			s += "</font>";
			if (curIdx < size)
				s += ", ";
		}
		return s;
	}

//	public Agent next(int i) {
//		Node tmp = current;
//		for (int j = 0; j < i - 1; j++) {
//			tmp = tmp.next;
//		}
//		return tmp.info;
//	}

}

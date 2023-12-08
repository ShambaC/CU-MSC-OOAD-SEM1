package rwsn;

public class Message<T> {
	public int id;
	public MessageTypes type;
	public T data;
	public Message(int id, MessageTypes type, T data) {
		this.id = id;
		this.type = type;
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Msg:"+id+"->"+data;
	}
	
	
}

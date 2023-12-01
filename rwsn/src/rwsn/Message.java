package rwsn;

public class Message {
    int id;
    String type;
    int data;
    double ddata;
    String sdata;

    Message(int id, String type) {
        this.id = id;
        this.type = type;

        switch (type) {
            case "integer":
                this.data = (int)(Math.random() * 20);
                break;
            case "double":
                this.ddata = Math.random() * 20;
                break;
            case "string":
                this.sdata = "Truly Random Text";
                break;          
            default:
                this.sdata = "Requires charging";
                break;
        }
    }
}

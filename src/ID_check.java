public class ID_check {
    protected static Boolean valid;
    protected static String text;

    public ID_check(Boolean valid, String text) {
        this.text = text;
        this.valid = valid;
    }

    public static ID_check check(String id){
        if(id.length() != 9)
            return new ID_check(false, "id number must have 9 numbers only!!");
        if (!id.matches("[0-9]+"))
            return new ID_check(false, "ID must contain numbers only!");
        return new ID_check(true, "");
    }
}

package dto;

public class AddressDTO {
    private int id;
    private String line1;
    private String line2;

    public AddressDTO(int id, String line1, String line2) {
        this.id = id;
        this.line1 = line1;
        this.line2 = line2;
    }

    public int getId() {
        return id;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }
}
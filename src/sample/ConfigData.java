package sample;

public class ConfigData {
    private String fieldName;
    private String value;
    private int beginIndex;
    private int endIndex;
    private String header;
    private String footer;

    /**
     * Constructor for the ConfigData object
     * @param fieldName, the name of the field
     * @param beginIndex, the beginning index of the field
     * @param endIndex, the endIndex of the field
     * @param value, the value of the field
     */
    public ConfigData(String fieldName, int beginIndex, int endIndex, String value) {
        this.fieldName = fieldName;
        this.beginIndex = beginIndex;
        this.endIndex = endIndex;
        this.value = value;
    }

    /**
     * Get the header value
     * @return header, the header
     */
    public String getHeader() { 
    	return this.header;
    }

    /**
     * Set the header Value
     * @param header, the header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Get the footer value
     * @return footer, the footer
     */
    public String getFooter() {
        return this.footer;
    }

    /**
     * Set the footer value
     * @param footer, the footer
     */
    public void setFooter(String footer) {
        this.footer = footer;
    }

    /**
     * Get the beginning index of the field
     * @return beginIndex, the beginning index
     */
    public int getBeginIndex() {
        return this.beginIndex;
    }

    /**
     * Set the beginning index of the field
     * @param beginIndex, the beginning index
     */
    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    /**
     * Get the ending index of the field
     * @return endIndex, the ending index
     */
    public int getEndIndex() {
        return this.endIndex;
    }

    /**
     * Set the ending index of the field
     * @param endIndex, the ending index
     */
    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    /**
     * Get the field name
     * @return fieldName, the field name
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * Set the field name
     * @param fieldName, the field name
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Get the value of the field
     * @return value, the value
     */
    public String getValue() {
        return this.value;
    }
    
    /**
     * Set the value of the field
     * @param value, the value of the field
     */
    public void setValue(String value) {
        this.value = value;
    }
}

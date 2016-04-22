public class StringNode {

    private String value;
    private Integer createTime;
    private Integer timeOut;
    private Integer lastUseTime;

    public StringNode(String value, Integer createTime, int timeOut) {
        this.setValue(value);
        this.setCreateTime(createTime);
        this.setTimeOut(timeOut);
        this.setLastUseTime(createTime);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Integer lastUseTime) {
        this.lastUseTime = lastUseTime;
    }
}

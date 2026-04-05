package data_model;

public final class SimpleTask extends Task {
    private int startHour;
    private int endHour;
    public SimpleTask(int idTask, String statusTask, int startHour, int endHour) {
        super(idTask, statusTask);
        this.startHour = startHour;
        this.endHour = endHour;
    }
    public int getStartHour() {
        return startHour;
    }
    void setStartHour(int startHour) {
        this.startHour = startHour;
    }
    public int getEndHour() {
        return endHour;
    }
    void setEndHour(int endHour) {
        this.endHour = endHour;
    }
    @Override
    public int estimateDuration() {
        if(endHour < startHour){
            return 24 - startHour + endHour;
        }
        else return endHour -startHour;
    }
}

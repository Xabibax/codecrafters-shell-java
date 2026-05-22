package app.models.result;

public interface Result {

    int SUCCESS = 0;
    int FAIL = 1;
    int WARNING = 2;
    int IO_FAIL = 3;

    String getOutput();
    String getErrorOutput();
    int getCode();

}

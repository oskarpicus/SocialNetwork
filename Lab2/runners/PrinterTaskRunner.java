package runners;

import java.time.LocalDateTime;

public class PrinterTaskRunner extends AbstractTaskRunner{

    public PrinterTaskRunner(TaskRunner taskRunner) {
        super(taskRunner);
    }

    @Override
    protected void afterExecution() {
        System.out.println(LocalDateTime.now());
    }

}

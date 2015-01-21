package batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ExecutionListener implements StepExecutionListener {

    static Stopwatch stopwatch;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Migration started at " + stepExecution.getStartTime());
        stopwatch = new Stopwatch();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("Migration finished at " + new Date());
        System.out.println("Albums readed: " + stepExecution.getReadCount());
        System.out.println("Albums writed: " + stepExecution.getWriteCount());
        System.out.println("Elapsed time, s: " + stopwatch.elapsedTime());
        return null;
    }
}

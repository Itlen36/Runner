package com.aldebaran.runner;

import com.aldebaran.runner.model.CheckerReply;
import com.aldebaran.runner.model.RunData;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class ChecksResend {
    @Inject
    RunnerMain runnerMain;

    @ConfigProperty(name = "ChecksResend.responseTimeout", defaultValue="4")
    int responseTimeout;

    private AtomicInteger counter = new AtomicInteger();

    public int get() {
        return counter.get();
    }

    @Scheduled(every="5s")
    @Transactional
    void increment() {
        CheckerReply checkerReply = CheckerReply.getCheckerReply();
        if (checkerReply.getRequestsCounter()==0 || !checkerReply.isAllowRepeatedRequests())
            return;
        LocalDateTime timeLastReply = LocalDateTime.of(checkerReply.getLastReplyDate().toLocalDate(),checkerReply.getLastReplyTime().toLocalTime());
        if (Duration.between(timeLastReply, LocalDateTime.now()).getSeconds() > responseTimeout) {
            checkerReply.setTime();
            checkerReply.persist();
            List<RunData> awaitingRuns = RunData.getAwaitingCheck();
            for (RunData awaitingRun : RunData.getAwaitingCheck())
                runnerMain.sendChecks(awaitingRun, false);
        }
    }
}
